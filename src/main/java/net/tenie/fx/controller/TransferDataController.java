package net.tenie.fx.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.controlsfx.control.CheckTreeView;

import com.jfoenix.controls.JFXComboBox;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import net.tenie.fx.PropertyPo.TreeNodePo;
import net.tenie.fx.component.ComponentGetter;
import net.tenie.fx.component.ImageViewGenerator;
import net.tenie.fx.component.ModalDialog;
import net.tenie.fx.config.DBConns;
import net.tenie.fx.config.MainTabs;
import net.tenie.fx.dao.TransferTabeDataDao;
import net.tenie.lib.db.DBTools;
import net.tenie.lib.db.ExportDDL;
import net.tenie.lib.po.DbConnectionPo;
import net.tenie.lib.tools.StrUtils;

public class TransferDataController implements Initializable {
	
	private static final String TABLE = "Table"; 
	private static final String VIEW = "View";
	private static final String FUNCTION = "Function";
	private static final String PROCEDURE = "Procedure";
	private static final String TRIGGER = "Trigger";
	private static final String INDEX = "Index";
	private static final String SEQUENCE = "Sequence";
	
	
	
	
	private static Thread currentThread; 
	
	
	@FXML private HBox treePane;
	
	@FXML private JFXComboBox<Label>  soDB;
	@FXML private JFXComboBox<Label>  soSC;
	
	@FXML private JFXComboBox<Label>  taDB;
	
	@FXML private JFXComboBox<Label>  taSC;
	
	@FXML private CheckBox isIgnore; 
	@FXML private CheckBox isDel;
	
	@FXML private CheckBox tabData; 
	@FXML private CheckBox tabStruct; 
	
	@FXML private CheckBox chView;
	@FXML private CheckBox chFun;
	@FXML private CheckBox chPro;
	@FXML private CheckBox chTri;
	@FXML private CheckBox chIndex; 
	@FXML private CheckBox chSeq;
	
	
	@FXML private Label queryLabel;
	@FXML private Button execBtn;
	@FXML private Button stopBtn;
	
	
	
	private CheckBoxTreeItem<String> root;
	 ObservableList<Label > empty = FXCollections.observableArrayList();
	
	// 清除 check Box 
	private void cleanCheckBox() {
		isIgnore.setSelected(false);
		isDel.setSelected(false);
		
		tabData.setSelected(false);
		tabStruct.setSelected(false);
		
		chView.setSelected(false);
		chFun.setSelected(false);
		chPro.setSelected(false);
		chTri.setSelected(false);
		chIndex.setSelected(false);
		chSeq.setSelected(false); 
		
	}
	 

	
	// 初始化方法, 这边在初始化的时候添加按钮的点击事件
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		queryLabel.setGraphic(ImageViewGenerator.svgImageUnactive("search"));
//		execBtn.getStyleClass().add("my-run-btn");
//		execBtn.setStyle("-fx-background-color: green");
		 
		soDB.setItems(DBConns.getChoiceBoxItems());
		taDB.setItems(DBConns.getChoiceBoxItems());
//		soDB.setItems( getConnComboBoxList()); 
//		taDB.setItems( getConnComboBoxList());

