package net.tenie.fx.utility;

import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import net.tenie.fx.PropertyPo.CacheTableDate;
import net.tenie.fx.component.ComponentGetter;
import net.tenie.fx.dao.UpdateDao;
import net.tenie.lib.tools.StrUtils;

/*   @author tenie */
public class CommonUtility {
	private static Logger logger = LogManager.getLogger(CommonUtility.class);
	public static void runThread(Function<Object, Object> fun) {
		Thread t = new Thread() {
			public void run() {
				fun.apply(null);
			}
		};
		t.start();
	}

	// 获取Tab 中的文本
	public static String tabText(Tab tb) {
		String title = tb.getText();
		if (StrUtils.isNullOrEmpty(title)) {
			Label lb = (Label) tb.getGraphic();
			if (lb != null)
				title = lb.getText();
			else
				title = "";
		}
		return title;
	}

	public static void setTabName(Tab tb, String val) {
		Label lb = (Label) tb.getGraphic();
		if (lb != null) {
			lb.setText(val);
			tb.setText("");
		} else {
			tb.setText(val);
		}
	}

	// 判断数据库字段是否是数字类型
	public static boolean isNum(int type) {
		if (type == java.sql.Types.BIGINT 
				|| type == java.sql.Types.BIT 
				|| type == java.sql.Types.DECIMAL
				|| type == java.sql.Types.DOUBLE 
				|| type == java.sql.Types.FLOAT
				|| type == java.sql.Types.NUMERIC
				|| type == java.sql.Types.REAL 
				|| type == java.sql.Types.TINYINT
				|| type == java.sql.Types.SMALLINT
				|| type == java.sql.Types.INTEGER) {
			return true;
		}
		return false;
	}

	// 时间类型判断
	public static boolean isDateTime(int type) {
		if (type == java.sql.Types.DATE || type == java.sql.Types.TIME || type == java.sql.Types.TIMESTAMP) {
			return true;
		}
		return false;
	}

	// 字符串类型判断
	public static boolean isString(int type) {
		if (type == java.sql.Types.CHAR || type == java.sql.Types.VARCHAR || type == java.sql.Types.LONGVARCHAR) {
			return true;
		}
		return false;
	}

	 

	public static void newStringPropertyChangeListener(StringProperty val, int dbtype) {
		ChangeListener<String> cl = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (CommonUtility.isNum(dbtype) 
				&& !StrUtils.isNumeric(newValue) 
				&& !"<null>".equals(newValue)) {
					logger.info("newStringPropertyChangeListener() : newValue= "+newValue+"set fail" );
					Platform.runLater(() -> val.setValue(oldValue));
					return;
				}
			}
		};
		val.addListener(cl);
	}

	public static void prohibitChangeListener(StringProperty val, String original) {
		ChangeListener<String> cl = new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (original == null) {
					if (newValue != null) {
						Platform.runLater(() -> val.setValue(original));
					}
				} else {
					if (newValue != null && !newValue.equals(original)) {
						Platform.runLater(() -> val.setValue(original));
					}
				}

			}
		};

	}

	// 剪贴板 赋值
	public static void setClipboardVal(String val) {
		Platform.runLater(() -> {
			javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
			javafx.scene.input.ClipboardContent clipboardContent = new javafx.scene.input.ClipboardContent();
			clipboardContent.putString(val);
			clipboard.setContent(clipboardContent);
		});

	}

}
