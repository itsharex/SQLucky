package net.tenie.Sqlucky.sdk.subwindow;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.tableview2.cell.ComboBox2TableCell;

import com.jfoenix.controls.JFXCheckBox;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.tenie.Sqlucky.sdk.component.MyTableCellTextField2ReadOnly;
import net.tenie.Sqlucky.sdk.db.DaoTools;
import net.tenie.Sqlucky.sdk.db.SqluckyConnector;
import net.tenie.Sqlucky.sdk.excel.ExcelHeadCellInfo;
import net.tenie.Sqlucky.sdk.excel.ExcelToDB;
import net.tenie.Sqlucky.sdk.excel.ExcelUtil;
import net.tenie.Sqlucky.sdk.po.ExcelFieldPo;
import net.tenie.Sqlucky.sdk.po.SheetFieldPo;
import net.tenie.Sqlucky.sdk.ui.IconGenerator;
import net.tenie.Sqlucky.sdk.ui.LoadingAnimation;
import net.tenie.Sqlucky.sdk.ui.SqluckyStage;
import net.tenie.Sqlucky.sdk.ui.UiTools;
import net.tenie.Sqlucky.sdk.utility.CommonUtility;
import net.tenie.Sqlucky.sdk.utility.StrUtils;

/**
 * excel导入
 * 
 * @author tenie
 *
 */
public class ImportExcelNextWindow {

	private Stage stage;
	private Logger logger = LogManager.getLogger(ImportExcelNextWindow.class);

	private TextField beginIdTF;
	private TextField conuntTF;
	private TextField tfFilePath;
	private JFXCheckBox saveSqlCheckBox;
	private JFXCheckBox onlySave;
	private String excelFile;
	private String tableName;
	private ObservableList<ExcelFieldPo> excelFields;
	private SqluckyConnector sqluckyConn;

	public void showWindow(SqluckyConnector dbc, String tableNameVal, String excelFilePath, Stage parentStage) {
		sqluckyConn = dbc;
		excelFile = excelFilePath;
		tableName = tableNameVal;
		VBox tbox = tableBox(dbc, tableName, excelFile);
		if (tbox == null) {
			MyAlert.errorAlert("没有表<" + tableNameVal + ">的信息, 请确保表存在!");
			return;
		}
		layout(tbox, tableName);
		parentStage.close();

	}

