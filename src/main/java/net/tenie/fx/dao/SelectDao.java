package net.tenie.fx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import org.controlsfx.control.tableview2.FilteredTableView;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.tenie.fx.PropertyPo.CacheTableDate;
import net.tenie.fx.PropertyPo.DbTableDatePo;
import net.tenie.fx.PropertyPo.SqlFieldPo;
import net.tenie.fx.component.ComponentGetter;
import net.tenie.fx.config.ConfigVal;
import net.tenie.fx.utility.CommonUtility;
import net.tenie.lib.tools.StrUtils;

/*   @author tenie */
public class SelectDao {
	// 获取查询的结果, 返回字段名称的数据和 值的数据
	public static DbTableDatePo selectSql(Connection conn, String sql, int limit,
			FilteredTableView<ObservableList<StringProperty>> table) throws SQLException {
		DbTableDatePo dpo = new DbTableDatePo();
		// DB对象
		PreparedStatement pstate = null;
		ResultSet rs = null;
		try {
			pstate = conn.prepareStatement(sql);
			// 处理结果集
			rs = pstate.executeQuery();
			// 获取元数据
			ResultSetMetaData mdata = rs.getMetaData();
			// 获取元数据列数
			Integer columnnums = Integer.valueOf(mdata.getColumnCount());
			// 迭代元数据
			for (int i = 1; i <= columnnums; i++) {
				SqlFieldPo po = new SqlFieldPo();
				po.setScale(mdata.getScale(i));
				po.setColumnName(mdata.getColumnName(i));
				po.setColumnClassName(mdata.getColumnClassName(i));
				po.setColumnDisplaySize(mdata.getColumnDisplaySize(i));
				po.setColumnLabel(mdata.getColumnLabel(i));
				po.setColumnType(mdata.getColumnType(i));
				po.setColumnTypeName(mdata.getColumnTypeName(i));
				dpo.addField(po);
			}
			// 数据
			if (limit > 0) {
				execRs(limit, rs, dpo, table);
			} else {
				execRs(rs, dpo, table);
			}

		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
		}
		return dpo;
	}

	private static void execRs(int limit, ResultSet rs, DbTableDatePo dpo,
			FilteredTableView<ObservableList<StringProperty>> table) throws SQLException {
		int idx = 1;
		int rowNo = 0;
		ObservableList<SqlFieldPo> fpo = dpo.getFields();
		int columnnums = fpo.size();
		while (rs.next()) {
			ObservableList<StringProperty> vals = FXCollections.observableArrayList();
			int rn = rowNo++;
			for (int i = 0; i < columnnums; i++) {
//				String field = fpo.get(i).getColumnLabel().get();
				int dbtype = fpo.get(i).getColumnType().get();
				StringProperty val;
				
				Object obj = rs.getObject(i + 1);
				if(obj == null) {
					val = new SimpleStringProperty("<null>");
				}else {
					if (CommonUtility.isDateTime(dbtype)) {
						java.sql.Timestamp ts = rs.getTimestamp(i + 1);
						Date d = new Date(ts.getTime());
						String v = StrUtils.dateToStr(d, ConfigVal.dateFormateL);
						val = new SimpleStringProperty(v);
					} else {
						String temp = rs.getString(i+1);
						val = new SimpleStringProperty(temp); 
					}
				}
				
				
				addStringPropertyChangeListener(val, rn, table.getId(), i, vals, dbtype);
				vals.add(val);
			}

			vals.add(new SimpleStringProperty(rn + ""));
			dpo.addData(vals);

			if (idx == limit)
				break;
			idx++;
		}
	}

	private static void execRs(ResultSet rs, DbTableDatePo dpo, FilteredTableView<ObservableList<StringProperty>> table)
			throws SQLException {
		execRs(Integer.MAX_VALUE, rs, dpo, table);
	}

	
    // 数据单元格添加监听
	// 字段修改事件
	public static void addStringPropertyChangeListener(StringProperty val, int rowNo, String tabId, int idx,
			ObservableList<StringProperty> vals, int dbtype) {
		ChangeListener<String> cl = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				System.out.println("addStringPropertyChangeListener ：newValue：" + newValue + " | oldValue =" + oldValue);
				System.out.println("key ==" + tabId + "-" + rowNo);
				System.out.println("observable = " + observable);
				
				if (CommonUtility.isNum(dbtype) && !StrUtils.isNumeric(newValue) && !"<null>".equals(newValue)) {
					Platform.runLater(() -> val.setValue(oldValue));
					return;
				}
				
				if(CommonUtility.isDateTime(dbtype) && "".equals(newValue )) {
					Platform.runLater(() -> val.setValue("<null>"));
				}
				
				ComponentGetter.dataFlowSaveBtn().setDisable(false);

				ObservableList<StringProperty> oldDate = FXCollections.observableArrayList();
				if (!CacheTableDate.exist(tabId, rowNo)) {
					for (int i = 0; i < vals.size(); i++) {
						if (i == idx) {
							oldDate.add(new SimpleStringProperty(oldValue));
						} else {
							oldDate.add(new SimpleStringProperty(vals.get(i).get()));
						}
					}
					CacheTableDate.addData(tabId, rowNo, vals, oldDate); // 数据修改缓存, 用于之后更新
				} else {
					CacheTableDate.addData(tabId, rowNo, vals);
				}
			}
		};
		val.addListener(cl);
	}
}
