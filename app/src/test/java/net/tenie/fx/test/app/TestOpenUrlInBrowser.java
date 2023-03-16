package net.tenie.fx.test.app;
 
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TestOpenUrlInBrowser extends Application {
    public static void main(String[] args) {
        launch(TestOpenUrlInBrowser.class,args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
       

        //创建一个url
        URL url = new URL("http://www.haotuo.net.cn/Resources/cq/qunlogo.png");
        Button button = new Button("按钮");
        button.setPrefWidth(200);
        button.setPrefHeight(200);
        //触碰到按钮的时候 鼠标变成 手形
        button.setCursor(Cursor.CLOSED_HAND);
        button.setOnAction(e->{
        	 //使用默认浏览器打开百度
            HostServices host = getHostServices();
            host.showDocument("www.baidu.com");
        });

        Group group = new Group();
        group.getChildren().add(button);
        Scene scene = new Scene(group);
        primaryStage.setScene(scene);
        //触碰到界面的时候 鼠标变成 url的这张图片
        scene.setCursor(Cursor.cursor(url.toExternalForm()));

        //程序左上角的图片 与 任务栏下面的图片 变成 url指定的图片
        primaryStage.getIcons().add(new Image(url.toExternalForm()));
        primaryStage.setWidth(800);
        primaryStage.setHeight(800);
        primaryStage.setTitle("JavaFX");
        primaryStage.show();
    }
}
