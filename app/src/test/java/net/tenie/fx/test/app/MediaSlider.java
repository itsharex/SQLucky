package net.tenie.fx.test.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.tenie.Sqlucky.sdk.component.SqlcukyEditor;

import java.io.File;
 
public class MediaSlider extends Application{
    public String DurationToString(Duration duration){
        int time = (int)duration.toSeconds();
        int hour = time /3600;
        int minute = (time-hour*3600)/60;
        int second = time %60;
        return hour + ":" + minute + ":" + second;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
 
        File url = new File("C:\\Users\\tenie\\Documents\\WeChat Files\\lovehexieshehui\\FileStorage\\Video\\2021-11\\4547be5a23d9e58412e63e8aafc80078.mp4");
 
        Media media = new Media(url.getAbsoluteFile().toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);
 
        Slider processSlider=new Slider();
        Label processLabel=new Label();
        Button playButton=new Button("||");
        Button rePlayButton=new Button(">>");
        Slider volumeSlider=new Slider();
 
        playButton.setOnAction(e->{
            String text=playButton.getText();
            if(text=="||"){
                playButton.setText(">");
                mediaPlayer.play();
            }
            else {
                playButton.setText("||");
                mediaPlayer.pause();
            }
        });
 
        rePlayButton.setOnAction(e->{
            mediaPlayer.seek(Duration.ZERO);
        });
 
       volumeSlider.setValue(10);
       mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty().divide(100));
 
       Duration totalDuration = mediaPlayer.getTotalDuration();
       String totalString=DurationToString(totalDuration);
       double maxProcessSlider= processSlider.getMax();
       mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
           @Override
           public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                   processSlider.setValue(newValue.toSeconds() / totalDuration.toSeconds() * 100);
                   processLabel.setText(DurationToString(newValue) + " " + DurationToString(mediaPlayer.getTotalDuration()));
           }
       });
 
        processSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                double totalTime=media.getDuration().toMillis();
                double newTime=processSlider.valueProperty().getValue()/100*totalTime;
                mediaPlayer.seek(Duration.millis(newTime));
            }
        });
 
        BorderPane borderPane=new BorderPane();
//        HBox hbox = new HBox();
//        hbox.setAlignment(Pos.CENTER);
//        hbox.getChildren().addAll(processSlider,processLabel,playButton,rePlayButton,volumeSlider);
//        borderPane.setBottom(hbox);
        borderPane.setCenter(mediaView);
 
        Scene scene = new Scene(borderPane,500,350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("mediaplayer");
//        primaryStage.setMaximized(true);
        primaryStage.show();
        mediaPlayer.play();
//        mediaPlayer.getCurrentRate()
       
        Thread      th = new Thread() {
			public void run() {
				 int i = 0;
				try {
					  while(true) {
						 
			        	  var val = mediaPlayer.getCurrentRate();
			        	  System.out.println(val);
			        	  System.out.println(  mediaPlayer.currentCountProperty());
			        	  System.out.println( mediaPlayer.getStatus());
			        	  
			        	 
			        	 
			        	  Thread.sleep(500);
			        	  i++;
			        	  if(i == 40) break;
			        }
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}
		};
		th.start();
      
    }
    public static void main(String[]args){
        Application.launch(args);
    }
 
}
