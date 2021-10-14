package net.tenie.Sqlucky.sdk.component;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.fxmisc.richtext.CodeArea;
import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.tenie.Sqlucky.sdk.utility.StrUtils;
import net.tenie.Sqlucky.sdk.SqluckyTab;
import net.tenie.Sqlucky.sdk.component.ComponentGetter;
import net.tenie.Sqlucky.sdk.po.MyRange;
import net.tenie.Sqlucky.sdk.utility.CommonUtility;


/**
 * 
 * @author tenie
 *
 */
public class SqlcukyEditor {
	private static Logger logger = LogManager.getLogger(SqlcukyEditor.class);

	// 当前文本框中文本重新高亮
	public static void applyHighlighting() {
		currentSqlCodeAreaHighLighting();
	}
	
	// 获取当前选中的区间
	public static IndexRange getSelection() {
		var codeArea = SqlcukyEditor.getCodeArea();
		return codeArea.getSelection();
	}
	
	
	// 设置选中
	public static void selectRange( IndexRange  ir) {
		var codeArea = SqlcukyEditor.getCodeArea();
		codeArea.selectRange(ir.getStart(), ir.getEnd());
	}
	
	
	// 获取当前在前台的文本框
	public static CodeArea getCodeArea() {
		var mtb = currentMyTab();
		if(mtb == null ) return null;
		return mtb.getSqlCodeArea().getCodeArea();
	}

	// 当前文本框中, 取消选中的文本
	public static void deselect() {
		getCodeArea().deselect(); 
	}

   //  获取Tab中的的code area
//	public static CodeArea getCodeArea(SqluckyTab tb) {
//		var code = tb.getSqlCodeArea().getCodeArea();
//		return code;
//	}

    // 获取area 中的文本
	public static String getCurrentTabSQLText(CodeArea code) {
		String sqlText = code.getText();
		return sqlText;
	}


	// 	get sql text
	public static String getCurrentCodeAreaSQLText() {
		CodeArea code = getCodeArea();
		String sqlText = code.getText();
		return sqlText;
	}  

   // get select text
	public static String getCurrentCodeAreaSQLSelectedText() {
		CodeArea code = getCodeArea();
		return code.getSelectedText();
	}
	//当前行的 字符串文本;
	public static String getCurrentLineText() {
		CodeArea code = getCodeArea();
		 
		int idx = code.getCurrentParagraph();
		var  val = code.getParagraph(idx);
		List<String > ls = val.getSegments();
		System.out.println(ls);
		return  ls.get(0);
	}
	
	
	// 复制当前选中的文本
	public static void copySelectionText() {
		String txt = getCurrentCodeAreaSQLSelectedText();
		CommonUtility.setClipboardVal(txt);
	}
	
	public static void pasteTextToCodeArea(){
		String val = CommonUtility.getClipboardVal();
		if(StrUtils.isNotNullOrEmpty(val)) {
			var codeArea = SqlcukyEditor.getCodeArea();
			int i = codeArea.getAnchor();
			codeArea.insertText(i, val);
			SqlcukyEditor.currentMyTab().getSqlCodeArea().highLighting();
		}
	}
	
	// 删除选中文本
	public static void deleteSelectionText() { 
		var codeArea = SqlcukyEditor.getCodeArea();
		IndexRange ir = codeArea.getSelection();
		codeArea.deleteText(ir);
	}
	
	// 剪切选中文本
	public static void cutSelectionText() { 
		 copySelectionText();
		 deleteSelectionText(); 
	}
		

	// 代码的容器
//	public static StackPane getTabStackPane(SqluckyTab tb) {
//		VBox vb =   tb.getVbox();;
//		StackPane sp = null;
//		if (vb.getChildren().size() > 1) {
//			int sz = vb.getChildren().size() - 1;
//			sp = (StackPane) vb.getChildren().get(sz);
//			
//		} else {
//			sp = (StackPane) vb.getChildren().get(0);
//		}
//		return sp;
//	}

	// 获取当前选中的代码Tab
	public static Tab mainTabPaneSelectedTab() { 
		var myTabPane = ComponentGetter.mainTabPane;
//		SqluckyTab rstab = (SqluckyTab) myTabPane.getSelectionModel().getSelectedItem();
//		return rstab;
		return myTabPane.getSelectionModel().getSelectedItem();
	}
	
	public static SqluckyTab currentMyTab() { 
		var myTabPane = ComponentGetter.mainTabPane;
		Tab currentTab = myTabPane.getSelectionModel().getSelectedItem();
		SqluckyTab rs = (SqluckyTab) currentTab;
		return rs;
	}
	
	public static void currentSqlCodeAreaHighLighting() {
		SqluckyTab mtb = currentMyTab();
		var area = mtb.getSqlCodeArea();
		area.highLighting();
	} 
	
	public static void ErrorHighlighting( int begin , int length , String str) {
		SqluckyTab mtb = currentMyTab();
		var area = mtb.getSqlCodeArea();
		area.errorHighLighting(begin, length, str);
	}
	
	public static void currentSqlCodeAreaHighLighting(String str) {
		SqluckyTab mtb = currentMyTab();
		var area = mtb.getSqlCodeArea();
		area.highLighting(str);
	}
	
	
	// 获取tab的内容 VBox
	public static VBox getTabVbox(Tab tb) {
		return (VBox) tb.getContent();
	}

	public static VBox getTabVbox() {
		Tab tb = mainTabPaneSelectedTab();
		return (VBox) tb.getContent();
	}