	/*
	 * 对数据库表字段转换为一个表格, 返回null表示没有找到表的信息
	 */
	public VBox tableBox(SqluckyConnector dbc, String tablename, String excelFile) {
		try {
			ObservableList<SheetFieldPo> fields = DaoTools.tableFields(dbc.getConn(), tablename);
			if (fields == null || fields.size() == 0) {
				return null;
			}
			for (int i = 0; i < fields.size(); i++) {
				SheetFieldPo p = fields.get(i);
				StringProperty strp = new SimpleStringProperty("");
				p.setValue(strp);
			}

			excelFields = FXCollections.observableArrayList();
			for (var fd : fields) {
				ExcelFieldPo excelpo = new ExcelFieldPo(fd);
				excelFields.add(excelpo);
			}

			VBox tableBox = tableFiledMapExcelRowBox(tablename, excelFields, excelFile);
			return tableBox;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return new VBox();
	}

	/**
	 * excel 文件头信息
	 * 
	 * @param excelFile
	 * @return
	 */
	private String[] excelHeadArray(String excelFile, ObservableList<ExcelFieldPo> fields) {
		List<ExcelHeadCellInfo> row1 = ExcelUtil.readExcelFileHead(excelFile);

		if (row1 != null) {

			String[] headArr = new String[row1.size()];
			List<String> selectVal = new ArrayList<>();
			for (int i = 0; i < row1.size(); i++) {
				ExcelHeadCellInfo cell = row1.get(i);
				String val = (cell.getCellIdx() + 1) + " - " + cell.getCellAddress() + " - " + cell.getCellVal();
				headArr[i] = val;
				selectVal.add(val);
			}
			for (var tmp : fields) {
				tmp.setExcelRowInfo(selectVal);
			}

			return headArr;
		}

		return new String[0];
	}

	public VBox tableFiledMapExcelRowBox(String tableName, ObservableList<ExcelFieldPo> fields, String excelFile) {
		FlowPane fp = new FlowPane();

		String colName1 = "字段名称";
		String colName2 = "Excel列";
		String colName3 = "自定义值";
		Label tf1 = new Label();
		tf1.setPrefWidth(150);

		Label tf2 = new Label();
		tf2.setPrefWidth(150);
		Label tf3 = new Label();
		tf3.setPrefWidth(150);

		fp.getChildren().add(tf1);
		fp.getChildren().add(tf2);
		fp.getChildren().add(tf3);
		fp.setPadding(new Insets(8, 0, 0, 0));
		Insets is = new Insets(0, 8, 8, 8);
		FlowPane.setMargin(tf1, is);
		FlowPane.setMargin(tf2, is);
		FlowPane.setMargin(tf3, is);

		// table
		TableView<ExcelFieldPo> tv = new TableView<>();
		tv.getStyleClass().add("myTableTag");
		tv.setEditable(true);
		tv.getSelectionModel().selectedItemProperty().addListener(// 选中某一行
				new ChangeListener<ExcelFieldPo>() {
					@Override
					public void changed(ObservableValue<? extends ExcelFieldPo> observableValue, ExcelFieldPo oldItem,
							ExcelFieldPo newItem) {
						ExcelFieldPo p = newItem;
						if (p == null)
							return;
						tf1.setText(p.getColumnLabel().get());
						String tyNa = p.getColumnTypeName().get() + "(" + p.getColumnDisplaySize().get();
						if (p.getScale() != null && p.getScale().get() > 0) {
							tyNa += ", " + p.getScale().get();
						}
						tyNa += ")";
						tf2.setText(tyNa);
						tf3.setText(p.getColumnClassName().get());
					}
				});
		// 第一列
		TableColumn<ExcelFieldPo, String> fieldNameCol = new TableColumn<>(colName1); // "Field Name"
		fieldNameCol.setEditable(false);
		// 给单元格赋值
		fieldNameCol.setCellValueFactory(cellData -> {
			return cellData.getValue().getColumnLabel();
		});

		fieldNameCol.setCellFactory(MyTableCellTextField2ReadOnly.forTableColumn());
		fieldNameCol.setPrefWidth(180);

		tv.getColumns().add(fieldNameCol);

		// 第二列
		TableColumn<ExcelFieldPo, String> valueCol = new TableColumn<>(colName2);
		valueCol.setPrefWidth(180);

		String[] headArr = excelHeadArray(excelFile, fields);
		valueCol.setCellValueFactory(p -> p.getValue().getExcelRowVal());
		valueCol.setCellFactory(ComboBox2TableCell.forTableColumn(headArr));

		tv.getColumns().add(valueCol);

		// 第三列
		TableColumn<ExcelFieldPo, String> valueCol3 = new TableColumn<>(colName3);
		valueCol3.setPrefWidth(180);

		valueCol3.setCellValueFactory(p -> p.getValue().getFixedValue());
		valueCol3.setCellFactory(TextFieldTableCell.forTableColumn());

		tv.getColumns().add(valueCol3);

		tv.getItems().addAll(fields);

		VBox subvb = new VBox();
		FlowPane topfp = new FlowPane();
		topfp.setPadding(new Insets(8, 5, 8, 8));
		Label lb = new Label();
		lb.setGraphic(IconGenerator.svgImageDefActive("search"));
		TextField filterField = new TextField();

		filterField.getStyleClass().add("myTextField");
		topfp.getChildren().add(lb);
		FlowPane.setMargin(lb, new Insets(0, 10, 0, 5));
		topfp.getChildren().add(filterField);
		topfp.setMinHeight(30);
		topfp.prefHeight(30);
		filterField.setPrefWidth(200);

		subvb.getChildren().add(topfp);

		subvb.getChildren().add(tv);
		VBox.setVgrow(tv, Priority.ALWAYS);
		subvb.getChildren().add(fp);

		// 过滤功能
		filterField.textProperty().addListener((o, oldVal, newVal) -> {
			if (StrUtils.isNotNullOrEmpty(newVal)) {
				TableDataDetail.bindTableViewExcelFieldFilter(tv, fields, newVal);
			} else {
				tv.setItems(fields);
			}

		});

		return subvb;
	}

	// 按钮面板
	private AnchorPane btnPane() {
		AnchorPane btnPane = new AnchorPane();
		// 保存按钮
		Button nextbtn = saveBtnSetup();

		Button cancel = cancelBtn();

		btnPane.getChildren().addAll(cancel, nextbtn);
		AnchorPane.setRightAnchor(nextbtn, 10.0);
		AnchorPane.setRightAnchor(cancel, 60.0);
		return btnPane;
	}

	// 其他设置
	public List<Region> otherSet() {
		Label lb1 = new Label("起始行号");
		beginIdTF = new TextField();
		beginIdTF.setPromptText("默认第一行开始");
//		HBox b1 = new HBox();
//		b1.getChildren().addAll(lb1, beginIdTF);

		Label lb2 = new Label("导入行数");
		conuntTF = new TextField();
		conuntTF.setPromptText("默认全部");
//		HBox b2 = new HBox();
//		b2.getChildren().addAll(lb2, conuntTF);

		saveSqlCheckBox = new JFXCheckBox("导入SQL保存到文件");
		tfFilePath = new TextField();
		tfFilePath.disableProperty().bind(saveSqlCheckBox.selectedProperty().not());
		Button selectFile = UiTools.openFileBtn(tfFilePath, stage);
		selectFile.disableProperty().bind(saveSqlCheckBox.selectedProperty().not());

		onlySave = new JFXCheckBox("只保存SQL到文件, 不用插入到数据库");
		onlySave.disableProperty().bind(saveSqlCheckBox.selectedProperty().not());
		HBox b2 = new HBox();
		b2.getChildren().addAll(tfFilePath, selectFile);

		List<Region> nds = new ArrayList<>();
//		nds.add(b1);
//		nds.add(b2);
		nds.add(lb1);
		nds.add(beginIdTF);
		nds.add(lb2);
		nds.add(conuntTF);
		nds.add(saveSqlCheckBox);
		nds.add(b2);

		nds.add(null);
		nds.add(onlySave);
		return nds;
	}

	// 组件布局
	public void layout(VBox tbox, String table) {
		VBox vb = new VBox();
		vb.maxHeight(600);
		vb.getChildren().add(tbox);

		GridPane grid = new GridPane();
		vb.getChildren().add(grid);
		vb.setPadding(new Insets(5));

		// 按钮
		AnchorPane btnsPane = btnPane();
		vb.getChildren().add(btnsPane);

		Stage stage = CreateWindow(vb, "Excel Row Map " + table + " Field");
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 10, 10));

