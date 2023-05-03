package net.tenie.fx.test.window;

import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import net.tenie.Sqlucky.sdk.utility.CommonUtility;
import net.tenie.fx.factory.ButtonFactory;

public class WinOsAppBox extends Application {
	 
    //窗体拉伸属性
    private static boolean isRight;// 是否处于右边界调整窗口状态
    private static boolean isBottomRight;// 是否处于右下角调整窗口状态
    private static boolean isBottomLeft;
     
    private static boolean isBottom;// 是否处于下边界调整窗口状态
    private static boolean isTop;
    private static boolean isLeft;
    private static boolean isTopLeft;

    private static boolean isTopRight;
    
     
    private final static int RESIZE_WIDTH = 5;// 判定是否为调整窗口状态的范围与边界距离
    private final static double MIN_WIDTH = 300;// 窗口最小宽度
    private final static double MIN_HEIGHT = 250;// 窗口最小高度
    private double xOffset = 0;
    private double yOffset = 0;
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

    	AnchorPane operateBtnPane = ButtonFactory.codeAreabtnInit();
    	root.getChildren().add(operateBtnPane);
//    	root.getChildr en().addAll(close, hidden, max, full);
    	
    	
        stage.setTitle("Hello World");
        stage.setScene(new Scene(root, 800, 450));
        stage.initStyle(StageStyle.TRANSPARENT);
 
        root.setOnMouseMoved(event -> {
            event.consume();
            double x = event.getSceneX();
            double y = event.getSceneY();
            double width = stage.getWidth();
            double height = stage.getHeight();
            
            double xstage = stage.getX();
            double ystage = stage.getY(); 
            CommonUtility.delayRunThread(v->{
            	 System.out.println("x = " + x + " | y = "+ y + " | xstage = "+xstage + " | ystage = "+ystage
            			 +" width = " + width + " | height = "+ height);
            }, 100);
           
            Cursor cursorType = Cursor.DEFAULT;// 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型
 
            // 先将所有调整窗口状态重置
            isTopRight = isTopLeft = isBottomLeft = isBottomRight =  isLeft =  isTop = isRight = isBottom = false;
         // 左下角调整窗口状态 isBottomLeft
            if (x <= RESIZE_WIDTH && y >= height - RESIZE_WIDTH) { 
            	isBottomLeft = true;
                cursorType = Cursor.NE_RESIZE;
           }
           // 右下角调整窗口状态 
            else if(y >= height - RESIZE_WIDTH && x >= width - RESIZE_WIDTH) {
            	 isBottomRight = true;
                 cursorType = Cursor.SE_RESIZE;
            } // 左上 isTopLeft
            else if( RESIZE_WIDTH >= x && RESIZE_WIDTH >= y ) { //x <= RESIZE_WIDTH
            	isTopLeft = true;
                cursorType = Cursor.SE_RESIZE;
            }
            // 右上 isTopRight
            else if( RESIZE_WIDTH >= y  && x >= width - RESIZE_WIDTH) { //x <= RESIZE_WIDTH
            	isTopRight = true;
                cursorType = Cursor.NE_RESIZE;
            }
            // 下边界调整窗口状态  isBottom
            else if(y >= height - RESIZE_WIDTH){
            	 isBottom = true;
                 cursorType = Cursor.S_RESIZE;
            } 
            // 右边界调整窗口状态 isRight
            else if (x >= width - RESIZE_WIDTH) {
                isRight = true;
                cursorType = Cursor.E_RESIZE;
            }
            else if( RESIZE_WIDTH >= y ) { //上面
            	 cursorType = Cursor.S_RESIZE;
            	 isTop = true;
            }
			else if (RESIZE_WIDTH >= x) { //  左面
				System.out.println(x);
				cursorType = Cursor.E_RESIZE;
				isLeft = true;
			}else {
				cursorType = Cursor.DEFAULT;
			}
			
            
            
            // 最后改变鼠标光标
            root.setCursor(cursorType);
        });
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
      
        
        root.setOnMouseDragged(event -> {
        	// 鼠标x y 坐标
            double x = event.getSceneX();
            double y = event.getSceneY();
            
            
            // 窗口的x, y ;  保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
            double nextX = stage.getX();
            double nextY = stage.getY();
            
            
            double nextWidth = stage.getWidth();
            double nextHeight = stage.getHeight();
            
//            double stageHeight = stage.getHeight();
//            double stageWidth = stage.getWidth();
            
 
            if( isRight || isBottomRight || isBottom ) {
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
            
			if (isBottomLeft) {
				nextHeight = y;
				
				double tmpWidth = nextWidth - x;
        		if (tmpWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
        			tmpWidth = MIN_WIDTH;
        			return;
 	            }
				if (nextHeight <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
					nextHeight = MIN_HEIGHT;
					return;
				}
				stage.setY(nextY);
				stage.setHeight(nextHeight);

				stage.setX(nextX + x);  
			    stage.setWidth(tmpWidth);
			}
            
			if (isTopRight) {

				nextWidth = x;
				if (nextWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
					nextWidth = MIN_WIDTH;
					return;
				}

				double tmpH = nextHeight - y;
				if (tmpH <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
					tmpH = MIN_HEIGHT;
					return;
				}

				stage.setX(nextX);
				stage.setWidth(nextWidth);

				stage.setY(nextY + y);
				stage.setHeight(tmpH);

			}
			if (isTopLeft) {
				double tmpWidth = nextWidth - x;
				if (tmpWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
					tmpWidth = MIN_WIDTH;
					return ;
				}
				double tmpH = nextHeight - y;
				if (tmpH <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
					tmpH = MIN_HEIGHT;
					return ;
				}

				stage.setX(nextX + x);
				stage.setWidth(tmpWidth);

				stage.setY(nextY + y);
				stage.setHeight(tmpH);
			}
           if(isLeft) {
        		double tmpWidth = nextWidth - x;
        		if (tmpWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
        			tmpWidth = MIN_WIDTH;
        			return;
 	            }

        		stage.setX(nextX + x);  
                stage.setWidth(tmpWidth);
           }
            
			if (isTop) {
				double tmpH = nextHeight - y;
				if (tmpH <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
					tmpH = MIN_HEIGHT;
					return;
				} 
				stage.setY(nextY + y);
				stage.setHeight(tmpH); 
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