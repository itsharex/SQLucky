package net.tenie.fx.test.window;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;

import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.*;
 
public class CloseFXWindow extends Application {
  public static void main(final String... args) {
    Application.launch(CloseFXWindow.class, args);
  }
 
  @Override public void start(final Stage stage) throws IOException {
	  
	  
		SVGGlyphLoader sl = new SVGGlyphLoader();
		File svgFile = new File("D:\\myGit\\Learning_Notes\\图标\\s.svg");
		URL url=svgFile.toURL();
		
		SVGGlyph svg = sl.loadGlyph(url);
    stage.setTitle("FX Close");
    
    Button close = new Button("Close");
    close.setOnAction(e->{
    	Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST ));
    });
    close.setGraphic(svg);
    
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
    
	VBox vbox = new VBox(8);
	vbox.getChildren().addAll(close, hidden, max, full);
    Group root = new Group(vbox); 
    Scene scene = new Scene(root, 300, 100);
    stage.setScene(scene);
    stage.show();
  }


}
