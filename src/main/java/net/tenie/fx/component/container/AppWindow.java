package net.tenie.fx.component.container;

import org.controlsfx.control.MasterDetailPane;
import javafx.geometry.Side;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.tenie.fx.component.ComponentGetter;

/*   @author tenie */
public class AppWindow {
	private VBox mainWindow;
	private MenuBarContainer mainMenuBar;
	private MasterDetailPane masterDetailPane;
	private OperateContainer operate;
	private DataViewContainer dataView;

	public AppWindow() {
		mainWindow = new VBox();

		mainMenuBar = new MenuBarContainer();
		masterDetailPane = new MasterDetailPane(Side.BOTTOM);
		masterDetailPane = new MasterDetailPane(Side.BOTTOM);
		operate = new OperateContainer();
		dataView = new DataViewContainer();

		masterDetailPane.getStyleClass().add(0, "masterDetailPane");
		masterDetailPane.setShowDetailNode(false);
		masterDetailPane.setMasterNode(operate.getContainer());
		masterDetailPane.setDetailNode(dataView.getContainer());
		masterDetailPane.setDividerPosition(0.6);
		VBox.setVgrow(masterDetailPane, Priority.ALWAYS);

		ComponentGetter.masterDetailPane = masterDetailPane;
		mainWindow.getChildren().addAll(mainMenuBar.getMainMenuBar(), masterDetailPane);

	}

	public VBox getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(VBox mainWindow) {
		this.mainWindow = mainWindow;
	}

	public MenuBarContainer getMainMenuBar() {
		return mainMenuBar;
	}

	public void setMainMenuBar(MenuBarContainer mainMenuBar) {
		this.mainMenuBar = mainMenuBar;
	}

	public MasterDetailPane getMasterDetailPane() {
		return masterDetailPane;
	}

	public void setMasterDetailPane(MasterDetailPane masterDetailPane) {
		this.masterDetailPane = masterDetailPane;
	}

	public OperateContainer getOperate() {
		return operate;
	}

	public void setOperate(OperateContainer operate) {
		this.operate = operate;
	}

	public DataViewContainer getDataView() {
		return dataView;
	}

	public void setDataView(DataViewContainer dataView) {
		this.dataView = dataView;
	}

}
