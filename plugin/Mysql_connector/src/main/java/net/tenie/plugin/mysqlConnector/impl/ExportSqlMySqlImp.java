package net.tenie.plugin.mysqlConnector.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.tenie.Sqlucky.sdk.db.DBTools;
import net.tenie.Sqlucky.sdk.db.Dbinfo;
import net.tenie.Sqlucky.sdk.db.ExportDBObjects;
import net.tenie.Sqlucky.sdk.po.db.FuncProcTriggerPo;
import net.tenie.Sqlucky.sdk.po.db.TableForeignKeyPo;
import net.tenie.Sqlucky.sdk.po.db.TableIndexPo;
import net.tenie.Sqlucky.sdk.po.db.TablePo;
import net.tenie.Sqlucky.sdk.utility.FetchDBInfoCommonTools;


/**
 * 
 * @author tenie
 *
 */
public class ExportSqlMySqlImp implements ExportDBObjects {
	private static Logger logger = LogManager.getLogger(ExportSqlMySqlImp.class);

	private FetchDBInfoCommonTools fdb2;
 
	public ExportSqlMySqlImp() {
		fdb2 = new FetchDBInfoCommonTools();

	}


	/**
	 * 导出所有表对象, 属性: 表名, 字段, 主键, ddl
	 */
	@Override
	public List<TablePo> allTableObj(Connection conn, String schema) {
		try {
			List<TablePo> vals = Dbinfo.fetchAllTableName(conn, schema);
			return vals;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 视图对象
	 */
	@Override
	public List<TablePo> allViewObj(Connection conn, String schema) {
		try {
			// 获取视图名称
			List<TablePo> vals = Dbinfo.fetchAllViewName(conn, schema);
			if (vals != null && vals.size() > 0) {
				vals.stream().forEach(v -> {
					// 视图ddl
					String ddl = exportCreateView(conn, schema, v.getTableName());
					v.setDdl(ddl);
				});
			}
			return vals;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 函数对象
	 */
	@Override
	public List<FuncProcTriggerPo> allFunctionObj(Connection conn, String schema) {
		try {
			// 函数名称
			List<FuncProcTriggerPo> vals = Dbinfo.fetchAllFunctions(conn, schema);
			if (vals != null && vals.size() > 0) {
				vals.forEach(v -> {
					String ddl = exportCreateFunction(conn, schema, v.getName());
					v.setDdl(ddl);
				});
			}

			return vals;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
// 获取 mysql 版本
	public String getVersion(Connection conn) {
		String sql = "select version()";
		String v  = ""; 
		v  = DBTools.selectOne(conn, sql);
		 
		return v;
	}
	/**
	 * 过程对象
	 */
	@Override
	public List<FuncProcTriggerPo> allProcedureObj(Connection conn, String schema) {
		try {
			// 函数名称
			String ver = getVersion(conn);

			String sql = "  select  name,  comment  from mysql.proc where db = '" + schema
					+ "' and `type` = 'PROCEDURE'";
			if(ver.startsWith("8")) {
			      sql = "SELECT   ROUTINE_NAME as name  , ROUTINE_COMMENT as comment "
						+ "    FROM INFORMATION_SCHEMA.ROUTINES where ROUTINE_TYPE = 'PROCEDURE' and  ROUTINE_SCHEMA ='"+schema+"'" ;
				
			}
			List<FuncProcTriggerPo> vals = fetchAllProcedures(conn, schema, sql);
			if (vals != null && vals.size() > 0) {
				vals.forEach(v -> {
					String ddl = exportCreateProcedure(conn, schema, v.getName());
					v.setDdl(ddl);
				});
			}

			return vals;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 触发器对象
	 */
	@Override
	public List<FuncProcTriggerPo> allTriggerObj(Connection conn, String schema) {
		try {
			String sql = "SELECT * FROM information_schema.TRIGGERS where TRIGGER_SCHEMA = '" +schema + "'";
			// 函数名称 
			List<FuncProcTriggerPo> vals = fetchAllTriggers(conn, schema, sql);
			if (vals != null && vals.size() > 0) {
				vals.forEach(v -> {
					String ddl = exportCreateTrigger(conn, schema, v.getName());
					v.setDdl(ddl);
				});
			}

			return vals;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	public static List<FuncProcTriggerPo> fetchAllTriggers(Connection conn, String schemaOrCatalog, String sql)
			throws Exception {
		List<FuncProcTriggerPo> ls = new ArrayList<FuncProcTriggerPo>();
		ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("TRIGGER_NAME");
//				String remarks = rs.getString("comment");
				String ddl = rs.getString("ACTION_STATEMENT");
				
				FuncProcTriggerPo po = new FuncProcTriggerPo();
				po.setName(name);
				po.setRemarks("");
				po.setDdl(ddl);
				po.setSchema(schemaOrCatalog);
				ls.add(po);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		return ls;
	}
	
	// 表对象ddl语句
	@Override
	public String exportCreateTable(Connection conn, String schema, String tab) {
		String sql = "SHOW CREATE TABLE " + schema + "." + tab;
		String ddl = getddlHelper(conn, sql, 2);
		return ddl;
	}

	@Override
	public String exportCreateView(Connection conn, String schema, String obj) {
		String sql = "SHOW CREATE VIEW  " + schema + "." + obj;
		String ddl = getddlHelper(conn, sql, 2);
		return ddl;
	}

	@Override
	public String exportCreateFunction(Connection conn, String schema, String obj) {
		String sql = "SHOW CREATE FUNCTION  " + schema + "." + obj;
		String ddl = getddlHelper(conn, sql, 3);
		return ddl;
	}

	@Override
	public String exportCreateProcedure(Connection conn, String schema, String obj) {
		String sql = "SHOW CREATE PROCEDURE  " + schema + "." + obj;
		String ddl = getddlHelper(conn, sql, 3);
		logger.info(ddl);
		return ddl;
	}

	@Override
	public String exportCreateIndex(Connection conn, String schema, String obj) { 
		String sql = "SELECT \n" + 
				"CONCAT('ALTER TABLE `',TABLE_NAME,'` ', 'ADD ', \n" + 
				"IF( any_value(NON_UNIQUE )= 1, \n" + 
				"CASE UPPER(any_value(INDEX_TYPE)) \n" + 
				"WHEN 'FULLTEXT' THEN 'FULLTEXT INDEX' \n" + 
				"WHEN 'SPATIAL' THEN 'SPATIAL INDEX' \n" + 
				"ELSE CONCAT('INDEX `', \n" + 
				"INDEX_NAME, \n" + 
				"'` USING ', \n" + 
				"any_value(INDEX_TYPE) \n" + 
				") \n" + 
				"END, \n" + 
				"IF(UPPER(INDEX_NAME) = 'PRIMARY', \n" + 
				"CONCAT('PRIMARY KEY USING ', \n" + 
				"any_value(INDEX_TYPE) \n" + 
				"), \n" + 
				"CONCAT('UNIQUE INDEX `', \n" + 
				"INDEX_NAME, \n" + 
				"'` USING ', \n" + 
				"any_value(INDEX_TYPE) \n" + 
				") \n" + 
				") \n" + 
				"),'(', GROUP_CONCAT(DISTINCT CONCAT('`', COLUMN_NAME, '`') ORDER BY SEQ_IN_INDEX ASC SEPARATOR ', '), ');') AS 'Show_Add_Indexes' \n" + 
				"FROM information_schema.STATISTICS \n" + 
				"WHERE  \n" + 
				"      TABLE_SCHEMA = '"+schema+"'  \n" + 
				"  AND INDEX_NAME='"+obj+"' \n" + 
				"  AND any_value(NON_UNIQUE )= 1 \n"+
				"GROUP BY TABLE_NAME, INDEX_NAME \n" + 
				"ORDER BY TABLE_NAME ASC, INDEX_NAME ASC";
		
		String ddl = getddlHelper(conn, sql, 1);
		return ddl;
	}

	@Override
	public String exportCreateSequence(Connection conn, String schema, String obj) {
		 
		return "";
	}

	@Override
	public String exportCreateTrigger(Connection conn, String schema, String obj) {
		String sql = "SHOW CREATE TRIGGER  " + schema + "." + obj;
		String ddl = getddlHelper(conn, sql, 3);
		return ddl;
	}

	@Override
	public String exportCreatePrimaryKey(Connection conn, String schema, String obj) {
		// TODO 暂时不用
		return "";
	}

	@Override
	public String exportCreateForeignKey(Connection conn, String schema, String obj) {
		String ddl = fdb2.exportForeignKey(conn, schema, obj);
		return ddl;
	}

	@Override
	public String exportAlterTableAddColumn(Connection conn, String schema, String tableName, String newCol) {
		 
		String sql = "ALTER TABLE "+schema+"."+tableName+" ADD    " + newCol +";";
		return sql;
	}

	@Override
	public String exportAlterTableDropColumn(Connection conn, String schema, String tableName, String col) {
		String sql = "ALTER TABLE "+schema+"."+tableName+" DROP COLUMN   " + col +";";
		return sql;
	}

	@Override
	public String exportAlterTableModifyColumn(Connection conn, String schema, String tableName, String col) {
		String sql = "ALTER TABLE "+schema+"."+tableName+" MODIFY COLUMN  " + col +";";
		return sql;
	}

	@Override
	public String exportAlterTableAddPrimaryKey(Connection conn, String schema, String tableName, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exportAlterTableAddForeignKey(Connection conn, String schema, String tableName, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exportDropTable(String schema, String name) {
		String sql = "DROP TABLE " + schema + "." + name.trim();
		return sql;
	}

	@Override
	public String exportDropView(String schema, String name) {
		String sql = "DROP VIEW " + schema + "." + name.trim();
		return sql;
	}

	@Override
	public String exportDropFunction(String schema, String name) {
		String sql = "DROP  FUNCTION " + schema + "." + name.trim();
		return sql;
	}

	@Override
	public String exportDropProcedure(String schema, String name) {
		String sql = "DROP  PROCEDURE " + schema + "." + name.trim();
		return sql;
	}

	@Override
	public String exportDropIndex(String schema, String name, String tableName) {
		String sql = "DROP INDEX " + name.trim() + " on " + schema + "." + tableName;
		return sql;
	}

	@Override
	public String exportDropSequence(String schema, String name) {
		String sql = "DROP sequence " + schema + "." + name.trim() ;
		return sql;
	}

	@Override
	public String exportDropTrigger(String schema, String name) {
		String sql = "DROP TRIGGER " + schema + "." + name.trim();
		return sql;
	}

	@Override
	public String exportDropPrimaryKey(String schema, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exportDropForeignKey(String schema, String foreignKeyName, String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getddlHelper(Connection conn, String sql, int i) {

		String ddl = "";
		logger.info(sql);
		ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(sql);
			if (rs.next()) {
				ddl = rs.getString(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return ddl;
	}
	
	private List<String> getAllddlHelper(Connection conn, String sql, int i) {
		List<String> allDDl = new ArrayList<String>();
		String ddl = "";
		logger.info(sql);
		ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				ddl = rs.getString(i);
				allDDl.add(ddl);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return allDDl;
	}
	

	public static List<FuncProcTriggerPo> fetchAllProcedures(Connection conn, String schemaOrCatalog, String sql)
			throws Exception {
		List<FuncProcTriggerPo> ls = new ArrayList<FuncProcTriggerPo>();
		ResultSet rs = null;
		try {
			rs = conn.createStatement().executeQuery(sql);
			while (rs.next()) {
				String name = rs.getString("name");
				String remarks = rs.getString("comment");
				FuncProcTriggerPo po = new FuncProcTriggerPo();
				po.setName(name);
				po.setRemarks(remarks);
				po.setSchema(schemaOrCatalog);
				ls.add(po);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

		return ls;
	}

	@Override
	public List<FuncProcTriggerPo> allIndexObj(Connection conn, String schema) {
		try {
			String str = 
					"select  INDEX_NAME from information_schema.STATISTICS  \n" + 
					"where TABLE_SCHEMA = '"+schema+"' \n" + 
					"and NON_UNIQUE = 1 \n" ;
			// 获取名称
			List<String> allDDLs = getAllddlHelper(conn, str, 1);
			List<FuncProcTriggerPo> vals =  new ArrayList<>();   // getAllddlHelper(conn, schema, 1); //Dbinfo.fetchAllViewName(conn, schema);
			if (allDDLs != null && allDDLs.size() > 0) {
				allDDLs.stream().forEach(v -> {
					FuncProcTriggerPo po = new FuncProcTriggerPo();
					po.setName(v);
					po.setSchema(schema);
					vals.add(po);
					
					// 视图ddl
//					String ddl = exportCreateView(conn, schema, v.getTableName());
//					v.setDdl(ddl);
				});
			}
			 
			return vals;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<FuncProcTriggerPo> allSequenceObj(Connection conn, String schema) {
		try {
			String str = 
					"select  INDEX_NAME from information_schema.STATISTICS  \n" + 
					"where TABLE_SCHEMA = '"+schema+"' \n" + 
					"and NON_UNIQUE = 1 \n" ;
			// 获取名称
			List<String> allDDLs = getAllddlHelper(conn, str, 1);
			List<FuncProcTriggerPo> vals =  new ArrayList<>();   // getAllddlHelper(conn, schema, 1); //Dbinfo.fetchAllViewName(conn, schema);
			if (allDDLs != null && allDDLs.size() > 0) {
				allDDLs.stream().forEach(v -> {
					FuncProcTriggerPo po = new FuncProcTriggerPo();
					po.setName(v);
					po.setSchema(schema);
					vals.add(po);
					// 视图ddl
//					String ddl = exportCreateView(conn, schema, v.getTableName());
//					v.setDdl(ddl);
				});
			}
			 
			return vals;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public List<FuncProcTriggerPo> allPrimaryKeyObj(Connection conn, String schema) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<FuncProcTriggerPo> allForeignKeyObj(Connection conn, String schema) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String exportCallFuncSql(String funcStr) {
		String sql = "select "+funcStr+" from dual";
		return sql;
	}


	@Override
	public List<TableIndexPo> tableIndex(Connection conn, String schema, String tableName) {
		String sql = "SELECT DISTINCT INDEX_NAME, TABLE_NAME, INDEX_SCHEMA, GROUP_CONCAT(COLUMN_NAME)  as COLUMN_NAME \n"
				+ "FROM INFORMATION_SCHEMA.STATISTICS \n"
				+ "WHERE  any_value(NON_UNIQUE )= 1  and TABLE_SCHEMA = '" + schema + "' and TABLE_NAME = '" + tableName + "' \n"
			    + "GROUP BY TABLE_NAME, INDEX_NAME  , INDEX_SCHEMA";

		ResultSet rs = null;
		Statement sm = null;
		List<TableIndexPo> ls = new ArrayList<>();

		try {
			sm = conn.createStatement();
			rs = sm.executeQuery(sql);

			while (rs.next()) {
				TableIndexPo po = new TableIndexPo();
//			private String indname; // INDNAME 索引名称
//			private String tabname;  // TABNAME 表名
//			private String indschema; // INDSCHEMA 索引schema
//			private String colnames; // COLNAMES 索引的列
				po.setIndname(rs.getString("INDEX_NAME"));
				po.setTabname(rs.getString("TABLE_NAME"));
				po.setIndschema(rs.getString("INDEX_SCHEMA"));
				po.setColnames(rs.getString("COLUMN_NAME"));
				ls.add(po);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return ls; 
	}


	@Override
	public List<TableForeignKeyPo> tableForeignKey(Connection conn, String schema, String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

}
