package net.tenie.fx.component.container;

import java.util.Objects;

import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import net.tenie.fx.PropertyPo.TreeNodePo;

/*   @author tenie */
public class TaskCellFactory implements Callback<TreeView<TreeNodePo>, TreeCell<TreeNodePo>> {
	private static final DataFormat JAVA_FORMAT = new DataFormat("application/x-java-serialized-object");
	private static final String DROP_HINT_STYLE = "-fx-border-color: #eea82f; -fx-border-width: 0 0 2 0; -fx-padding: 3 3 1 3";
	private TreeCell<TreeNodePo> dropZone;
	private TreeItem<TreeNodePo> draggedItem;

	@Override
	public TreeCell<TreeNodePo> call(TreeView<TreeNodePo> treeView) {
		TreeCell<TreeNodePo> cell = new TreeCell<TreeNodePo>() {
			private TextField textField;

			@Override
			public void startEdit() {
				super.startEdit();

				if (textField == null) {
					createTextField();
				}
				setText(null);
				setGraphic(textField);
				textField.selectAll();
			}

			@Override
			public void cancelEdit() {
				super.cancelEdit();
				setText(getItem().getName());
				setGraphic(getItem().getIcon());
			}

			@Override
			public void updateItem(TreeNodePo item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					setGraphic(item.getIcon());
					setText(item.getName());

				}
			}

			private void createTextField() {
				textField = new TextField(getString());
				textField.setOnKeyReleased((KeyEvent t) -> {
					if (t.getCode() == KeyCode.ENTER) {
						TreeNodePo item = getItem();
						item.setName(textField.getText());
						commitEdit(item);
					} else if (t.getCode() == KeyCode.ESCAPE) {
						cancelEdit();
					}
				});
			}

			private String getString() {
				return getItem() == null ? "" : getItem().getName();
			}

		};

		cell.setOnDragDetected((MouseEvent event) -> dragDetected(event, cell, treeView));
		cell.setOnDragOver((DragEvent event) -> dragOver(event, cell, treeView));
		cell.setOnDragDropped((DragEvent event) -> drop(event, cell, treeView));
		cell.setOnDragDone((DragEvent event) -> clearDropLocation());

		return cell;
	}

	// 发现拖动
	private void dragDetected(MouseEvent event, TreeCell<TreeNodePo> treeCell, TreeView<TreeNodePo> treeView) {
		draggedItem = treeCell.getTreeItem();

		// root can't be dragged
		if (draggedItem.getParent() == null)
			return;
		Dragboard db = treeCell.startDragAndDrop(TransferMode.MOVE);

		ClipboardContent content = new ClipboardContent();
		content.put(JAVA_FORMAT, draggedItem.getValue().getName());
		db.setContent(content);
		db.setDragView(treeCell.snapshot(null, null));
		event.consume();
	}

	private void dragOver(DragEvent event, TreeCell<TreeNodePo> treeCell, TreeView<TreeNodePo> treeView) {
		if (!event.getDragboard().hasContent(JAVA_FORMAT))
			return;
		TreeItem<TreeNodePo> thisItem = treeCell.getTreeItem();

		// can't drop on itself
		if (draggedItem == null || thisItem == null || thisItem == draggedItem)
			return;
		// ignore if this is the root
		if (draggedItem.getParent() == null) {
			clearDropLocation();
			return;
		}

		event.acceptTransferModes(TransferMode.MOVE);
		if (!Objects.equals(dropZone, treeCell)) {
			clearDropLocation();
			this.dropZone = treeCell;
			dropZone.setStyle(DROP_HINT_STYLE);
		}
	}

	private void drop(DragEvent event, TreeCell<TreeNodePo> treeCell, TreeView<TreeNodePo> treeView) {
		Dragboard db = event.getDragboard();
		boolean success = false;
		if (!db.hasContent(JAVA_FORMAT))
			return;

		TreeItem<TreeNodePo> thisItem = treeCell.getTreeItem();
		TreeItem<TreeNodePo> droppedItemParent = draggedItem.getParent();

		// 只能同一个父节点下换位置, 否则不动
		if (Objects.equals(droppedItemParent, thisItem.getParent())) {
			droppedItemParent.getChildren().remove(draggedItem);
			int indexInParent = thisItem.getParent().getChildren().indexOf(thisItem);
			thisItem.getParent().getChildren().add(indexInParent + 1, draggedItem);

		}
		if (Objects.equals(droppedItemParent, thisItem)) {
			droppedItemParent.getChildren().remove(draggedItem);
			droppedItemParent.getChildren().add(0, draggedItem);

		}

		treeView.getSelectionModel().select(draggedItem);
		event.setDropCompleted(success);
	}

	private void clearDropLocation() {
		if (dropZone != null)
			dropZone.setStyle("");
	}
}
