package net.tenie.fx.dao;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import net.tenie.fx.PropertyPo.SqlFieldPo;
import net.tenie.fx.utility.CommonUtility;
import net.tenie.lib.tools.StrUtils;

/*   @author tenie */
public class GenerateSQLString {
	public static String insertSQL(String tableName, ObservableList<StringProperty> data,
			ObservableList<SqlFieldPo> fpos) {

		StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
		StringBuilder values = new StringBuilder("");
		int size = fpos.size();
		for (int i = 0; i < size; i++) {
			SqlFieldPo po = fpos.get(i);
			int type = po.getColumnType().get();
			String temp = data.get(i).get();
			sql.append(po.getColumnLabel().get());
			if (StrUtils.isNullOrEmpty(temp) || "<null>".equals(temp)) {
				values.append("null");
			} else if (CommonUtility.isString(type) || CommonUtility.isDateTime(type)) {
				values.append("'" + temp + "'");
			} else {
				values.append(temp);
			}
			sql.append(" ,");
			values.append(" ,");
		}
		String insert = sql.toString();
		String valstr = values.toString();
		if (insert.endsWith(",")) {
			insert = insert.substring(0, insert.length() - 1);
			valstr = valstr.substring(0, values.length() - 1);
		}

		insert += " ) VALUES (" + valstr + ");\n";

		return insert;
	}

	public static String insertSQLHelper(ObservableList<ObservableList<StringProperty>> vals, String tableName,
			ObservableList<SqlFieldPo> fs) {

		if (vals != null && vals.size() > 0) {
			StringBuilder strb = new StringBuilder();
			for (int i = 0; i < vals.size(); i++) {
				ObservableList<StringProperty> vl = vals.get(i);
				String rs = GenerateSQLString.insertSQL(tableName, vl, fs);
				strb.append(rs);
			}
			String str = strb.toString();
			return str;
		}
		return "";
	}

	public static String csvStr(String tableName, ObservableList<StringProperty> data,
			ObservableList<SqlFieldPo> fpos) {

		StringBuilder values = new StringBuilder("");
		int size = fpos.size();
		for (int i = 0; i < size; i++) {
			SqlFieldPo po = fpos.get(i);
			int type = po.getColumnType().get();
			String temp = data.get(i).get();
			if (StrUtils.isNullOrEmpty(temp) || "<null>".equals(temp)) {
				values.append("null");
			} else if (CommonUtility.isString(type) || CommonUtility.isDateTime(type)) {
				values.append("'" + temp + "'");
			} else {
				values.append(temp);
			}
			values.append(" ,");
		}
		String valstr = values.toString();
		if (valstr.endsWith(",")) {
			valstr = valstr.substring(0, values.length() - 1);
		}

		valstr += "\n";

		return valstr;
	}

	public static String csvStrHelper(ObservableList<ObservableList<StringProperty>> vals, String tableName,
			ObservableList<SqlFieldPo> fs) {

		if (vals != null && vals.size() > 0) {
			StringBuilder strb = new StringBuilder();
			for (int i = 0; i < vals.size(); i++) {
				ObservableList<StringProperty> vl = vals.get(i);
				String rs = GenerateSQLString.csvStr(tableName, vl, fs);
				strb.append(rs);
			}
			String str = strb.toString();
			return str;
		}
		return "";
	}

	public static String txtStr(String tableName, ObservableList<StringProperty> data,
			ObservableList<SqlFieldPo> fpos) {

		StringBuilder values = new StringBuilder("");
		int size = fpos.size();
		for (int i = 0; i < size; i++) {
			SqlFieldPo po = fpos.get(i);
			int type = po.getColumnType().get();
			String temp = data.get(i).get();
			if (StrUtils.isNullOrEmpty(temp) || "<null>".equals(temp)) {
				values.append("null");
			} else if (CommonUtility.isString(type) || CommonUtility.isDateTime(type)) {
				values.append("'" + temp + "'");
			} else {
				values.append(temp);
			}
			values.append("  ");
		}
		String valstr = values.toString();
		if (valstr.endsWith(",")) {
			valstr = valstr.substring(0, values.length() - 1);
		}

		valstr += "\n";

		return valstr;
	}

	public static String txtStrHelper(ObservableList<ObservableList<StringProperty>> vals, String tableName,
			ObservableList<SqlFieldPo> fs) {

		if (vals != null && vals.size() > 0) {
			StringBuilder strb = new StringBuilder();
			for (int i = 0; i < vals.size(); i++) {
				ObservableList<StringProperty> vl = vals.get(i);
				String rs = GenerateSQLString.txtStr(tableName, vl, fs);
				strb.append(rs);
			}
			String str = strb.toString();
			return str;
		}
		return "";
	}

}
