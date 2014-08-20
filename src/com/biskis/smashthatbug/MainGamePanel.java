package com.biskis.smashthatbug;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Dani Dudas
 * Date: 11/17/11
 * Time: 9:37 PM
 */
public class MainGamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = MainGamePanel.class.getSimpleName();
    private MainThread thread ;
    private CopyOnWriteArrayList<Bug> bugs;
    private Random rand = new Random();

    //private int stage = 0;    nu mai trebe
    //private int level = 0;    nu mai trebe

    private String whereIAm = "home";
    private boolean playing = false;
    private boolean alive;
    private boolean pause;
    private boolean show_alert = false;
    private String alert_type = "";
    private boolean first_time = true;

    private int maxIdGazaRea ;
    private int maxIdGazaBuna ;
    private int id_bug_smash;
    private int score;
    private int bugs_kill;
    public int MAX_BUGS_ON_SCREEN;

    private int difficulty = 2; //0-kid, 1-easy 2-mediu, 3-hard
    private int bg_id = 0;
    private int[] bugs_to_show; //idurile bugurilor care le afisez
    private String fullname;

    private Context context;
    private Bitmap background;
    //private Bitmap background_home;
    private Bitmap background_help = null;
    private Bitmap background_scores = null;
    private Bitmap btn_pauza;
    private Bitmap btn_home;
    private Bitmap btn_try_again;
    private Bitmap btn_help;
    private Bitmap btn_resume;
    private Bitmap btn_play;
    private Bitmap btn_share;
    //private Bitmap logo;

    //START SCREEN
    private Bitmap start_screen;
    private Bitmap btn_home_play;
    private Bitmap btn_home_scores;
    private Bitmap btn_home_help;
    private Bitmap btn_home_settings;
    private Bitmap btn_home_home;

    //SETTINGS
    private Bitmap background_settings;
    private Bitmap btn_settings_baby_s = null;
    private Bitmap btn_settings_baby_us = null;
    private Bitmap btn_settings_easy_s = null;
    private Bitmap btn_settings_easy_us = null;
    private Bitmap btn_settings_medium_s = null;
    private Bitmap btn_settings_medium_us = null;
    private Bitmap btn_settings_hard_s = null;
    private Bitmap btn_settings_hard_us = null;

    private Constante constante = null;

    private String CONFIG_FILENAME = "smastthatbug_settings";
    private Scoreboard scoreboard;

    //todo del dupa modificari
    private int max_level;

    //private int posXMax = 250;
    //private int posYMax = 500;
    public String log = "";
    public String log2 = "";


    public MainGamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        setFocusable(true);
        this.context = context;

        read_config_from_file();
        //todo bg in functie de care ii ala preferat.
        this.background  = BitmapFactory.decodeResource(getResources(), R.drawable.bg_patura);
        //this.background_home  = BitmapFactory.decodeResource(getResources(), R.drawable.bg_verde);
        //this.background_scores  = BitmapFactory.decodeResource(getResources(), R.drawable.bg_scores);
        //this.background_help  = BitmapFactory.decodeResource(getResources(), R.drawable.bg_help);
        //this.background_settings  = BitmapFactory.decodeResource(getResources(), R.drawable.bg_settings);
        //START SCREEN - HOME
        this.start_screen = BitmapFactory.decodeResource(getResources(), R.drawable.start_screen);
        this.btn_home_help = BitmapFactory.decodeResource(getResources(), R.drawable.btn_home_help);
        this.btn_home_play = BitmapFactory.decodeResource(getResources(), R.drawable.btn_home_play);
        this.btn_home_settings = BitmapFactory.decodeResource(getResources(), R.drawable.btn_home_settings);
        this.btn_home_scores = BitmapFactory.decodeResource(getResources(), R.drawable.btn_home_scores);
        this.btn_home_home = BitmapFactory.decodeResource(getResources(), R.drawable.btn_home_home);
        //init aici butoane ca sa mearga mai usor apoi.
        this.btn_pauza = BitmapFactory.decodeResource(getResources(), R.drawable.btn_pause);
        this.btn_home = BitmapFactory.decodeResource(getResources(), R.drawable.btn_home);
        this.btn_try_again = BitmapFactory.decodeResource(getResources(), R.drawable.btn_try_again);
        this.btn_help = BitmapFactory.decodeResource(getResources(), R.drawable.btn_help);
        this.btn_resume = BitmapFactory.decodeResource(getResources(), R.drawable.btn_resume);
        this.btn_play = BitmapFactory.decodeResource(getResources(), R.drawable.btn_play);
        this.btn_share = BitmapFactory.decodeResource(getResources(), R.drawable.btn_share);
        //this.logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        bugs_init();
        //scoareboard
        scoreboard = new Scoreboard(context);
        scoreboard.read_scoreboard();
        //read_scoreboard();
    }
    //load settings bitmaps
    private void load_content_settings(){
        if(btn_settings_baby_s == null){    //load all. niciodata nu os a fie doar 1 load. ci ori toate ori niciunul
            btn_settings_baby_s =  BitmapFactory.decodeResource(getResources(), R.drawable.btn_settings_baby_select);
            btn_settings_baby_us =  BitmapFactory.decodeResource(getResources(), R.drawable.btn_settings_baby_unselected);
            btn_settings_easy_s =  BitmapFactory.decodeResource(getResources(), R.drawable.btn_settings_easy_selected);
            btn_settings_easy_us =  BitmapFactory.decodeResource(getResources(), R.drawable.btn_settings_easy_unselected);
            btn_settings_medium_s =  BitmapFactory.decodeResource(getResources(), R.drawable.btn_settings_medium_selected);
            btn_settings_medium_us =  BitmapFactory.decodeResource(getResources(), R.drawable.btn_settings_medium_unselected);
            btn_settings_hard_s =  BitmapFactory.decodeResource(getResources(), R.drawable.btn_settings_hard_selected);
            btn_settings_hard_us =  BitmapFactory.decodeResource(getResources(), R.drawable.btn_settings_hard_unselected);
            //todo redimensionez butoanele in functi de constante.
        }
        if(background_settings == null){
            background_settings = BitmapFactory.decodeResource(getResources(), R.drawable.bg_settings);
            this.background_settings = Bitmap.createScaledBitmap(this.background_settings, getWidth(), getHeight(), false);
        }
    }
    private void load_content_help(){
        if(background_help == null) {
            this.background_help  = BitmapFactory.decodeResource(getResources(), R.drawable.bg_help);
            this.background_help = Bitmap.createScaledBitmap(this.background_help, getWidth(), getHeight(), false);
        }
    }
    private void load_content_score(){
        if(background_scores == null) {
            this.background_scores  = BitmapFactory.decodeResource(getResources(), R.drawable.bg_scores);
            this.background_scores = Bitmap.createScaledBitmap(this.background_scores, getWidth(), getHeight(), false);
        }
    }

    private void bugs_init(){
        //bug1 = BitmapFactory.decodeResource(getResources(), R.drawable.ladybug2);
        bug1 = BitmapFactory.decodeResource(getResources(), R.drawable.bug1);
        bug2 = BitmapFactory.decodeResource(getResources(), R.drawable.bug_r1);
        bug3 = BitmapFactory.decodeResource(getResources(), R.drawable.bug2);
        bug4 = BitmapFactory.decodeResource(getResources(), R.drawable.bug_r2);
        bug5 = BitmapFactory.decodeResource(getResources(), R.drawable.bug_musca);
        //bug50 = BitmapFactory.decodeResource(getResources(), R.drawable.bug_buburuza);
        bug50 = BitmapFactory.decodeResource(getResources(), R.drawable.ladybug2);
        bug51 = BitmapFactory.decodeResource(getResources(), R.drawable.bee);
        bug52 = BitmapFactory.decodeResource(getResources(), R.drawable.bug_fluture);
    }

    private void read_config_from_file(){
        FileInputStream fin = null;
        byte[] config_bytes = new byte[100];
        String config = null;
        //configurare default:
        this.difficulty = 2;
        this.bg_id = 0;
        //this.bugs_to_show = new int[100];
        this.fullname = "Me";
        try {
            fin = context.openFileInput(CONFIG_FILENAME);
            fin.read(config_bytes);
            fin.close();
            config = new String(config_bytes);
            //log2 = config;
            //parsam configul

            String[] config_split = config.split("#");
            for(String c_split : config_split){
                String[] c = c_split.split("=");
                if(c[0].equals("difficulty")){
                    this.difficulty = Integer.parseInt(c[1]);
                } else if(c[0].equals("bg")){
                    this.bg_id = Integer.parseInt(c[1]);
                } else if(c[0].equals("fisrttime")){
                    this.first_time = Boolean.getBoolean(c[1]);
                } else if(c[0].equals("bugs")){
                    //todo lista cu buguri de spart
                } else if(c[0].equals("name")){
                    this.fullname = c[1];
                }
            }
        } catch (FileNotFoundException e) {
            save_config_to_file();
            log = "File not found [open]";
        } catch (IOException e) {
            log2 = "eroare io";
        }
    }

    private void save_config_to_file(){
        String string = "difficulty=" + this.difficulty + "#";
        string += "bg=" + this.bg_id + "#";
        string += "firsttime=" + ((this.first_time) ? 1 : 0) + "#";
        //todo save lista de bugs care pot fi apasate.
        //string += "bugs=" + implode_int(this.bugs_to_show, ",") + "#";
        string += "name=" + this.fullname + "#";

        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(CONFIG_FILENAME, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            log = "file not found [save]";
        } catch (IOException e) {
            log2 = "eroare write" + e.toString();
        }
    }

    public static String implode_int(int[] ary, String delim) {
       String out = "";
        for(int i=0; i<ary.length; i++) {
            if(i!=0) { out += delim; }
            out += ary[i];
        }
        return out;
    }
