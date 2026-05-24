package com.jonhu.app.controllers;

import com.jonhu.core.builder.Builder;
import com.jonhu.core.builder.ConvertingTask;
import com.jonhu.core.parser.MyNotesFormatParser;
import com.jonhu.util.Constant;
import com.jonhu.util.Utils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * 负责多个Controller之间的通信
 */
public class ControllerManager {

    public static Pane welcomePane;
    public static Pane defaultPane;
    public static Pane outputChoosePane;
    //public static Pane wordPane;
    public static Pane pptPane;
    //public static Pane mdPane;
    public static Set<File> srcFiles = new HashSet<>();
    public static File destDir;
    //private static final Parser PARSER = new MyNotesFormatParser();
	
	//记录任务完成的数量
	private static volatile int count = 0;

    private static Timeline initTimeline(Pane pane, int endValue) {
        return new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(pane.translateXProperty(), endValue)));
    }

    /**
     * 翻页
     */
    public static void turnPanes(Pane hide, Pane show) {
        Timeline hideAnimation = initTimeline(hide, -Constant.WINDOW_WIDTH);
        hideAnimation.setOnFinished(event -> hide.setVisible(false));
        Timeline showAnimation = initTimeline(show, 0);
        showAnimation.setOnFinished(event -> show.setVisible(true));

        hideAnimation.play();
        showAnimation.play();
    }

    public static void startTask(Builder builder, String extension, Pane showing) {
		Utils.alert("转换中,请耐心等待", "转换过程中请耐心等待,转换完成后会自动跳转值初始界面");
		srcFiles.forEach(src -> new Thread(() -> {
			try {
				System.out.println("src:"+src.getAbsolutePath()+"\tdest:"+getDestFile(src, extension));
				new ConvertingTask(src,getDestFile(src,extension),builder,new MyNotesFormatParser()).start();
				count++;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}).start());

		Thread check = new Thread(() -> {
			for(;;) {
				if (srcFiles.size() == count) {
					turnPanes(showing, defaultPane);
					count = 0;
					break;
				} try {
					Thread.sleep(500);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		check.setDaemon(true);
		check.start();
    }

    private static File getDestFile(File src, String extension) {
        return new File(ControllerManager.destDir.getAbsolutePath() + File.separator + src.getName().split("\\.")[0] + extension);
    }
	
	
}