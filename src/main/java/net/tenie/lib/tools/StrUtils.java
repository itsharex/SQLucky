package net.tenie.lib.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.tenie.fx.PropertyPo.MyRange;
import net.tenie.fx.config.ConfigVal;

/*   @author tenie */
public class StrUtils {
	public final static SimpleDateFormat sdf_s = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat sdf_l = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static String EMPTY_STRING = "";
	public final static String BLANK_SPRING_STRING = " ";
	public static final char CHAR_TILDE = '~';

	// 驼峰命名转下划线
	public static String CamelCaseUnderline(String str) {
		StringBuilder rs = new StringBuilder();

		rs.append(str.charAt(0));
		for (int i = 1; i < str.length(); i++) {
			char strChar = str.charAt(i);
			if (strChar >= 'A' && strChar <= 'Z') {
				rs.append("_");
				rs.append(strChar);
			} else {
				rs.append(strChar);
			}
		}
		return rs.toString();
	}

public static void main(String[] args) {
	String str = ";123;567;9";
	
	List<MyRange> idxs = new ArrayList<>();
	if (str.contains(";")) {
		String[] all = str.split(";"); // 分割多个语句
		if (all != null && all.length > 0) {
			int ss = 0;
			for(int i = 0; i < all.length ; i++) {
				String s = all[i]; 
			    int end  = ss + s.length()  ;
			    if( end > str.length()) {
			    	end--;
			    }  
			    MyRange mr = new MyRange(ss, end);
			    ss = end + 1;
			    idxs.add(mr); 
			} 
		}
	}
	System.out.println(idxs);
	for(MyRange mr: idxs) {
		int s = mr.getStart();
		int e = mr.getEnd();
		String tmps = str.substring(s, e);
		System.out.println(tmps);
	}
//	
//	
//	Date d  = datePlus1Second("2021-01-07 11:47:17" );
////								2021-01-07 11:47:18
//	 System.out.println( dateToStrL(d)); ;
//	String str = 
//			  "balancePartAmount\n"
//			+ "1111--ssss\n"
//			+ "2222"  ;
//	System.out.println(str.length());
//	System.out.println(str);
//	System.out.println("======");
//	String s = trimComment(str, "--");
//	System.out.println(s.length());
//	System.out.println(s);
//	System.out.println("======");
//    s = trimCommentToSpace(str, "--");
//	System.out.println(s.length());
//	System.out.println(s);
}
	// 下划线 轉 驼峰命名
	public static String underlineCaseCamel(String str) {
		StringBuilder rs = new StringBuilder();
		str = str.toLowerCase();
		boolean tf = false;
		for (int i = 0; i < str.length(); i++) {
			char strChar = str.charAt(i);
			if (strChar == '_') {
				tf = true;
			} else {
				if (tf) {
					if (strChar >= 'A' && strChar <= 'Z') {
						strChar += 26;
					} else if (strChar >= 'a' && strChar <= 'z') {
						strChar -= 32;
					}
				}
				rs.append(strChar);
				tf = false;
			}
		}
		return rs.toString();
	}