	public static void closeEditor() {
		TabPane myTabPane = ComponentGetter.mainTabPane;
		if (myTabPane.getTabs().size() > 1) {
			myTabPane.getTabs().remove(myTabPane.getSelectionModel().getSelectedIndex());
		}
	}
	
	// 获取所有的CodeArea
	public static List<SqluckyTab> getAllgetMyTabs() {
		List<SqluckyTab> cas = new ArrayList<>();
		TabPane myTabPane = ComponentGetter.mainTabPane;
		if (myTabPane.getTabs().size() > 1) {
			ObservableList<Tab> tabs = myTabPane.getTabs();
			for(Tab tb : tabs) { 
				SqluckyTab mtb = (SqluckyTab) tb;
				cas.add(mtb);
			}
		}
		return cas;
	}
	
	// 改变样式
	public static void changeThemeAllCodeArea() { 
		TabPane myTabPane = ComponentGetter.mainTabPane;
		if (myTabPane != null && myTabPane.getTabs().size() > 0) {
			ObservableList<Tab> tabs = myTabPane.getTabs();
			for(Tab tb : tabs) { 
				SqluckyTab mtb = (SqluckyTab) tb;
				// 修改代码编辑区域的样式
				mtb.getSqlCodeArea().changeCodeAreaLineNoThemeHelper();
				// 修改查找替换的样式如果有的话
				changeFindReplacePaneBtnColor(tb);
			}
		}
	}
	
    // 修改查找替换的样式如果有的话
	private static void changeFindReplacePaneBtnColor(Tab tb) {
		VBox vbx = (VBox) tb.getContent();
		if(vbx.getChildren().size() > 1) {
			String color = CommonUtility.themeColor();
			for(int i = 0 ; i< vbx.getChildren().size() -1 ; i++) {
				Node nd  = vbx.getChildren().get(i);
				if( nd instanceof AnchorPane) {
					AnchorPane ap =  (AnchorPane) nd;
					var apchs = ap.getChildren();
					for(Node apnd : apchs ) {
						if( apnd instanceof JFXButton) {
							JFXButton btn = (JFXButton) apnd;
							if(btn.getGraphic() != null)
								btn.getGraphic().setStyle("-fx-background-color: " +  color + ";");
						}
					}
				}
				
			}
		}
	}
	
	/*
	 * 根据";" 分割字符串, 找到要执行的sql, 并排除sql字符串中含有;的情况 1. 先在原始文本中找到sql的字符串, 替换为空白字符串,
	 * 得到一个新文本 2. 在新文本中根据 ; 分割字符串, 得到每个分割出来的子串在文本中的区间 3. 根据区间, 在原始文本中 提炼出sql语句
	 */
	public static List<String> findSQLFromTxt(String text) {
		String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'";
		String patternString = "(?<STRING>" + STRING_PATTERN + ")";
		Pattern PATTERN = Pattern.compile(patternString);
		Matcher matcher = PATTERN.matcher(text);
		String txtTmp = "";
		int lastKwEnd = 0;
		// 把匹配到的sql的字符串替换为对应长度的空白字符串, 得到一个和原始文本一样长度的新字符串
		while (matcher.find()) {
//			 String styleClass = matcher.group("STRING") != null ? "string" : null;
			int start = matcher.start();
			int end = matcher.end();
			int len = end - start;
			String space = createSpaceStr(len);
			String tmp = text.substring(start, end);
//			 logger.info("len = "+len+" ; tmp = " + tmp); 
			txtTmp += text.substring(lastKwEnd, start) + space;
			lastKwEnd = end;
		}
		if (lastKwEnd > 0) {
			String txtEnd = text.substring(lastKwEnd, text.length());
			txtTmp += txtEnd;
		} else {
			txtTmp = text;
		}
//		logger.info("txtTmp = " + txtTmp);

		// TODO 在新字符上面, 提取字sql语句的区间
		String str = txtTmp;
		// 根据区间提炼出真正要执行的sql语句
		List<String> sqls = new ArrayList<>();
		if (str.contains(";")) {
			List<MyRange> idxs = new ArrayList<>();
			String[] all = str.split(";"); // 分割多个语句
			if (all != null && all.length > 0) {
				int ss = 0;
				for (int i = 0; i < all.length; i++) {
					String s = all[i];
					int end = ss + s.length();
					if (end > str.length()) {
						end--;
					}
					MyRange mr = new MyRange(ss, end);
					ss = end + 1;
					idxs.add(mr);
				}
			}
			for (MyRange mr : idxs) {
				int s = mr.getStart();
				int e = mr.getEnd();
				String tmps = text.substring(s, e);
				sqls.add(tmps);
			}
		} else {
			sqls.add(text);
		}

		return sqls;
	}
	
	private static String createSpaceStr(int len) {
		String space = "";
		for (int j = 0; j < len; j++) {
			space += " ";
		}
		return space;
	}
	
	
	// 将注释部分转换为空格字符,保持字符串的长度
	public static String trimCommentToSpace(String sql, String symbol) {
		if (!sql.contains(symbol))
			return sql;
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
				if (! StrUtils.beginWith(temp, symbol)) {
					nstr += temp + "\n";
				} else {
					// 生成空白行的字符串
					String space = createSpaceStr(temp.length());

					nstr = nstr.substring(0, nstr.length() - 1);
					nstr += space + "\n";
				}
			}
		}
		if ("".equals(nstr)) {
			nstr = sql;
		}
		return nstr;
	}

	  
}
