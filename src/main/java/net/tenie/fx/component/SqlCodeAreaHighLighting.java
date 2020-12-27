package net.tenie.fx.component;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.IndexRange;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import net.tenie.fx.Action.CommonAction;
import net.tenie.fx.config.ConfigVal;
import net.tenie.fx.utility.EventAndListener.CommonEventHandler;
import net.tenie.lib.tools.StrUtils;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

/*   @author tenie */
public class SqlCodeAreaHighLighting {
  
	private static final String sampleCode = String.join("\n", new String[] { "" });

	private CodeArea codeArea;
	private ExecutorService executor;

	public StackPane getObj(String text, boolean editable) {
		executor = Executors.newSingleThreadExecutor();
		codeArea = new CodeArea();
		if(ConfigVal.THEME.equals("DARK")) {
			codeArea.setParagraphGraphicFactory(MyLineNumberFactory.get(codeArea));
		}else {
			codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
		} 
		// 事件KeyEvent
		
		codeArea.addEventFilter(KeyEvent.KEY_PRESSED , e->{ 
			if(e.getCode() == KeyCode.TAB ) {
				System.out.println("e.getCode() "+e.getCode() );
				if (codeArea.getSelectedText().contains("\n") ) { 
					e.consume();
					if(e.isShiftDown()) {
						CommonAction.minus4Space();
					}else { 
						CommonAction.add4Space();
					}
				}
				
			}
		});
 
		codeArea.setOnKeyPressed(CommonEventHandler.codeAreaChange(codeArea)); 
		codeArea.replaceText(0, 0, sampleCode);
		if (text != null)
			codeArea.appendText(text);
		StackPane sp = new StackPane(new VirtualizedScrollPane<>(codeArea));
		sp.getStyleClass().add("my-tag");
		SqlCodeAreaHighLightingHelper.applyHighlighting(codeArea);
		codeArea.setEditable(editable);
		 
	    // 中午输入法显示问题
		codeArea.setInputMethodRequests(new InputMethodRequestsObject(codeArea));
		codeArea.setOnInputMethodTextChanged(e ->{		
			 if (e.getCommitted() != "") {
				 codeArea.insertText(codeArea.getCaretPosition(), e.getCommitted());
		        }
		});
		
		// 当表被拖拽进入到code editor , 将表名插入到 光标处
		codeArea.setOnDragEntered(e->{
			String val = ComponentGetter.dragTreeItemName;
			if(StrUtils.isNotNullOrEmpty(val)) {
				IndexRange i = codeArea.getSelection(); // 获取当前选中的区间
				int start = i.getStart();
				codeArea.insertText(start, val);
			}
			
		});
		 
		// 选中事件
		codeArea.selectedTextProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	if(newValue.length() > 0) {
		    		SqlCodeAreaHighLightingHelper.applyFindWordHighlighting(codeArea, newValue); 
		    	}else {
		    		SqlCodeAreaHighLightingHelper.applyHighlighting(codeArea);
		    	}
		    	
		    
		    }
		});
		
		return sp;
	}

	public StackPane getObj() {
		return getObj(null, true);
	}

	public void stop() {
		executor.shutdown();
	}

	 

	 
 
}
class InputMethodRequestsObject implements InputMethodRequests {
    private CodeArea area;
	public InputMethodRequestsObject(CodeArea area) {
		this.area = area;
	}
	@Override
	public
    String getSelectedText() {
        return "";
    }
    @Override
	public
    int getLocationOffset(int x, int y) {
        return 0;
    }
    @Override
	public
    void cancelLatestCommittedText() {

    }
    @Override
    public Point2D getTextLocation(int offset) {
        // a very rough example, only tested under macOS
        Optional<Bounds> caretPositionBounds = area.getCaretBounds();
        if (caretPositionBounds.isPresent()) {
            Bounds bounds = caretPositionBounds.get();
            return new Point2D(bounds.getMaxX() - 5, bounds.getMaxY());
        } 
        throw new NullPointerException();
    }
    
}