		soDB.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<? super Label>) (observable, oldValue, newValue) -> {
					soSC.setItems(empty);
					cleanCheckBox();
					String str = newValue.getText();
					soSC.setItems(getSchemaLabels(str));

					root.getChildren().removeAll(root.getChildren());
//					 soSC.getItems().get(0); 

				});
		soSC.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<? super Label>) (observable, oldValue, newValue) -> {

					root.getChildren().removeAll(root.getChildren());
					cleanCheckBox();
				});

		taDB.getSelectionModel().selectedItemProperty()
				.addListener((ChangeListener<? super Label>) (observable, oldValue, newValue) -> {
					String str = newValue.getText();
					taSC.setItems(getSchemaLabels(str));
				});

		root = new CheckBoxTreeItem<String>("全选");
		root.setExpanded(true);

		CheckTreeView<String> checkTreeView = new CheckTreeView<>(root);
		checkTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		treePane.getChildren().addAll(checkTreeView);
		HBox.setHgrow(checkTreeView, Priority.ALWAYS);

		// check box
		tabData.selectedProperty().addListener((ChangeListener<? super Boolean>) (observable, oldValue, newValue) -> {
			if (newValue && !tabStruct.isSelected()) {
				String dbname = soDB.getValue().getText();
				String schename = "";
				if(soSC.getValue() != null) {
					schename = soSC.getValue().getText();
				}
				
				if (StrUtils.isNotNullOrEmpty(dbname) && StrUtils.isNotNullOrEmpty(schename)) {
					TreeItem<TreeNodePo> schemaNode = ComponentGetter.getSchemaNode(dbname, schename);
					addNode(schemaNode.getChildren().get(0));
				}
			}
			if (!newValue && !tabStruct.isSelected()) { //
				removeNode(TABLE);
			}

		});
		tabStruct.selectedProperty().addListener((ChangeListener<? super Boolean>) (observable, oldValue, newValue) -> {
			if (newValue && !tabData.isSelected()) {
				String dbname = soDB.getValue().getText();
				String schename = soSC.getValue().getText();
//				String schename = "";
				if(soSC.getValue() != null) {
					schename = soSC.getValue().getText();
				}
				if (StrUtils.isNotNullOrEmpty(dbname) && StrUtils.isNotNullOrEmpty(schename)) {
					TreeItem<TreeNodePo> schemaNode = ComponentGetter.getSchemaNode(dbname, schename);
					addNode(schemaNode.getChildren().get(0));
				}
			}
			if (!newValue && !tabData.isSelected()) { //
				removeNode(TABLE);
			}

		});
		chView.selectedProperty().addListener((ChangeListener<? super Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				String dbname = soDB.getValue().getText();
				String schename = soSC.getValue().getText();
				if (StrUtils.isNotNullOrEmpty(dbname) && StrUtils.isNotNullOrEmpty(schename)) {
					TreeItem<TreeNodePo> schemaNode = ComponentGetter.getSchemaNode(dbname, schename);
					addNode(schemaNode.getChildren().get(1));
				}
			}
			if (!newValue ) { //
				removeNode(VIEW);
			}

		});
		
		chFun.selectedProperty().addListener((ChangeListener<? super Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				String dbname = soDB.getValue().getText();
				String schename = soSC.getValue().getText();
				if (StrUtils.isNotNullOrEmpty(dbname) && StrUtils.isNotNullOrEmpty(schename)) {
					TreeItem<TreeNodePo> schemaNode = ComponentGetter.getSchemaNode(dbname, schename);
					addNode(schemaNode.getChildren().get(2));
				}
			}
			if (!newValue ) { //
				removeNode(FUNCTION);
			}

		});
		
		chPro.selectedProperty().addListener((ChangeListener<? super Boolean>) (observable, oldValue, newValue) -> {
			if (newValue) {
				String dbname = soDB.getValue().getText();
				String schename = soSC.getValue().getText();
				if (StrUtils.isNotNullOrEmpty(dbname) && StrUtils.isNotNullOrEmpty(schename)) {
					TreeItem<TreeNodePo> schemaNode = ComponentGetter.getSchemaNode(dbname, schename);
					addNode(schemaNode.getChildren().get(3));
				}
			}
			if (!newValue ) { //
				removeNode(PROCEDURE);
			}

		});
		
		
		//TODO 执行按钮
		execBtn.setOnAction(e->{
			execBtn.setDisable(true);
			execBtn.getStyleClass().add("my-run-btn");
			stopBtn.getStyleClass().add("my-stop-btn");  
			currentThread = new Thread() {
				public void run() {
					runBtnAction();
					Platform.runLater(() -> { 
						execBtn.setDisable(false); 
						execBtn.getStyleClass().remove("my-run-btn");
						stopBtn.getStyleClass().remove("my-stop-btn");
					});
					
				};
			};
			currentThread.start();
		});
		stopBtn.setOnAction(e->{
			if(currentThread != null) {
				currentThread.stop();
				execBtn.setDisable(false);
				execBtn.getStyleClass().remove("my-run-btn");
				stopBtn.getStyleClass().remove("my-stop-btn");
			}
		});

	}
	
	private void runBtnAction() {

		String dbname = "";
		String schename = "";
		
		String targetDBName ="";
		String targetSchename = "";
		
		if(   soSC.getValue() != null 
		   && soDB.getValue() != null 
		   && taDB.getValue() != null 
		   && taSC.getValue() != null ) {
			dbname = soDB.getValue().getText(); 
			schename = soSC.getValue().getText();
			
			targetDBName = taDB.getValue().getText(); 
			targetSchename = taSC.getValue().getText();
		}
		
		if (    StrUtils.isNotNullOrEmpty(dbname) 
		     && StrUtils.isNotNullOrEmpty(schename)
		     && StrUtils.isNotNullOrEmpty(targetDBName)
		     && StrUtils.isNotNullOrEmpty(targetSchename)) {
			
			DbConnectionPo dbpo = DBConns.get(dbname);
			Connection  soConn = dbpo.getConn();
			ExportDDL export = dbpo.getExportDDL();
			
			
			DbConnectionPo tarDbpo = DBConns.get(targetDBName);
			Connection  tarConn = tarDbpo.getConn();
			
			
			// 将要执行的sql集合
			List<String> sqls = new ArrayList<>();
			
			
			if(tabStruct.isSelected()) {  
				List<String>  vals = createSynSql( soConn , export, schename, TABLE ,targetSchename);
				sqls.addAll( vals );
			}
			
			// 视图同步
			if(chView.isSelected()) {
				List<String>  vals = createSynSql( soConn , export, schename, VIEW , targetSchename);
				sqls.addAll( vals );
			}
			// 函数同步
			if(chFun.isSelected()) {
				List<String>  vals = createSynSql( soConn , export, schename, FUNCTION  , targetSchename);
				sqls.addAll( vals );
			}
			// 过程同步
			if(chPro.isSelected()) {
				List<String>  vals = createSynSql( soConn , export, schename, PROCEDURE  , targetSchename);
				sqls.addAll( vals );
			}
			// 触发器同步
			if(chTri.isSelected()) {
				List<String>  vals = createSynSql( soConn , export, schename, TRIGGER  , targetSchename);
				sqls.addAll( vals );
			}
			// 索引同步
			if(chIndex.isSelected()) {
				List<String>  vals = createSynSql( soConn , export, schename, INDEX  , targetSchename);
				sqls.addAll( vals );
			}
			// 序列同步
			if(chSeq.isSelected()) {
				List<String>  vals = createSynSql( soConn , export, schename, SEQUENCE  , targetSchename);
				sqls.addAll( vals );
			}
			
			
			execSQL(sqls, tarConn);
			// 数据同步
			if(tabData.isSelected()) { 
				synTabData( soConn ,tarConn ,  export, schename , targetSchename);
			}
			
		}
		
	
	}
	
 
	// 表 
	private List<String> synTabData(Connection  soConn , Connection  toConn , ExportDDL export,  String schename , String targetSchename) {
			boolean delObj = isDel.isSelected();
			List<String> sqls = new ArrayList<>();
			TreeItem<String> table = rootSubNode(TABLE);
			if(table != null) {
				ObservableList<CheckBoxTreeItem<String> > selectNodes = selectNode(table);
				for(CheckBoxTreeItem<String> cb : selectNodes ) {
//					System.out.println(cb.getValue());
					String tabName = cb.getValue();
					// 删表语句
					if(delObj) {
						String tableName = targetSchename+"."+cb.getValue();
						try {
							DBTools.execDelTab(toConn, tableName);
						} catch (SQLException e) { 
							e.printStackTrace();
						} 
					}
					// 建表语句 TransferTabeDataDao
//					String ctab = export.exportCreateTable(  soConn , schename, tableName);
//					System.out.println(ctab);
//					sqls.add(ctab);
					try {
						TransferTabeDataDao.insertData(soConn,  toConn, tabName, schename, targetSchename);
					} catch (SQLException e) { 
						e.printStackTrace();
					}
					
				}
			}
			return sqls;
		}
	
	// 试图
	private List<String> createSynSql(Connection  soConn , ExportDDL export,  String schename, String nodeType, String targetSchename ) {
			boolean delObj = isDel.isSelected();
			List<String> sqls = new ArrayList<>();
			TreeItem<String> table = rootSubNode(nodeType);
			if(table != null) {
				ObservableList<CheckBoxTreeItem<String> > selectNodes = selectNode(table);
				for(CheckBoxTreeItem<String> cb : selectNodes ) {
					String checkBoxName = cb.getValue();
					//drop语句
					if(delObj) {
						String drop = getDropDDL(export, nodeType, schename, checkBoxName, targetSchename) ; 
						System.out.println(drop);
						sqls.add(drop);
					}
					// create语句
					String create = getCreateDDL(soConn, export, nodeType, schename, checkBoxName, targetSchename);  
					System.out.println(create);
					sqls.add(create);
					
					
				}
			}
			return sqls;
		}
		 
	
	//TODO 获取drop 语句
	private String  getDropDDL( ExportDDL export, String type, String schename, String objName, String tarSchename) {
		String drop = "";
		if(type.equals(TABLE)) {
			drop = export.exportDropTable(schename, objName);
		}else if(type.equals(VIEW)) {
			drop = export.exportDropView(schename, objName);
		}else if(type.equals(FUNCTION)) {
			drop = export.exportDropFunction(schename, objName);
		}else if(type.equals(PROCEDURE)) {
			drop =  export.exportDropProcedure(schename, objName);
		}
		drop = drop.replaceAll(schename+"."+objName,  tarSchename+ "." + objName);
		return drop;
	}
	//TODO 获取Create 语句
	private String  getCreateDDL(Connection  conn, ExportDDL export, String type, String schename, String objName, String tarSchename) {
		String drop = "";
		if(type.equals(TABLE)) {
			drop = export.exportCreateTable(conn, schename, objName);
		}else if(type.equals(VIEW)) {
			drop = export.exportCreateView(conn, schename, objName);
		}else if(type.equals(FUNCTION)) {
			drop = export.exportCreateFunction(conn, schename, objName);
		}else if(type.equals(PROCEDURE)) {
			drop =  export.exportCreateProcedure(conn, schename, objName);
		}
		drop = drop.replaceAll(schename+"."+objName,  tarSchename+ "." + objName);
		return drop;
	}
		
	
	private void execSQL(List<String> sqls , Connection tarConn) {
		Thread th = new Thread() {
			public void run() {
				System.out.println("线程启动了" + this.getName());
				// 执行sql
				for(String sql : sqls) { 
					try { 
						System.out.println(sql);
//						DBTools.execDDL(tarConn, sql);
						PreparedStatement pstmt = null;
						try {
							pstmt = tarConn.prepareStatement(sql);
							pstmt.execute();
						} catch (SQLException e) {
							e.printStackTrace();
							throw e;
						} finally {
							if (pstmt != null)
								pstmt.close();
						}
						
					} catch (Exception e1) { 
						e1.printStackTrace();
					}
				}
				System.out.println("线程end了" + this.getName());	 
			}
		};
		th.start();
	}
	
	// 根据名称获取root 中的子节点
	private TreeItem<String> rootSubNode(String name){
		ObservableList<TreeItem<String> > childs = root.getChildren();
		for(int i = 0; i < childs.size(); i++) {
			TreeItem<String> val = childs.get(i);
			if( val.getValue().equals(name)) {
				return val;
			}
		}
		return null;
	}
	
	// 获取节点下选中的子节点
	private  ObservableList<CheckBoxTreeItem<String> > selectNode(TreeItem<String> node){
		ObservableList<CheckBoxTreeItem<String> > selectNodes = FXCollections.observableArrayList();
		ObservableList<TreeItem<String> > nodeSub = node.getChildren();
		for (int i = 0; i < nodeSub.size(); i++) {
			CheckBoxTreeItem<String> sub = (CheckBoxTreeItem<String>) nodeSub.get(i);
			if(sub.isSelected()) {
				selectNodes.add(sub);
			}
		}
		
		return selectNodes;
	}
	
	// 获取连接名称list
	private ObservableList<Label> getConnComboBoxList() {
		ComboBox<Label> connComboBox = ComponentGetter.connComboBox;
		ObservableList<Label> sos = connComboBox.getItems();
		ObservableList<Label> newVal = FXCollections.observableArrayList();
		for(Label label : sos) {
			Label la = new Label(label.getText());
			
			newVal.add(la);
		}
		return newVal;
	}
	
	// 获取schema名称列表
	private ObservableList<TreeItem<TreeNodePo>> getSchemaComboBoxList(String dbName) {
		 
	    ObservableList<TreeItem<TreeNodePo> > temp = FXCollections.observableArrayList();
	    ObservableList<TreeItem<TreeNodePo> > newVal = FXCollections.observableArrayList();
	    
	    TreeItem<TreeNodePo>  connNode = ComponentGetter.getConnNode(dbName);
	    if(connNode !=null) {
	    	if(connNode.getChildren().size() > 0) {
	    		temp = connNode.getChildren().get(0).getChildren();
		    	if(temp.size() > 0 ) {
	    			for(TreeItem<TreeNodePo> tnp : temp) {
	    				if(tnp.getChildren().size()> 0) {
	    					newVal.add(tnp);
	    				}
	    			}
	    		}
	    	}
	    	
	    }
		return newVal;
	}
	// 获取schema的下拉的数据
	private ObservableList<Label> getSchemaLabels(String dbName ) { 
		ObservableList<TreeItem<TreeNodePo>>  vals = getSchemaComboBoxList(dbName);
		ObservableList<Label> newVal = FXCollections.observableArrayList();
		newVal.add(new  Label(""));
		for(TreeItem<TreeNodePo> val : vals) {
			Label la = new Label(val.getValue().getName());
			
			newVal.add(la);
		}
		return newVal;
	}
	
	// 生成数据checkTreeView
	private void addNode(TreeItem<TreeNodePo> item) {
		CheckBoxTreeItem<String>  pi  = new CheckBoxTreeItem<>(item.getValue().getName());
		ObservableList<CheckBoxTreeItem<String>> newVal = FXCollections.observableArrayList();
		root.getChildren().add(pi);
		ObservableList<TreeItem<TreeNodePo>>  subItem = item.getChildren();
		
		for(TreeItem<TreeNodePo> subNode : subItem) {
			String name = subNode.getValue().getName();
			CheckBoxTreeItem<String> SubCbt  = new CheckBoxTreeItem<>(name);
			
			newVal.add(SubCbt);
		}
		pi.getChildren().addAll( newVal);
	}
	
	private void removeNode(String name) {
		root.getChildren();
		for(int i = 0; i < root.getChildren().size() ; i++) { 
			TreeItem<String> ch = root.getChildren().get(i);
			if(ch.getValue().equals(name)) {
				root.getChildren().remove(i);
				break;
			}
		}
	}
	
	
	
}
