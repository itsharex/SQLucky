package net.tenie.fx.test.control;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class WebViewTest extends Application {
  @Override
  public void start(final Stage stage) {
    stage.setWidth(400);
    stage.setHeight(500);
    Scene scene = new Scene(new Group());


    final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setContent(browser);

    webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
          @Override
          public void changed(ObservableValue ov, State oldState, State newState) {

            if (newState == Worker.State.RUNNING) {
              stage.setTitle(webEngine.getLocation());
            }

          }
        });
    webEngine.load("file:///D:/myGit/Learning_Notes/java/chat/NiceAdmin/index.html");

    scene.setRoot(scrollPane);

    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
