/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FiboStopWatch;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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


/**
 *
 * @author steav
 */
public final class FiboStopWatch extends Application {
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
        
        // タイマーをインスタンス化
        TimerLabel timer_count = new TimerLabel();

        // botton 
        Button btnStart = new Button();
        btnStart.setText("Start");
        // botton action
        btnStart.setOnAction(e -> {
            System.out.println("Start!");
            timer_count.start();
        });

        // Stop botton
        Button btnStop = new Button();
        btnStop.setText("Stop");
        btnStop.setOnAction(e -> {
            timer_count.stop();
        });
        
        // Reset botton
        Button btnReset = new Button();
        btnReset.setText("Reset");
        btnReset.setOnAction(e -> {
            timer_count.reset();
        });
        
        // 下段のボタンを整列
        HBox hbox = new HBox(btnStart, btnStop, btnReset);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        
        // 上段のタイマー部と下段のボタン部を整列
        VBox vbox = new VBox(timer_count, hbox);
        vbox.setAlignment(Pos.CENTER);
        
        // main window
        Scene scene = new Scene(vbox, 200, 150);
        window.setScene(scene);
        window.show();
    }
}

/**
 * ラベルクラスを承継し今回のタイマーで使うラベルとして機能を付与します。
 * @author steav
 */
final class TimerLabel extends Label {
    // field variable
    private final StringProperty mmssTime = new SimpleStringProperty("00:00");
    private Timeline timeline;
    private Boolean onFlag;
    private Instant start;
    private Duration time;
    private Duration lasttime;
    private List<Integer> Fibolist;

    public TimerLabel() {
        this.textProperty().bind(mmssTime);// data binding
        this.setTextFill(Color.NAVY);// Color setting
        this.setStyle("-fx-font-size: 4em;");// make timer bigger
        this.lasttime = Duration.ZERO;
        Fibolist = Fibonacci(100);
    }
    
    /**
     * 
     */
    public void start() {
        onFlag = true;
        start = Instant.now();// タイマーの基準時
        // 反復処理
        timeline = new Timeline(new KeyFrame(javafx.util.Duration.millis(10),
                e2 -> {
                    if (onFlag) {// このフラグでタイマーの実行を制御
                        time = Duration.between(start, Instant.now());// 基準時との差
                        time = time.plus(lasttime);// 前回スタートした際に経過した時間をプラス
                        mmssTime.set(maketimeString(time));// タイマーを表示するメソッドの呼出し
                    }else {
                        timeline.stop();
                    }
                }
            )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    /**
     * フラグを操作しタイマーの実行を停止
     * 加えて、累積の経過時間を保持
     */
    public void stop(){
        lasttime = time;
        onFlag = false;
    }
    
    /**
     * タイマー停止、各種設定を初期化
     */
    protected void reset() {
        mmssTime.set(maketimeString(Duration.ZERO));
        start = null;
        time = Duration.ZERO;
        lasttime = Duration.ZERO;
        onFlag = false;
    }
    
    /**
     * タイマーの表示を担当するメソッド
     * @param time
     * @return 
     */
    private String maketimeString(Duration time) {
        // 秒数を分と秒に分離
        String isTime = String.format("%02d:%02d"
                ,time.toMinutes()
                ,time.getSeconds() % 60
            );
        // フィボナッチ数列かを判定しタイマーの色を変更
        if (isFibonacci((int)time.getSeconds())) {
            this.setTextFill(Color.CRIMSON);
        } else {
            this.setTextFill(Color.NAVY);
        }

        return isTime;
    }
    
    /**
     * フィボナッチ数列か否かを判定
     * @param num フィボナッチ数列を何個まで計算するかを決定
     * @return boolean
     */
    private boolean isFibonacci(int num){
        return Fibolist.contains(num);
    }
    
    /**
     * フィボナッチ数列を生成し返り値とする
     * @param iterate
     * @return Listとしてフィボナッチ数列をreturn
     */
    private List<Integer> Fibonacci(int iterate){
        Integer temp;
        Integer num1 = 1;
        Integer num2 = 1;
        List<Integer> fibolist = new ArrayList<>();
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
