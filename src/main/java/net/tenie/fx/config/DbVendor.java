package net.tenie.fx.config;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/*   @author tenie */
/**
 * 数据库厂家 和对应的启动字符串
 */
public class DbVendor {
	
	public static String DB2 = "DB2";
	public static String MYSQL = "MYSQL";
	public static String H2 = "H2";
	public static String SQLITE = "SQLITE";
	
	
	private static LinkedHashSet<String> keys = new LinkedHashSet<String>();
	private static Map<String, String> data = new HashMap<String, String>();

	static {
		add( DB2, "com.ibm.db2.jcc.DB2Driver");
		add( MYSQL, "com.mysql.jdbc.Driver");
		add( H2, "org.h2.Driver");
		// 新版的jdbc 不需要手动注册驱动了
		add(SQLITE, "");
	}

	public static void add(String name, String val) {
		keys.add(name);
		data.put(name, val);
	}

	public static LinkedHashSet<String> getAll() {
		return keys;
	}

	public static String getDriver(String name) {
		return data.get(name);
	}

	public static void clear() {
		keys.clear();
		data.clear();
	}

}
