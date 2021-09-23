package net.tenie.fx.test.app;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App16_8 extends Application {

    private TextArea textArea = new TextArea("我喜欢用JavaFx编程");

    @Override
    public void start(Stage primaryStage) {

        StackPane stackPane = new StackPane();
        Circle circle = new Circle(50);
        circle.setStroke(Color.BLUE);
        circle.setFill(Color.RED);
         
        stackPane.getChildren().addAll(textArea);

        // 从下面语句创建一个淡入淡出效果对象并设置持续事件为2S
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000));
        fadeTransition.setFromValue(0.0);   // 设置起始透明度为1.0，表示不透明
        fadeTransition.setToValue(1.0);     // 设置结束透明度为0.0，表示透明
        fadeTransition.setCycleCount(1);     // 设置循环周期为无限
        fadeTransition.setAutoReverse(true);    // 设置自动反转
        fadeTransition.setNode(textArea);         // 设置动画应用的节点
        fadeTransition.play();                  // 播放动画

        Scene scene = new Scene(stackPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("淡入淡出动画");
        primaryStage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}