/*
    public void start_game(int level){
        init();
        thread.setRunning(true);
        thread.start();
    }
*/
    public void init(){
        this.alive = true;
        this.playing = true;
        this.whereIAm = "playing";
        this.bugs = new CopyOnWriteArrayList<Bug>();
        this.score = 1230;
        this.bugs_kill = 0;
        this.MAX_BUGS_ON_SCREEN = 4;
        this.pause = false;
        this.show_alert = false;
        this.alert_type = "none";
        this.maxIdGazaBuna = 1;
        this.maxIdGazaRea = 1;
        this.id_bug_smash = 0;

        if(this.first_time){
            this.pause = true;
            this.show_alert = true;
            this.alert_type = "first_time";
            this.first_time = false;
        }
        //this.max_level = 2;
        //this.MAX_BUGS_ON_SCREEN = 2;
        /*
        for(int i=0;i<MAX_BUGS_ON_SCREEN / 2;i++){
            add_new_bug(1);
        }
        for(int i=0;i<MAX_BUGS_ON_SCREEN / 2;i++){
            add_new_bug(2);
        }
        */

    }

    Bitmap bug1;
    Bitmap bug2;
    Bitmap bug3;
    Bitmap bug4;
    Bitmap bug5;
    Bitmap bug50;
    Bitmap bug51;
    Bitmap bug52;
    private Bitmap getBitmapByLevel(int id){
        /*
        1 - furnica
        2 - gaza colorata
        3 - paianjen
        4 - gaza colorata
        5 - musca
        6 - tantar

        50 - buburuza
        51 - albina
        52 - fluturas
         */
        switch (id){
            case 1:
                return bug1;
            case 2:
                return bug2;
            case 3:
                return bug3;
            case 4:
                return bug4;
            case 5:
                return bug5;
            case 50:
                return bug50;
            case 51:
                return bug51;
            case 52:
                return bug52;
            default:
                return BitmapFactory.decodeResource(getResources(), R.drawable.bug1);
        }
        /*
        Bitmap bmp = null ;
        switch (id){
            case 1:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bug1);
            break;
            case 2:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bug_r1);
            break;
            case 3:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bug2);
            break;
            case 4:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bug_r2);
            break;
            case 5:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bug_musca);
            break;
            case 50:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bug_buburuza);
            break;
            case 51:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bug_albina);
            break;
            case 52:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bug_fluture);
            break;
            default:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bug);
            break;
        }
        return bmp;
        */
        /*Matrix mtx = new Matrix();
        mtx.postRotate(90);
        Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mtx, true);
        return  rotatedBMP;
        */
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //Canvas canvas = getHolder().lockCanvas();
        Log.d(TAG, "SURFACE CREATE");
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int dpiClassification = dm.densityDpi;
        this.constante = new Constante(getWidth(), getHeight(), dpiClassification, this.difficulty);
        //BG GAME
        this.background = Bitmap.createScaledBitmap(this.background, getWidth(), getHeight(), false);
        //START SCREEN
        this.start_screen = Bitmap.createScaledBitmap(this.start_screen,getWidth(),getHeight(),false);
        this.btn_home_play = Bitmap.createScaledBitmap(this.btn_home_play,constante.xHomeButton,constante.yHomeButton,false);
        this.btn_home_help = Bitmap.createScaledBitmap(this.btn_home_help,constante.xHomeButton,constante.yHomeButton,false);
        this.btn_home_scores = Bitmap.createScaledBitmap(this.btn_home_scores,constante.xHomeButton,constante.yHomeButton,false);
        this.btn_home_settings = Bitmap.createScaledBitmap(this.btn_home_settings,constante.xHomeButton,constante.yHomeButton,false);
        this.btn_home_home = Bitmap.createScaledBitmap(this.btn_home_home,constante.xHomeButton,constante.yHomeButton,false);
        //BG DEFAULT HOMES.
        //this.background_help = Bitmap.createScaledBitmap(this.background_help, getWidth(), getHeight(), false);
        //this.background_scores = Bitmap.createScaledBitmap(this.background_scores, getWidth(), getHeight(), false);
        //this.background_settings = Bitmap.createScaledBitmap(this.background_settings, getWidth(), getHeight(), false);
        //this.background_home = Bitmap.createScaledBitmap(this.background_home, getWidth(), getHeight(), false);
        //BUTOANE ALERT
        this.btn_home = Bitmap.createScaledBitmap(this.btn_home, constante.xAlertButton, constante.yAlertButton, false);
        this.btn_resume = Bitmap.createScaledBitmap(this.btn_resume, constante.xAlertButton, constante.yAlertButton, false);
        this.btn_play = Bitmap.createScaledBitmap(this.btn_play, constante.xAlertButton, constante.yAlertButton, false);
        this.btn_help = Bitmap.createScaledBitmap(this.btn_help, constante.xAlertButton, constante.yAlertButton, false);
        this.btn_try_again = Bitmap.createScaledBitmap(this.btn_try_again, constante.xAlertButton, constante.yAlertButton, false);
        this.btn_share = Bitmap.createScaledBitmap(this.btn_share, constante.xAlertButton, constante.yAlertButton, false);

        Log.d(TAG, "###### DIMENSIUNI ####");
        Log.d(TAG, "denisity: " + dpiClassification);
        Log.d(TAG, "background: " + getWidth() + " x " + getHeight());
        Log.d(TAG, "butoane home: " + constante.xHomeButton + " x " + constante.yHomeButton);
        Log.d(TAG, "alert: " + constante.xAlert + " x " + constante.yAlert);
        Log.d(TAG, "alert buttons: " + constante.xAlertButton + " x " + constante.yAlertButton);
        Log.d(TAG, "font mic: "+ constante.fontSizeMic + ", mare: "+ constante.fontSizeMare);
        Log.d(TAG, "###### END DIMENSIUNI ####");

        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        if(!thread.isAlive()){
            try{
                thread.start();
            } catch (Exception e){
                Log.d(TAG,e.toString());
            }
            Log.d(TAG,"START THREAD pt ca nu e alive");
            Log.d(TAG,thread.toString());
            Log.d(TAG,thread.getState().toString());     //TERMINATED
            Log.d(TAG, String.valueOf(thread.isRunning()));
        }

    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //To change body of implemented methods use File | Settings | File Templates.
        Log.d(TAG, "SURFACE CHANGE");
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.d(TAG, "SURFACE DESTROY");
        boolean retry = true;
        while (retry){
            try{
                thread.join();
                Log.d(TAG, "thread join");
                retry = false;
            } catch (InterruptedException e){

            }
        }
    }
    
    private void whereiam_goto(String w){
        if(w.equals("settings")){
            load_content_settings();
        } else if(w.equals("help")){
            load_content_help();
        } else if(w.equals("scoreboard")){
            load_content_score();
        } else if(w.equals("playing")){
            init();
        }
        this.whereIAm = w;        
    }


    private void touchEventHome(MotionEvent event){
        if(event.getX() > constante.xHomeButtonPlay && event.getX() < constante.xHomeButtonPlay + constante.xHomeButton &&
                event.getY() > constante.yHomeButtonPlay && event.getY() < constante.yHomeButton + constante.yHomeButtonPlay){
            whereiam_goto("playing");
            //log ="click play";
        }

        //daca ating help?
        if(event.getX() > constante.xHomeButtonHelp && event.getX() < constante.xHomeButtonHelp + constante.xHomeButton &&
                event.getY() > constante.yHomeButtonHelp && event.getY() < constante.yHomeButton + constante.yHomeButtonHelp)
            whereiam_goto("help");

        //daca ating scoreboard?
        if(event.getX() > constante.xHomeButtonScores && event.getX() < constante.xHomeButtonScores + constante.xHomeButton &&
                event.getY() > constante.yHomeButtonScores && event.getY() < constante.yHomeButton + constante.yHomeButtonScores)
            whereiam_goto("scoreboard");

        //daca ating options?
        if(event.getX() > constante.xHomeButtonSettings && event.getX() < constante.xHomeButtonSettings + constante.xHomeButton &&
                event.getY() > constante.yHomeButtonSettings && event.getY() < constante.yHomeButton + constante.yHomeButtonSettings)
            whereiam_goto("settings");

        /*  OLD
        //daca ating play?
        if(event.getX() > constante.xHomeButtonOffset && event.getX() < constante.xHomeButtonOffset + constante.xHomeButton &&
            event.getY() > constante.yHomeButtonOffset_play && event.getY() < constante.yHomeButton + constante.yHomeButtonOffset_play){
            init();
            //log ="click play";
        }

        //daca ating options?
        if(event.getX() > constante.xHomeButtonOffset && event.getX() < constante.xHomeButtonOffset + constante.xHomeButton &&
            event.getY() > constante.yHomeButtonOffset_settings && event.getY() < constante.yHomeButton + constante.yHomeButtonOffset_settings)
            whereIAm = "settings";

        //daca ating scoreboard?
        if(event.getX() > constante.xHomeButtonOffset && event.getX() < constante.xHomeButtonOffset + constante.xHomeButton &&
            event.getY() > constante.yHomeButtonOffset_scoreboard && event.getY() < constante.yHomeButton + constante.yHomeButtonOffset_scoreboard)
            whereIAm = "scoreboard";

        //daca ating help?
        if(event.getX() > constante.xHomeButtonOffset && event.getX() < constante.xHomeButtonOffset + constante.xHomeButton &&
            event.getY() > constante.yHomeButtonOffset_help && event.getY() < constante.yHomeButton + constante.yHomeButtonOffset_help)
            whereIAm = "help";
        */
    }

    private void touchEventPlaying(MotionEvent event){
        if(show_alert){
            touchEventAlert(event);
        } else if(alive && !pause){
            for(Bug bug : bugs){
                if(bug.isActive() && !bug.isOutside()){
                    if(bug.can_be_touch()){
                        if (bug.handleActionDown((int)event.getX(), (int)event.getY())) {
                            bug.setActive(false);
                            this.score += bug.getScore();
                            this.bugs_kill ++;
                        }
                    } else if (bug.handleActionDownEnemy((int)event.getX(), (int)event.getY())) {
                        this.id_mesaj_dead = -1;
                        this.alive = false;
                        this.pause = true;
                        this.show_alert = true;
                        this.alert_type = "dead";
                        this.id_bug_smash = bug.getId();
                        String currentDateTimeString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date());
                        this.scoreboard.add(new Scoreboard_entry(this.score, currentDateTimeString, this.fullname));
                    }
                }
            }
            //daca apas pe butonul de pauza
            if(event.getX() < getWidth() - 10 && event.getX() > getWidth() - 35 && event.getY() < 35 && event.getY() > 10){
                this.pause = true;
                this.show_alert = true;
                this.alert_type = "pause";
            }
        }
    }
    private void touchEventAlert(MotionEvent event){
        //log = "touch in alert";
        int fromX = constante.xAlertOffset;
        int fromY = constante.yAlertOffset;
        if(event.getY() > constante.yAlertButton1Offset && event.getY() < constante.yAlertButton1Offset + constante.yAlertButton){
            if(event.getX() > constante.xAlertButton1Offset && event.getX() < constante.xAlertButton1Offset + constante.xAlertButton){
                //primul buton
                if(alert_type.equals("dead")){
                    init();
                } else if(alert_type.equals("pause") || alert_type.equals("first_time")){
                    this.pause = false;
                    this.show_alert = false;
                    this.alert_type = "none";
                }
            } else if(event.getX() > constante.xAlertButton2Offset && event.getX() < constante.xAlertButton2Offset + constante.xAlertButton){
                //al doilea buton
                if(alert_type.equals("first_time"))
                    whereiam_goto("help");
                else if(alert_type.equals("dead")){
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My new score at Smash that bug");   //todo edit mesajul de share
                    //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My new score is " + this.score + ". How many bugs can you kill? [Smash that bug]");    //todo edit mesajul de share
                    //if($code == md5('134' . $score . 'furnicarRr' . md5('biskis' . $score))){
                    String link = "http://www.smashthatbug.com/?score=" + this.score + "&code=" + getMD5("134" + this.score + "furnicarRr" + getMD5("biskis" + this.score));
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "My new score is " + this.score + ". How many bugs can you kill? [Smash that bug] " + link);
                    //startActivity(emailIntent);
                    ((Activity)context).startActivity(Intent.createChooser(sharingIntent, "Share your score"));
                } else
                    whereiam_goto("home");

            }

        }
    }

    private void touchEventHelp(MotionEvent event){
        //daca apasa pe home
        if(event.getY() > constante.yHomeButtonOffset && event.getY() < constante.yHomeButtonOffset + constante.yHomeButton
                && event.getX() > constante.xHomeButtonOffset && event.getX() < constante.xHomeButtonOffset + constante.xHomeButton)
            whereiam_goto("home");
    }
    private void touchEventScores(MotionEvent event){
        //daca apasa pe home
        if(event.getY() > constante.yHomeButtonOffset && event.getY() < constante.yHomeButtonOffset + constante.yHomeButton
                && event.getX() > constante.xHomeButtonOffset && event.getX() < constante.xHomeButtonOffset + constante.xHomeButton)
            whereiam_goto("home");
    }
    private  void save_dificulty(int dif){
        if(dif != difficulty){
            this.difficulty = dif;
            constante.setDifficulty(dif);
            save_config_to_file();
        }
    }
    private void touchEventSettings(MotionEvent event){
        //butoane pt setat dificultatea
        if(event.getX() > constante.xSettingsBabyOffset && event.getX() < constante.xSettingsBabyOffset + constante.xSettingsButton
            && event.getY() > constante.ySettingsBabyOffset && event.getY() < constante.ySettingsBabyOffset + constante.ySettingsButton) {
                save_dificulty(0);
        }
        if(event.getX() > constante.xSettingsEasyOffset && event.getX() < constante.xSettingsEasyOffset + constante.xSettingsButton
                && event.getY() > constante.ySettingsEasyOffset && event.getY() < constante.ySettingsEasyOffset + constante.ySettingsButton) {
            save_dificulty(1);
        }
        if(event.getX() > constante.xSettingsMediumOffset && event.getX() < constante.xSettingsMediumOffset + constante.xSettingsButton
                && event.getY() > constante.ySettingsMediumOffset && event.getY() < constante.ySettingsMediumOffset + constante.ySettingsButton) {
            save_dificulty(2);
        }
        if(event.getX() > constante.xSettingsHardOffset && event.getX() < constante.xSettingsHardOffset + constante.xSettingsButton
                && event.getY() > constante.ySettingsHardOffset && event.getY() < constante.ySettingsHardOffset + constante.ySettingsButton) {
            save_dificulty(3);
        }

        //bugs show to smash

        //bugs show not to smash

        //buton save

        //daca apasa pe home
        if(event.getY() > constante.yHomeButtonOffset && event.getY() < constante.yHomeButtonOffset + constante.yHomeButton
                && event.getX() > constante.xHomeButtonOffset && event.getX() < constante.xHomeButtonOffset + constante.xHomeButton)
            whereiam_goto("home");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(whereIAm.equals("home")){
                touchEventHome(event);
            } else if(whereIAm.equals("help")){
                touchEventHelp(event);
            } else if(whereIAm.equals("settings")){
                touchEventSettings(event);
            } else if(whereIAm.equals("scoreboard")){
                touchEventScores(event);
            } else if(whereIAm.equals("playing")){
                 touchEventPlaying(event);
            }
        }
        return true;
        //return super.onTouchEvent(event);
    }

    @Override
    public void onDraw(Canvas canvas){
        //white background
        if(whereIAm.equals("home")){
            drawHome(canvas);
        } else if(whereIAm.equals("help")){
            drawHelp(canvas);
        } else if(whereIAm.equals("settings")){
            drawSettings(canvas);
        } else if(whereIAm.equals("scoreboard")){
            drawScoreboard(canvas);
        } else if(whereIAm.equals("playing")){
            drawPlaying(canvas);
        }

        //todo delete this dupa ce termin cu logurile
        /*
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(12);
        canvas.drawText(log, 10, 50,paint);
        canvas.drawText(log2, 10,100,paint);
        /**/
    }

    public void drawHome(Canvas canvas){
        //desenam doar o imagine (home screen)
        canvas.drawBitmap(start_screen,0,0, null);

        //desenam butoanele
        canvas.drawBitmap(btn_home_play, constante.xHomeButtonPlay, constante.yHomeButtonPlay, null );
        canvas.drawBitmap(btn_home_help, constante.xHomeButtonHelp, constante.yHomeButtonHelp, null );
        canvas.drawBitmap(btn_home_scores, constante.xHomeButtonScores, constante.yHomeButtonScores, null );
        canvas.drawBitmap(btn_home_settings, constante.xHomeButtonSettings, constante.yHomeButtonSettings, null );

        //OLD
        //bg alb
        /*
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        //logo
        canvas.drawBitmap(logo, (getWidth() - logo.getWidth() ) / 2, (getHeight() / 2 - logo.getHeight()) / 2, null);

        paint.setAntiAlias(false);
        paint.setColor(Color.BLACK);
        //button play
        canvas.drawRect(
                constante.xHomeButtonOffset,
                constante.yHomeButtonOffset_play,
                constante.xHomeButtonOffset + constante.xHomeButton,
                constante.yHomeButtonOffset_play + constante.yHomeButton,
                paint);
        //button scoreboard
        canvas.drawRect(
                constante.xHomeButtonOffset,
                constante.yHomeButtonOffset_scoreboard,
                constante.xHomeButtonOffset + constante.xHomeButton,
                constante.yHomeButtonOffset_scoreboard + constante.yHomeButton,
                paint);
        //button options
        canvas.drawRect(
                constante.xHomeButtonOffset,
                constante.yHomeButtonOffset_settings,
                constante.xHomeButtonOffset + constante.xHomeButton,
                constante.yHomeButtonOffset_settings + constante.yHomeButton,
                paint);
        //button help
        canvas.drawRect(
                constante.xHomeButtonOffset,
                constante.yHomeButtonOffset_help,
                constante.xHomeButtonOffset + constante.xHomeButton,
                constante.yHomeButtonOffset_help + constante.yHomeButton,
                paint);

        //text
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("PLAY", constante.xHomeButtonOffset + constante.xHomeButton / 2, 7 + constante.yHomeButtonOffset_play + constante.yHomeButton / 2, paint);
        canvas.drawText("SCOREBOARD", constante.xHomeButtonOffset + constante.xHomeButton / 2, 7 + constante.yHomeButtonOffset_scoreboard + constante.yHomeButton / 2, paint);
        canvas.drawText("SETTINGS", constante.xHomeButtonOffset + constante.xHomeButton / 2, 7 + constante.yHomeButtonOffset_settings + constante.yHomeButton / 2, paint);
        canvas.drawText("HELP",         constante.xHomeButtonOffset + constante.xHomeButton / 2 , 7 + constante.yHomeButtonOffset_help + constante.yHomeButton / 2, paint);
        */
    }

    public void drawPlaying(Canvas canvas){
        canvas.drawBitmap(background, 0, 0, null);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        //paint.setColor(Color.WHITE);
        //canvas.drawPaint(paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(constante.fontSizeMare);
        canvas.drawText(String.valueOf(score), constante.PauseButtonOffsetMargin, constante.PauseButtonOffsetMargin + constante.fontSizeMare, paint);

        /*
        canvas.drawText("Level:" + (max_level - 1), 120, 20,paint);
        canvas.drawLine(0,30,300,30,paint);
        */


        //log = "x: " + getWidth() + " y: " + getHeight();
        //paint.setTextSize(12);
        //canvas.drawText(log, 10, 50,paint);
        //canvas.drawText(log2, 10,100,paint);

        for(Bug bug : bugs)
            if(bug.isActive() && !bug.isOutside())
                bug.draw(canvas);

        //btn pauza
        if(!pause){
            canvas.drawBitmap(btn_pauza, getWidth() - btn_pauza.getWidth() - constante.PauseButtonOffsetMargin, constante.PauseButtonOffsetMargin, null);
        }
        if(show_alert){
            drawAlert(canvas);
        }
    }

    public void drawAlert(Canvas canvas){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setAlpha(100);

        //log = "fromx: " + fromX + " fromy: " + fromY;
        //log2 = "x: " + getWidth() + " y: " + getHeight();
        canvas.drawRect(constante.xAlertOffset, constante.yAlertOffset, constante.xAlertOffset + constante.xAlert, constante.yAlertOffset + constante.yAlert, paint);
         //Log.d(TAG, "xAllertOffset:" + constante.xAlertOffset + "xAllert: " + constante.xAlert + " x:" + getWidth());
        int fromX = constante.xAlertOffset;
        int fromY = constante.yAlertOffset;

        paint.setColor(Color.WHITE);
        paint.setAlpha(250);
        paint.setAntiAlias(true);
        Bitmap btn1 = null;
        Bitmap btn2 = null;
        if(alert_type.equals("dead")){
            paint.setTextSize(constante.fontSizeMare);
            canvas.drawText("Your score: " + String.valueOf(score), fromX + constante.AlertMargin, fromY + constante.AlertMargin, paint);

            paint.setTextSize(constante.fontSizeMic);
            String all_msg = get_mesaj_when_dead(id_bug_smash);
            String[] msg_split = all_msg.split("#");
            if(msg_split.length > 0)
                canvas.drawText(msg_split[0], fromX + constante.AlertMargin, fromY + constante.AlertMargin + constante.fontSizeMic + 2 * constante.yIntreRanduri, paint);
            if(msg_split.length > 1)
                canvas.drawText(msg_split[1], fromX + constante.AlertMargin, fromY + constante.AlertMargin + 2 * constante.fontSizeMic + 3 * constante.yIntreRanduri, paint);
            /*punem 3 randuri?
            if(msg_split.length > 2)
                canvas.drawText(msg_split[2], fromX + 20, fromY + 130, paint);
            */
            //cele 2 butoane.
            //try again
            btn1 = btn_try_again; //BitmapFactory.decodeResource(getResources(), R.drawable.btn_try_again);
            btn2 = btn_share; //BitmapFactory.decodeResource(getResources(), R.drawable.btn_home);
            //todo publish pe facebook

        } else if(alert_type.equals("pause")){
            paint.setTextSize(constante.fontSizeXMare);
            canvas.drawText("Taking a break...", fromX + constante.AlertMargin, fromY + 3 * constante.AlertMargin, paint);
            btn1 = btn_resume; //BitmapFactory.decodeResource(getResources(), R.drawable.btn_resume);
            btn2 = btn_home; //BitmapFactory.decodeResource(getResources(), R.drawable.btn_home);
        } else if(alert_type.equals("first_time")){
            //maybe poza cu explicatie
            paint.setTextSize(constante.fontSizeXMare);
            canvas.drawText("Pay attention!", fromX + constante.AlertMargin, fromY + constante.AlertMargin, paint);
            paint.setTextSize(constante.fontSizeMic);
            canvas.drawText("Do NOT kill the ladybirds, bees and butterflies", fromX + constante.AlertMargin, fromY + constante.AlertMargin + constante.fontSizeMic + constante.yIntreRanduri, paint);

            canvas.drawBitmap(bug50, constante.xAlertBug1Offset, constante.yAlertBug1Offset, null);
            canvas.drawBitmap(bug51, constante.xAlertBug2Offset, constante.yAlertBug2Offset, null);
            canvas.drawBitmap(bug52, constante.xAlertBug3Offset, constante.yAlertBug3Offset, null);

            btn1 = btn_play; //BitmapFactory.decodeResource(getResources(), R.drawable.btn_play);
            btn2 = btn_help;    //BitmapFactory.decodeResource(getResources(), R.drawable.btn_help);
        }
        //draw cele 2 butoane
        if(btn1 != null)
            canvas.drawBitmap(btn1, constante.xAlertButton1Offset, constante.yAlertButton1Offset, null);
        if(btn2 != null)
            canvas.drawBitmap(btn2, constante.xAlertButton2Offset, constante.yAlertButton2Offset, null);
    }

    private void drawHelp(Canvas canvas){
        /*Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        */
        canvas.drawBitmap(background_help,0,0, null);

        //buton cu home jos
        canvas.drawBitmap(btn_home_home, constante.xHomeButtonOffset, constante.yHomeButtonOffset, null);
    }
    public void drawSettings(Canvas canvas){
        canvas.drawBitmap(background_settings,0,0, null);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(constante.fontSizeXMare);
        canvas.drawText("Difficulty", constante.xSettingsDifficultyText, constante.ySettingsDifficultyText , paint);

        if(difficulty == 0)
            canvas.drawBitmap(btn_settings_baby_s, constante.xSettingsBabyOffset, constante.ySettingsBabyOffset, null );
        else
            canvas.drawBitmap(btn_settings_baby_us, constante.xSettingsBabyOffset, constante.ySettingsBabyOffset, null );
        if(difficulty == 1)
            canvas.drawBitmap(btn_settings_easy_s, constante.xSettingsEasyOffset, constante.ySettingsEasyOffset, null );
        else
            canvas.drawBitmap(btn_settings_easy_us, constante.xSettingsEasyOffset, constante.ySettingsEasyOffset, null );
        if(difficulty == 2)
            canvas.drawBitmap(btn_settings_medium_s, constante.xSettingsMediumOffset, constante.ySettingsMediumOffset, null );
        else
            canvas.drawBitmap(btn_settings_medium_us, constante.xSettingsMediumOffset, constante.ySettingsMediumOffset, null );
        if(difficulty == 3)
            canvas.drawBitmap(btn_settings_hard_s, constante.xSettingsHardOffset, constante.ySettingsHardOffset, null );
        else
            canvas.drawBitmap(btn_settings_hard_us, constante.xSettingsHardOffset, constante.ySettingsHardOffset, null );

        //buton cu home jos
        canvas.drawBitmap(btn_home_home, constante.xHomeButtonOffset, constante.yHomeButtonOffset, null);
    }
    public void drawScoreboard(Canvas canvas){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        /*
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        */
        canvas.drawBitmap(background_scores,0,0, null);
        //pe rand fiecare scor
        //int offsetY = 50;
        int nr = 0;

        paint.setColor(Color.BLACK);

        paint.setAntiAlias(true);
        
        //scoreboard.read_scoreboard();
        for(Object o : scoreboard.getTop()){
            Scoreboard_entry se = (Scoreboard_entry)o;
            paint.setTextSize(constante.fontSizeXMare);
            canvas.drawText((nr + 1) + ".", constante.xScoresNrOffset, constante.yScoresOffset +  constante.yScoresPas * nr, paint);
            canvas.drawText(se.getScore() + "", constante.xScoresScoreOffset, constante.yScoresOffset +  constante.yScoresPas * nr, paint);
            paint.setTextSize(constante.fontSizeMare);
            canvas.drawText("[" + se.getDate() + "]", constante.xScoresDateOffset, constante.yScoresOffset +  constante.yScoresPas * nr, paint);
            nr++;
        }

        //buton cu home jos
        canvas.drawBitmap(btn_home_home, constante.xHomeButtonOffset, constante.yHomeButtonOffset, null);
    }
    
    int id_mesaj_dead = -1; 
    private String get_mesaj_when_dead(int id_gaza){
        String msg = "";
        if(id_mesaj_dead == -1)
            id_mesaj_dead = rand.nextInt(2);
        switch (id_gaza){
            case 50:
                switch (id_mesaj_dead){
                    case 0:
                        msg =  "Why you smash the lady bird?#She is beautiful";
                        break;
                    case 1:
                    default:
                        msg = "You don't have to kill the lady birds";
                        break;
                }

            break;
            case 51:
                switch (id_mesaj_dead){
                    case 0:
                        msg =  "You don't like honey?#Or why you smash that bee?";
                        break;
                    case 1:
                        msg = "That’s not how you make honey.";
                        break;
                    case 2:
                    default:
                        msg = "The other bees won’t be so happy.#They are coming after you.";
                        break;
                }
            break;
            case  52:
                switch (id_mesaj_dead){
                    case 0:
                        msg = "Butterflys are beatiful#You don't have to smash them";
                        break;
                    case 1:
                    default:
                        msg = "Smashing the butterfly... #not good... not good... dude";
                        break;
                }
            break;
            default:
                msg = "You smash the wrong bug";
            break;
        }
        return msg;
    }

    public void update(){
        if(playing && alive && !pause){
            int nr_bugs_alive = 0;
            //log = "";
            for(Bug bug : bugs){
                if(bug.isActive()){
                    if(bug.isOutside()){
                        //daca o intrat inauntru
                        if(bug.getX() > 0 && bug.getX() < getWidth() && bug.getY() > 0 && bug.getY() < getHeight()){
                            bug.setOutside(false);
                            //log2 += bug.getId() + "IN; ";
                        }
                        if(bug_exit_screen(bug, 50)) {
                            //log2 += bug.getId() + "REMOVE100; ";
                            bugs.remove(bug);
                        }
                    } else {
                        if(bug_exit_screen(bug, constante.offsetOut + 5)) {
                            //log2 += bug.getId() + "REMOVE10; ";
                            bugs.remove(bug);
                        }
                    }

                    bug.update();
                    nr_bugs_alive ++;
                } else {
                    //log2 += bug.getId() + "REMOVE; ";
                    bugs.remove(bug);
                }
                //log += bug.getId() + " " + bug.getX() + "x" + bug.getY() + "xd:" + bug.getSpeed().getxDirection() + "yd:" + bug.getSpeed().getyDirection() + "O:" + bug.isOutside()+"|";
                //log += bug.getId() + " " + bug.getX() + "x" + bug.getY() + "O:" + bug.isOutside()+"|";
                /*if(bug.getId() == 50)
                    log2 = bug.getId() + " a:" + bug.getAngle() + " s:" + bug.getSin() + "|";
                else
                    log = bug.getId() + " a:" + bug.getAngle() + " s:" + bug.getSin() + "|";
                */
            }

            recalculate_max_bugs_on_screen();

            int nr_bugs_to_create = MAX_BUGS_ON_SCREEN - nr_bugs_alive;
            if(nr_bugs_to_create > 0){
                //increase max level daca e casul
                //log += "\nmax: "  + MAX_BUGS_ON_SCREEN + " nr_to_Create: " + nr_bugs_to_create;

                for(int i=0;i<nr_bugs_to_create;i++){
                    add_new_bug(0); //create new bug
                }
            }
        }
    }

    private void recalculate_max_bugs_on_screen(){
        MAX_BUGS_ON_SCREEN = 3;
        if(score > 5){
            MAX_BUGS_ON_SCREEN = 5;
            if(score < 10){
                maxIdGazaBuna = 1;
                maxIdGazaRea = 1;
            } else if(score < 30){
                maxIdGazaBuna = 1;
                maxIdGazaRea = 2;
                MAX_BUGS_ON_SCREEN = score / 8;
            } else if(score < 100){
                maxIdGazaBuna = 2;
                maxIdGazaRea = 2;
                MAX_BUGS_ON_SCREEN = (int) (score / 7);
            } else if(score < 150){
                maxIdGazaBuna = 2;
                maxIdGazaRea = 3;
               // MAX_BUGS_ON_SCREEN = score / 5;
            } else if(score < 300){
                maxIdGazaBuna = 3;
                maxIdGazaRea = 3;
              //  MAX_BUGS_ON_SCREEN = (int) (score / 5.5);
            } else if(score < 600){
                maxIdGazaBuna = 3;
                maxIdGazaRea = 4;
             //   MAX_BUGS_ON_SCREEN = score / 6;
            } else if(score > 1000){
                maxIdGazaBuna = 3;
                maxIdGazaRea = 5;
              //  MAX_BUGS_ON_SCREEN = score / 10;
            }
            /*
            if(score > 150){
                MAX_BUGS_ON_SCREEN = score / 10;
            }
            if(MAX_BUGS_ON_SCREEN < 4)
                MAX_BUGS_ON_SCREEN = 4;
            */
        }
        MAX_BUGS_ON_SCREEN = (int)(2 * ( Math.log(score) / Math.log(2)));
        if(MAX_BUGS_ON_SCREEN < 4)
            MAX_BUGS_ON_SCREEN = 4;
    }

    private int next_bug_id(){
        int nr_bugs_boss = 0;
        int nr_bugs_kill = 0;
        for(Bug bug : bugs){
            if(bug.isActive() && !bug.can_be_touch())
                nr_bugs_boss++;
            else
                nr_bugs_kill++;
        }

        if((nr_bugs_boss < MAX_BUGS_ON_SCREEN / constante.bug_proportion) && (this.difficulty > 0) && (nr_bugs_kill > 0)){
            //o gaza care NU poate fi omorata (50/51/52, depinde de level cu random intre)
            return 50 + rand.nextInt(maxIdGazaBuna);
        } else {
            //o gaza care poate fi omorata (1/2/3/4/5/6, depinde de level cu random intre)
            return 1 + rand.nextInt(maxIdGazaRea);
        }
    }

    public void add_new_bug(int id){
        int offsetOut = 40;
        int rand_nr = rand.nextInt(4);
        int posX = constante.getNewXForBug(rand_nr);
        int posY = constante.getNewYForBug(rand_nr);

        /* mutat in constane.java toata astea
        switch (rand.nextInt(4)){
            case 0:
                posX = 100 + rand.nextInt(getWidth() - 200);
                posY = 0 - offsetOut;
            break;
            case 1:
                posX = 0 - offsetOut;
                posY = 200 + rand.nextInt(getHeight() - 400);
            break;
            case 2:
                posX = 100 + rand.nextInt(getWidth() - 200);
                posY = getHeight() + offsetOut;
            break;
            case 3:
                posX = getWidth() + offsetOut;
                posY = 200 + rand.nextInt(getHeight() - 400);
            break;
        }
        */
        //posX = 100;
        //posY = 200;

        if(id == 0)
            id = next_bug_id();

        Bug new_bug = new Bug(getBitmapByLevel(id), posX, posY, id);
        new_bug.setSpeedBoost(constante.bug_speed);
        bugs.add(new_bug);
    }

    private boolean bug_exit_screen(Bug bug, int marja){
        //log += "?" + marja + "-> " + bug.getX() + "x" + bug.getY() + "(" + getWidth() + "x" + getHeight() + ");" ;
        if(bug.getX() < 0 - marja || bug.getX() > getWidth() + marja || bug.getY() < 0 - marja  || bug.getY() > getHeight() + marja){
            return true;
        }
        return false;
    }

    public MainThread getThread() {
        return thread;
    }

    public String getWhereIAm() {
        return whereIAm;
    }

    public void setWhereIAm(String whereIAm) {
        this.whereIAm = whereIAm;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isShow_alert() {
        return show_alert;
    }

    public void setShow_alert(boolean show_alert) {
        this.show_alert = show_alert;
    }

    public String getAlert_type() {
        return alert_type;
    }

    public void setAlert_type(String alert_type) {
        this.alert_type = alert_type;
    }

    public boolean isFirst_time() {
        return first_time;
    }

    public void setFirst_time(boolean first_time) {
        this.first_time = first_time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBugs_kill() {
        return bugs_kill;
    }

    public void setBugs_kill(int bugs_kill) {
        this.bugs_kill = bugs_kill;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public static String getMD5(String pass) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] data = pass.getBytes();
            m.update(data,0,data.length);
            BigInteger i = new BigInteger(1,m.digest());
            return String.format("%1$032X", i);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }
}