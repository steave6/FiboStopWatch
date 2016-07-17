/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiboStopWatch;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import java.time.Duration;
import java.time.Instant;
import javafx.scene.paint.Color;
import java.util.*;
import javafx.event.Event;


/**
 *
 * @author steav
 */
public final class FiboStopWatch extends Application implements EventHandler<ActionEvent>{
    // Field variable
    private Button btnStart;
    private TimerLabel timer_count;
    private Button btnStop;
    private Button btnReset;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String... args) {
        launch(args);
    }
        
    
    @Override
    public void start(Stage primaryStage) {
        Stage window;
        
        window = primaryStage;
        window.setTitle("Timer!");
        
        timer_count = new TimerLabel();

        // botton 
        btnStart = new Button();
        btnStart.setText("Start");
        btnStart.setOnAction(this);

        // Stop botton
        btnStop = new Button();
        btnStop.setText("Stop");
        btnStop.setOnAction(this);

        
        // Reset botton
        btnReset = new Button();
        btnReset.setText("Reset");
        btnReset.setOnAction(this);

        
        HBox hbox = new HBox(btnStart, btnStop, btnReset);
        hbox.setAlignment(Pos.BOTTOM_CENTER);

        
        VBox vbox = new VBox(timer_count, hbox);
        vbox.setAlignment(Pos.CENTER);
        
        // main window
        Scene scene = new Scene(vbox, 200, 150);
        window.setScene(scene);
        window.show();
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource()==btnStart) {
            System.out.println("Start!");
            timer_count.start();
        }
        if (event.getSource()==btnStop) {
            timer_count.stop();
        }
        if (event.getSource()==btnReset) {
            timer_count.reset();
        }
    }
    
    
}

final class TimerLabel extends Label {
    // field variable
    private final StringProperty mmssTime = new SimpleStringProperty("00:00");
    private Timeline timeline;
    private Boolean onFlag = true;
    private Instant start;

    public TimerLabel() {
        this.textProperty().bind(mmssTime);
        this.setTextFill(Color.NAVY);
        this.setStyle("-fx-font-size: 4em;");
    }
    
    public void start() {
        if (start != null) {
            System.out.println(start);
            timeline.stop();
            return;
        }
        start = Instant.now(); 
        timeline = new Timeline(new KeyFrame(javafx.util.Duration.millis(10),
                e2 -> {
                    Duration time = Duration.between(start, Instant.now());
                    mmssTime.set(maketimeString(time));
                    if (!onFlag) {
                        timeline.stop();
                    }
                }
            )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    public void stop(){
        onFlag = false;
    }
    
    protected void reset() {
        mmssTime.set(maketimeString(Duration.ZERO));
        start = null;
        onFlag = true;
    }
    
    private String maketimeString(Duration time) {
        String isTime = String.format("%02d:%02d"
                ,time.toMinutes()
                ,time.getSeconds() % 60
            );
        if (isFibonacci((int)time.getSeconds())) {
            this.setTextFill(Color.CRIMSON);
        } else {
            this.setTextFill(Color.NAVY);
        }

        return isTime;
    }
    private Boolean isFibonacci(int second){
        ArrayList<Integer> fibolist = Fibonacci(100);
        return fibolist.contains(second);
    }
    
    private ArrayList<Integer> Fibonacci(int iterate){
        Integer temp;
        Integer num1 = 1;
        Integer num2 = 1;
        ArrayList<Integer> fibolist = new ArrayList<>();
        fibolist.add(num1);
        fibolist.add(num2);
        for (int i = 0; i < iterate; i++) {
            temp = num1 + num2;
            num1 = num2;
            num2 = temp;
            fibolist.add(temp);
        }
        return fibolist;
    }
}
