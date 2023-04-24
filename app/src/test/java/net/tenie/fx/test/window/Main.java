package net.tenie.fx.test.window;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Main extends Application {
	 
    //窗体拉伸属性
    private static boolean isRight;// 是否处于右边界调整窗口状态
    private static boolean isBottomRight;// 是否处于右下角调整窗口状态
    private static boolean isBottom;// 是否处于下边界调整窗口状态
    private static boolean isTop;
    private final static int RESIZE_WIDTH = 5;// 判定是否为调整窗口状态的范围与边界距离
    private final static double MIN_WIDTH = 300;// 窗口最小宽度
    private final static double MIN_HEIGHT = 250;// 窗口最小高度
    private double xOffset = 0;
    private double yOffset = 0;
    static boolean topLock = false;
    @Override
    public void start(Stage stage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
    	  Button close = new Button("Close");
    	    close.setOnAction(e->{
    	    	Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST ));
    	    });
    	    
    	    
    	    Button hidden = new Button("hidden");
    	    hidden.setOnAction(e->{
    	    	 stage.setIconified(true);
    	    });
    	    
    	    Button full = new Button("Full Screen");
    	    full.setOnAction(e->{
    	    	stage.setFullScreen(true);
    	    });
    	    
    	    
    	    Button max = new Button("Max");
    	    max.setOnAction(e->{
    	    	 javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    	         stage.setX(primaryScreenBounds.getMinX());
    	         stage.setY(primaryScreenBounds.getMinY());
    	         stage.setWidth(primaryScreenBounds.getWidth());
    	         stage.setHeight(primaryScreenBounds.getHeight());
    	    });
    	VBox root = new VBox(8);
    	root.getChildren().addAll(close, hidden, max, full);
        
    	
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 800, 450));
        stage.initStyle(StageStyle.TRANSPARENT);
 
        root.setOnMouseMoved(event -> {
            event.consume();
            double x = event.getSceneX();
            double y = event.getSceneY();
            double width = stage.getWidth();
            double height = stage.getHeight();
            
//            System.out.println("x = " + x + " | y = "+ y +"\n width = " + width + " | height = "+ height);
//            double top = stage.get
//            stage.get
            Cursor cursorType = Cursor.DEFAULT;// 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型
 
            // 先将所有调整窗口状态重置
            isTop = isRight = isBottomRight = isBottom = false;
            if (y >= height - RESIZE_WIDTH) {
                if (x <= RESIZE_WIDTH) {// 左下角调整窗口状态
 
                } else if (x >= width - RESIZE_WIDTH) {// 右下角调整窗口状态
                    isBottomRight = true;
                    cursorType = Cursor.SE_RESIZE;
                } else {// 下边界调整窗口状态
                    isBottom = true;
                    cursorType = Cursor.S_RESIZE;
                }
            } else if (x >= width - RESIZE_WIDTH) {// 右边界调整窗口状态
                isRight = true;
                cursorType = Cursor.E_RESIZE;
            }else if( RESIZE_WIDTH >= y ) {
            	 System.out.println(y );
            	 cursorType = Cursor.S_RESIZE;
            	 isTop = true;
            }
            
            
            
            // 最后改变鼠标光标
            root.setCursor(cursorType);
        });
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        double viewWidth = screenRectangle.getWidth();
        double viewHeight = screenRectangle.getHeight();
      
        
        root.setOnMouseDragged(event -> {
            double x = event.getSceneX();
            double y = event.getSceneY();
            // 保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
            double nextX = stage.getX();
            double nextY = stage.getY();
            double stageY = stage.getY();
            
            double nextWidth = stage.getWidth();
            double nextHeight = stage.getHeight();
            
            double stageHeight = stage.getHeight();
            
            double topNextHeight = 0.0;
 
            if( isRight || isBottomRight || isBottomRight || isBottom ) {
            	 if (isRight || isBottomRight) {// 所有右边调整窗口状态
                     nextWidth = x;
                 }
                 if (isBottomRight || isBottom) {// 所有下边调整窗口状态
                     nextHeight = y;
                 }
                if (nextWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
	               nextWidth = MIN_WIDTH;
	            }
	            if (nextHeight <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
	               nextHeight = MIN_HEIGHT;
	            }
	            
	      	  // 最后统一改变窗口的x、y坐标和宽度、高度，可以防止刷新频繁出现的屏闪情况
                stage.setX(nextX);
                stage.setY(nextY);
                stage.setWidth(nextWidth);
                stage.setHeight(nextHeight);

                System.out.println("y = " +y+ " | nextY = "+nextY+" | nextHeight = " + nextHeight);
	            
            }
            
           
            
            if( isTop && topLock == false) { 
            	topLock = true;
            	topNextHeight = stageHeight - y;
           
                if (topNextHeight >= viewHeight) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
                	topNextHeight = viewHeight;
                }
                if( topNextHeight < 0 ) {
                	return;
                }
                stage.setX(nextX);
                
                double tmpY  = topNextHeight - nextHeight;
	           	 tmpY = stageY - tmpY;
	           	 if(tmpY <0 ) {
	           		 return;
	           	 }else if(tmpY > viewHeight) {
	           		 return;
	           	 }
	           	 
	           	 stage.setY(tmpY);
	           	 
                stage.setWidth(nextWidth);
                stage.setHeight(topNextHeight);
//                System.out.println("isRight = " + isRight + " | isBottom = " + isBottom + " | isBottomRight = " + isBottomRight);
                
                System.out.println("y = " +y+ " | tmpY = "+tmpY+" | topNextHeight = " + topNextHeight);
                topLock = false;
            }


            
            
          
//            if(!isBottom && !isBottomRight && !isRight && !isTop)
//            {
//                stage.setX(event.getScreenX() - xOffset);
//                stage.setY(event.getScreenY() - yOffset);
//            }
        });
 
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
 
 
 
        stage.show();
    }
 
 
    public static void main(String[] args) {
        launch(args);
    }
}