		List<Region> list = otherSet();

		int i = 0;
		int j = 0;

		for (int k = 0; k < list.size(); k += 2) {
			var node1 = list.get(k);
			var node2 = list.get(k + 1);
			int idxi = i++;
			int idxj = j++;
			if (node1 != null)
				grid.add(node1, 0, idxi);
			if (node2 != null)
				grid.add(node2, 1, idxj);
		}

		stage.show();
	}

	// 保存按钮
	public Button saveBtnSetup() {
		Button btn = new Button("Save");
		btn.getStyleClass().add("myAlertBtn");
		btn.setOnAction(e -> {
			if (saveSqlCheckBox.isSelected()) {
				String filePath = tfFilePath.getText();
				if (StrUtils.isNullOrEmpty(filePath)) {
					MyAlert.errorAlert("保存Sql的文件路径不能为空!");
					tfFilePath.requestFocus();
					return;
				}
			}
			List<ExcelFieldPo> vals = new ArrayList<>();
			// 提取有被映射的字段
			for (ExcelFieldPo fieldpo : excelFields) {
				fieldpo.getExcelRowVal().getValue();
				if (StrUtils.isNotNullOrEmpty(fieldpo.getExcelRowVal())
						|| StrUtils.isNotNullOrEmpty(fieldpo.getFixedValue())) {
					vals.add(fieldpo);
				}

			}
			String beginInt = beginIdTF.getText();
			String countInt = conuntTF.getText();
			Integer beginval = null;
			Integer countval = null;
			if (StrUtils.isNotNullOrEmpty(beginInt)) {
				beginval = Integer.valueOf(beginInt);
				// 程序的下标是从0开始, 需要减 1
				beginval = beginval - 1;
			}
			if (StrUtils.isNotNullOrEmpty(countInt)) {
				countval = Integer.valueOf(countInt);
			}
			btn.setDisable(true);

			Integer tmpBeginval = beginval;
			Integer tmpCountval = countval;
			LoadingAnimation.loadingAnimation("Saving....", v -> {
				try {
					ExcelToDB.toTable(sqluckyConn, tableName, excelFile, tfFilePath.getText(), vals, tmpBeginval,
							tmpCountval, onlySave.isSelected());
					MyAlert.infoAlert("导入成功!");
				} catch (Exception e1) {
					MyAlert.showTextArea("Error", "导入失败 ! \n" + e1.getMessage());
				}
				btn.setDisable(false);
			});

		});
		return btn;
	}

	public Button cancelBtn() {
		Button cancelBtn = new Button("Cancel");
		cancelBtn.getStyleClass().add("myAlertBtn");
		cancelBtn.setOnAction(e -> {
			stage.close();
		});
		return cancelBtn;
	}

	public Stage CreateWindow(VBox vb, String title) {
		SqluckyStage sqluckyStatge = new SqluckyStage(vb);
		stage = sqluckyStatge.getStage();
		Scene scene = sqluckyStatge.getScene();

		vb.getStyleClass().add("connectionEditor");

		vb.setPrefWidth(600);
		vb.maxWidth(600);
		AnchorPane bottomPane = new AnchorPane();
		bottomPane.setPadding(new Insets(10));

		vb.getChildren().add(bottomPane);
		KeyCodeCombination escbtn = new KeyCodeCombination(KeyCode.ESCAPE);
		KeyCodeCombination spacebtn = new KeyCodeCombination(KeyCode.SPACE);
		scene.getAccelerators().put(escbtn, () -> {
			stage.close();
		});
		scene.getAccelerators().put(spacebtn, () -> {
			stage.close();
		});

		stage.setTitle(title);
		CommonUtility.loadCss(scene);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setScene(scene);

		stage.setMaximized(false);
		stage.setResizable(false);
		return stage;
	}

}
