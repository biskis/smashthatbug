package com.biskis.smashthatbug;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MyActivity extends Activity {
    private static final String TAG = MyActivity.class.getSimpleName();
    private MainGamePanel mainGamePanel = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "CREATE");
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
   //         /*
        //no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //sa nu adoarma
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        //incepe jocu
        if(mainGamePanel == null) {
            mainGamePanel = new MainGamePanel(this);
            Log.d(TAG, "mainGamePanel is null");
            if(savedInstanceState != null) {
                String where_i_am = savedInstanceState.getString("where_i_am");
                Log.d(TAG, "where i am: " + where_i_am);
                if(where_i_am != null){
                    Log.d(TAG, "start from WHere i am");
                    mainGamePanel.setWhereIAm(where_i_am);
                    if(where_i_am.equals("playing"))
                        mainGamePanel.init();   //todo trebe salvat toate starile la gaze si sa mergem de acolo incolo...
                        Log.d(TAG, "init");
                }
            } else {
                Log.d(TAG, "saveInstanceState is null");
            }
        }
        setContentView(mainGamePanel);
        /*
        //ads
        //adView = (AdView)this.findViewById(R.id.adView);
        //LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);

        // Add the adView to it
        //layout.addView(adView);

        // Initiate a generic request to load it with an ad
        //adView.loadAd(new AdRequest());
        /**/
    }

    @Override
    public void onStart(){
        Log.d(TAG, "START");
        super.onStart();
    }

    @Override
    public void onResume(){
        Log.d(TAG, "RESUME");
        super.onResume();

        if(mainGamePanel != null && mainGamePanel.getThread() != null){
            mainGamePanel.getThread().setRunning(true);
            if(!mainGamePanel.getThread().isAlive()){
                try{
                    mainGamePanel.getThread().start();
                } catch (Exception e){
                    Log.d(TAG,e.toString());
                }
                Log.d(TAG,"START THREAD pt ca nu e alive");
            }
        }
        /*
        if(mainGamePanel != null && mainGamePanel.getThread() != null){
            if(!mainGamePanel.getThread().isRunning()){
                mainGamePanel.getThread().setRunning(true);
                if(!mainGamePanel.getThread().isAlive())
                    mainGamePanel.getThread().start();
            }
        } */

    }

    @Override
    public void onPause(){
        Log.d(TAG, "PAUSE");
        super.onPause();
        //if(mainGamePanel != null)
        //    mainGamePanel.getThread().setRunning(false);
        if(mainGamePanel != null && mainGamePanel.getThread() != null){
            mainGamePanel.getThread().setRunning(false);
            try {
                mainGamePanel.getThread().join();
                Log.d(TAG, "Threaduri so joinuit. [My activity]");
            } catch (InterruptedException e) {
                Log.d(TAG, "Eroare la join threads");
            }
        }
    }

    @Override
    public void onStop(){
        Log.d(TAG, "Stop:P");
        super.onStop();

    }

    @Override
    public void onRestart(){
        Log.d(TAG, "RESTSRT");
        super.onRestart();
    }

    @Override
    public void onDestroy(){
        Log.d(TAG, "Destroing:D");
        super.onDestroy();
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        Log.d(TAG, "SAVE INSTANCE STATE");
        super.onSaveInstanceState(outState);
        if(mainGamePanel != null){
            outState.putString("where_i_am", mainGamePanel.getWhereIAm());
            Log.d(TAG,  mainGamePanel.getWhereIAm());
        }
    }

}