	// 去除字符串中的非数字部分
	public static String clearString(String str) {
		StringBuffer val = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c >= '0' && c <= '9') {
				val.append(c);
			}
		}
		return val.toString();
	}

	// 去除字符串中的非数字部分
	public static boolean isNumeric(String str) {
		return !str.isEmpty() && str.matches("-?\\d+(\\.\\d+)?");
	}

	// 去除字符串中的非数字部分, 包含.
	public static String clearIpStr(String str) {
		StringBuffer val = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if ((c >= '0' && c <= '9') || c == '.') {
				val.append(c);
			}
		}
		return val.toString();
	}

	public static UUID getRandomUUID() {
		return UUID.randomUUID();
	}

	public static boolean beginWith(String str, String tag) {
		boolean tf = false;
		if (str != null && tag != null && str.length() >= tag.length()) {
			if (str.subSequence(0, tag.length()).equals(tag)) {
				tf = true;
			}
		}
		return tf;
	}

	public static boolean beginWithNotSensitive(String str, String tag) {
		boolean tf = false;
		if (str != null && tag != null && str.length() > tag.length()) {
			if (str.subSequence(0, tag.length()).toString().toUpperCase().equals(tag.toUpperCase())) {
				tf = true;
			}
		}
		return tf;
	}

	// 去除多余空白字符， 转化为一个“ ”
	public static String clearBlank(String str) {
		String temp = str.replaceAll("\r", "");
		temp = temp.replaceAll("\n", " ");
		temp = temp.replaceAll("\t", " ");
		temp = temp.trim();
		String rs = "";
		if (temp.indexOf(' ') > 0) {
			String[] arr = temp.split(" ");
			for (int i = 0; i < arr.length; i++) {
				String val = arr[i].trim();
				if (val.length() > 0) {
					rs += val + " ";
				}
			}
		}

		return rs;
	}

	// 去除注释
	public static String trimComment(String sql, String symbol) {
		String str = sql.replaceAll(symbol, "\n" + symbol);
		if (str.contains("\r")) {
			str = str.replace("\r", "");
		}

		String[] sa = str.split("\n");
		String nstr = "";
		if (sa != null && sa.length > 1) {
			for (int i = 0; i < sa.length; i++) {
				String temp = sa[i];
				if (!beginWith(temp, symbol)) {
					nstr += temp + "\n";
				}
			}
		}
		if ("".equals(nstr)) {
			nstr = sql;
		}
		return nstr.trim();
	}

	private static String createSpaceStr(int len) {
		String space = ""; 
		for(int j = 0 ; j < len; j++){
			space += " ";
		}
		return space;
	}
	
	// 将注释部分转换为空格字符,保持字符串的长度
	public static String trimCommentToSpace(String sql, String symbol) {
			if(! sql.contains(symbol)) return sql;
			// 在symbol前插入换行符, 之后就是对行的处理
			String str = sql.replaceAll(symbol, "\n" + symbol);
			if (str.contains("\r")) {
				str = str.replace("\r", "");
			}

			String[] sa = str.split("\n");
			String nstr = "";
			if (sa != null && sa.length > 1) {
				// 遍历行
				for (int i = 0; i < sa.length; i++) {
					String temp = sa[i];
					// 如果不是以symbol开头的字符串就保持到nstr字符串
					if (!beginWith(temp, symbol)) {
						nstr += temp + "\n";
					}else {
						// 生成空白行的字符串
//						String space = ""; 
//						for(int j = 0 ; j < temp.length(); j++){
//							space += " ";
//						}
						String space = createSpaceStr( temp.length());
						
						nstr = nstr.substring(0, nstr.length()-1);
						nstr +=  space + "\n";
					}
				}
			}
			if ("".equals(nstr)) {
				nstr = sql;
			}
//			return nstr.trim();
			return nstr;
		}
	/*
	 *  根据";" 分割字符串, 找到要执行的sql, 并排除sql字符串中含有;的情况
	 *  1. 先在原始文本中找到sql的字符串, 替换为空白字符串, 得到一个新文本 
	 *  2. 在新文本中根据 ; 分割字符串, 得到每个分割出来的子串在文本中的区间
	 *  3. 根据区间, 在原始文本中 提炼出sql语句
	 */
	public static List<String> findSQLFromTxt(String text) {
		String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'";
		String patternString =   "(?<STRING>" + STRING_PATTERN + ")";
		Pattern PATTERN = Pattern.compile(patternString  );
		Matcher matcher = PATTERN.matcher(text);
		String txtTmp = "";
		int lastKwEnd = 0;
		// 把匹配到的sql的字符串替换为对应长度的空白字符串, 得到一个和原始文本一样长度的新字符串
		while(matcher.find()) {
//			 String styleClass = matcher.group("STRING") != null ? "string" : null;
			 int start =  matcher.start();
			 int end =  matcher.end();
			 int len = end - start;
			 String space = createSpaceStr( len);
			 String tmp = text.substring(start, end);
//			 System.out.println("len = "+len+" ; tmp = " + tmp); 
			 txtTmp += text.substring(lastKwEnd, start) + space ; 
			 lastKwEnd = end;
		}
		if(lastKwEnd > 0 ) {
			String txtEnd = text.substring(lastKwEnd ,text.length());
			 txtTmp +=  txtEnd;
		
		
		
		} 
//		System.out.println("txtTmp = " + txtTmp);
		
		
		//TODO 在新字符上面, 提取字sql语句的区间
		String str = txtTmp;
		List<MyRange> idxs = new ArrayList<>(); 
		if (str.contains(";")) {
			String[] all = str.split(";"); // 分割多个语句
			if (all != null && all.length > 0) {
				int ss = 0;
				for(int i = 0; i < all.length ; i++) {
					String s = all[i]; 
				    int end  = ss + s.length()  ;
				    if( end > str.length()) {
				    	end--;
				    }  
				    MyRange mr = new MyRange(ss, end);
				    ss = end + 1;
				    idxs.add(mr); 
				} 
			}
		}
		// 根据区间提炼出真正要执行的sql语句
		List<String> sqls = new ArrayList<>();
		for(MyRange mr: idxs) {
			int s = mr.getStart();
			int e = mr.getEnd();
			String tmps = text.substring(s, e);
			sqls.add(tmps);
//			System.out.println(tmps);
		}
//		System.out.println(sqls);
		return sqls;
	}
	
	// 根据; 分割字符串, 需要忽略在注释下的分号
	public static List<String> splitSqlStr(String sql) {
		List<String> rs = new ArrayList<>();
		String[] sa = sql.split("\n");
		String nSql = "";
		if(sa !=null && sa.length > 0) {
			for(int i = 0; i< sa.length; i++) {
				String sub = sa[i];
				if(!sub.contains(";")) {  //没有分隔符, 拼接字符串
					nSql += sub +  "\n";
				}else{					  //有分隔符: 1. 判断有没有注释,
					if(sub.contains("--")) {
						int local = sub.indexOf("--");
						String subTmp1 = sub.substring(0, local);
						String subTmp2 = sub.substring(local);
						if(subTmp1.contains(";")) {
//							 sub
						}
						
					}else{
						nSql += sub +  "\n";
						rs.add(nSql);
						nSql = "";
					}
				}
				
			}
			if(nSql.length()>0) {
				rs.add(nSql);
			}
		}
		return rs;
	}
	
	/**
	 * check if null or empty string
	 */
	public static boolean isNullOrEmpty(Object obj) {
		return (null == obj || EMPTY_STRING.equals(obj));
	}

	public static boolean isNotNullOrEmpty(Object obj) {
		return (!isNullOrEmpty(obj));
	}

	/**
	 * 检查是否相等
	 */
	public static boolean isEquals(Object obj, String expectValue) {
		if (isNullOrEmpty(obj) || isNullOrEmpty(expectValue)) {
			return false;
		}
		return expectValue.equals(obj.toString());
	}

	/**
	 * 检查是否相等（忽略大小写）
	 */
	public static boolean isEqualsNoCasetive(Object obj, String expectValue) {
		if (isNullOrEmpty(obj) || isNullOrEmpty(expectValue)) {
			return false;
		}
		return expectValue.toUpperCase().equals(obj.toString().toUpperCase());
	}

	/**
	 * 判断是否是符合的正则表达示
	 * 
	 */
	public static boolean isMatcherPatten(String pattenStr, String extensions) {
		Pattern patten = Pattern.compile(pattenStr);
		Matcher matcher = patten.matcher(extensions);
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}

	public static String dateToStrL(Date d) {
		return sdf_l.format(d);
	}

	public static String dateToStr(Date d, String formate) {
		SimpleDateFormat sdf = new SimpleDateFormat(formate);
		return sdf.format(d);
	}

	public static Date StrToDate(String str, String formate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(formate);
			return new Date(sdf.parse(str).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public static Date datePlus1Second(String str) {
		try {
			Date d = StrUtils.StrToDate(str, ConfigVal.dateFormateL);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			c.add(Calendar.SECOND, 1);
			
			return c.getTime();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	

	// 字符串转时间
	public static Date StrToDate_L(String str) {
		try {
			return new Date(sdf_l.parse(str).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 字符串转时间
	public static java.sql.Date StrToDate_S(String str) {

		try {
			return new java.sql.Date(sdf_s.parse(str).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

	}

	public static String StrPlitJoin(String str, String regex, String delimiter) {
		String rs = "";
		String[] temp = str.split(regex);
		if (temp != null && temp.length > 0) {
			int len = temp.length;
			for (int i = 0; i < len; i++) {
				String s = temp[i];
				if (s.length() > 0 && s.trim().length() > 0) {
					rs += s + delimiter;
				}
			}
		}
		if (rs.length() > 0)
			rs = rs.substring(0, rs.length() - 1);
		return rs;
	}
}
