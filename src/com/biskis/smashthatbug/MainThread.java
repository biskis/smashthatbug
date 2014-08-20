package com.biskis.smashthatbug;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

//import javax.xml.transform.sax.SAXTransformerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Dani Dudas
 * Date: 11/17/11
 * Time: 9:39 PM
 */
public class MainThread extends Thread {
    private boolean running;
    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;

    private static final String TAG = MainThread.class.getSimpleName();

    public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }
    public void setRunning(boolean running){
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run(){
        Canvas canvas;
        Log.d(TAG, "Starting game loop");
        //long tickCount = 0;
        while (running){
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.onDraw(canvas);
                }
            } finally {
                if(canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            long sleep_time = 10;
            try {
                Thread.sleep(sleep_time);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            //Log.d(TAG,"running....");
        }
        Log.d(TAG, "FINISH: thread");
    }
}
