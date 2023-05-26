package net.tenie.fx.test.window;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.girod.javafx.svgimage.SVGLoader;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SvgFileLoad extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		File svgFile = new File("D:\\myGit\\Learning_Notes\\图标\\s.svg");
//	SVGLoader loader = new SvgLoader();
		Group svgImage = SVGLoader.load(svgFile);
//    Group svgImage = loader.loadSvg(svgFile);

		// Scale the image and wrap it in a Group to make the button
		// properly scale to the size of the image
		svgImage.setScaleX(0.1);
		svgImage.setScaleY(0.1);
		Group graphic = new Group(svgImage);

		// create a button and set the graphics node
		Button button = new Button();
		button.setGraphic(graphic);

		// add the button to the scene and show the scene
		HBox layout = new HBox(button);
		HBox.setMargin(button, new Insets(10));
		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(final String... args) {
		Application.launch(SvgFileLoad.class, args);
	}

}
