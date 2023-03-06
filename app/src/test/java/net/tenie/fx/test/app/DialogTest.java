package net.tenie.fx.test.app;

import java.util.Optional;

import com.jfoenix.controls.JFXButton;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import net.tenie.Sqlucky.sdk.config.ConfigVal;
import net.tenie.fx.test.Dialog.MyDialog;
import net.tenie.fx.test.Dialog.MyTestAlert;
import net.tenie.fx.test.Dialog.MyTestAlert.MyAlertType;

public class DialogTest extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }
    @Override
    public void init() {
  
    		ConfigVal.cssList.add(DialogTest.class.getResource("/css/application.css").toExternalForm());	
    		ConfigVal.cssList.add(DialogTest.class.getResource("/css/dark/common.css").toExternalForm());	
    		ConfigVal.cssList.add(DialogTest.class.getResource("/css/dark/sql-keywords.css").toExternalForm());	    
    		ConfigVal.cssList.add(DialogTest.class.getResource("/css/dark/treeView.css").toExternalForm());
    		ConfigVal.cssList.add(DialogTest.class.getResource("/css/dark/TableView.css").toExternalForm());
    		ConfigVal.cssList.add(DialogTest.class.getResource("/css/dark/tabPane.css").toExternalForm());
    		 
    		ConfigVal.cssListLight.add(DialogTest.class.getResource("/css/application.css").toExternalForm());	
    		ConfigVal.cssListLight.add(DialogTest.class.getResource("/css/light/common-light.css").toExternalForm());
    		ConfigVal.cssListLight.add(DialogTest.class.getResource("/css/light/sql-keywords-light.css").toExternalForm());
    		ConfigVal.cssListLight.add(DialogTest.class.getResource("/css/light/tabPane-light.css").toExternalForm());
    		
    		ConfigVal.cssListYellow.add(DialogTest.class.getResource("/css/application.css").toExternalForm());	
    		ConfigVal.cssListYellow.add(DialogTest.class.getResource("/css/yellow/common-yellow.css").toExternalForm());
    		ConfigVal.cssListYellow.add(DialogTest.class.getResource("/css/yellow/sql-keywords-yellow.css").toExternalForm());
    		ConfigVal.cssListYellow.add(DialogTest.class.getResource("/css/yellow/treeView-yellow.css").toExternalForm());
    		ConfigVal.cssListYellow.add(DialogTest.class.getResource("/css/yellow/TableView-yellow.css").toExternalForm());
    		ConfigVal.cssListYellow.add(DialogTest.class.getResource("/css/yellow/tabPane-yellow.css").toExternalForm());	 
 
    }
    @Override
    public void start(Stage primaryStage) {

        HBox root = new HBox();

        EventHandler<KeyEvent> handler = new EventHandler<KeyEvent>() {

            private boolean willConsume = true;

            @Override
            public void handle(KeyEvent event) {


                if (event.getCode()  == KeyCode.SLASH) {
                    if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                        willConsume = true;
                    } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
                        willConsume = false;
                    }
                }

                if (willConsume) {
                    event.consume();
                }
            }

        };

        
        Button btn = new Button("test");
        btn.setOnAction(e->{
        	createDialog();
        });
        
        Button btn2 = new Button("MyAlert");
        btn2.setOnAction(e->{
//        	ModalDialog.Confirmation("111");
//        	MyTestAlert ma = new MyTestAlert(MyAlertType.CONFIRMATION);
//        	ma.setTitle("Confirmation");
//        	ma.show();
        	
        	 
        	MyDialog<Pair<String, String>> dialog = new MyDialog<>();
        	dialog.setTitle("Login Dialog");
        	Pane p = new Pane(); 
        	VBox vb = new VBox();
    		vb.getStyleClass().add("myAlert");

    		TextField username = new TextField();
    		username.setPromptText("Username");
    		PasswordField password = new PasswordField();
    		password.setPromptText("Password");
    		JFXButton Cancelbtn = new JFXButton("Cancel");
    		btn.getStyleClass().add("myAlertBtn");
    		vb.getChildren().addAll(username, password, Cancelbtn);
    		
//    		dialog.getDialogPane().getChildren().add(vb);
    		dialog.setDialogPane(vb);   		
    		
    		dialog.setResultConverter(dialogButton -> {
    				System.out.println(dialogButton);
    		        return new Pair<>(username.getText(), password.getText());
    		});

    		Optional<Pair<String, String>> result = dialog.showAndWait();
    		System.out.println("1111");
    		System.out.println(username.getText() +password.getText());
        });
        
        root.getChildren().addAll(btn, btn2);

        Scene scene = new Scene(root, 500, 500);

        primaryStage.setScene(scene);
        primaryStage.show();

    }
    
    
    /**
     * 
     */
    public void createDialog() {
    	Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login Dialog");
		dialog.setHeaderText("Look, a Custom Login Dialog");

		// Set the icon (must be included in the project).
		// Get the Stage.
//		Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();

		// Add a custom icon.
//		stage.getIcons().add(new Image(this.getClass().getResource("login.png").toString()));
//		dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton =  dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);
		loginButton.getStyleClass().clear();
		loginButton.getStyleClass().add("myAlertOkBtn");

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		        return new Pair<>(username.getText(), password.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();
		System.out.println("1111");
		result.ifPresent(usernamePassword -> {
		    System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
		});
    }
    
     

}