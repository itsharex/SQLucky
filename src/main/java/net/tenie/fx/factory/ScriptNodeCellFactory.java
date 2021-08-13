package net.tenie.fx.factory;


import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import net.tenie.fx.PropertyPo.ScriptPo;

/**
 * 
 * @author tenie
 *
 */
public class ScriptNodeCellFactory implements Callback<TreeView<ScriptPo>, TreeCell<ScriptPo>> {
//	private static Logger logger = LogManager.getLogger(ScriptNodeCellFactory.class);
 

	@Override
	public TreeCell<ScriptPo> call(TreeView<ScriptPo> treeView) {
		TreeCell<ScriptPo> cell = new TreeCell<ScriptPo>() {
//			private TextField textField;

//			@Override
//			public void startEdit() {
//				super.startEdit();
//
////				if (textField == null) {
////					createTextField();
////				}
//				setText(null);
//				setGraphic(textField);
//				textField.selectAll();
//			}

//			@Override
//			public void cancelEdit() {
//				super.cancelEdit();
//				setText(getItem().getTitle());
////				setGraphic(getItem().getIcon());
//			}

			@Override
			public void updateItem(ScriptPo item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
//					setGraphic(item.getIcon());
					setText(item.getTitle());

				}
			}

//			private void createTextField() {
//				textField = new TextField(getString());
//				textField.setOnKeyReleased((KeyEvent t) -> {
//					if (t.getCode() == KeyCode.ENTER) {
//						ScriptPo item = getItem();
//						item.setTitle(textField.getText());
//						commitEdit(item);
//					} else if (t.getCode() == KeyCode.ESCAPE) {
//						cancelEdit();
//					}
//				});
//			}

			private String getString() {
				return getItem() == null ? "" : getItem().getTitle();
			}

		};
 
		return cell;
	}
	    
}
