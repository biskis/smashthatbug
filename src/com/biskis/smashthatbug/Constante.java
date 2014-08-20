package com.biskis.smashthatbug;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Dani Dudas
 * Date: 11/20/11
 * Time: 4:12 PM
 */
public class Constante {
    //public static final int MaxX = 320;
    //public static final int MaxY = 533;
/*
    //dimensiuni home button
    public static final int xHomeButton = 200;
    public static final int yHomeButton = 50;

    //offset from top home buttons
    public static final int yHomeLogo = 50;
    public static final int yHomePlay = 200;
    public static final int yHomeScoreboard = 280;
    public static final int yHomeOptions = 360;
    public static final int yHomeHelp = 440;
*/
    //offset x,y Alert
    //public static final int xAlert = 500;
    //public static final int yAlert = 630;

    //velocity amplifier
    public static final float velocityAmplifier = 5.0f;

    private int w;
    private int h;
    private int dpiClassification;
    private int xHomeButtonProcent = 80;
    private int difficulty;
    public float bug_speed;
    public float bug_proportion;
//HOME BUTTONS
    public int xHomeButton = 145;
    public int yHomeButton = 80;
    public int xHomeButtonPlay;
    public int yHomeButtonPlay;
    public int xHomeButtonHelp;
    public int yHomeButtonHelp;
    public int xHomeButtonScores;
    public int yHomeButtonScores;
    public int xHomeButtonSettings;
    public int yHomeButtonSettings;
    /* OLD
    public int xHomeButton;
    public int yHomeButton;
    public int yHomeButtonOffset_play;
    public int yHomeButtonOffset_scoreboard;
    public int yHomeButtonOffset_settings;
    public int yHomeButtonOffset_help;
    */
//HOME BUTTON SETTINGS & HELP & SCOREBOARD
    //public int xToHomeButtonOffset ;
    //public int yToHomeButtonOffsetDown;
    public int xHomeButtonOffset;
    public int yHomeButtonOffset;

//ALERT
    public int xAlert;
    public int yAlert;
    public int xAlertOffset;
    public int yAlertOffset;
    public int AlertMargin;
    public int xAlertButton;
    public int yAlertButton;
    public int xAlertButton1Offset;
    public int xAlertButton2Offset;
    public int yAlertButton1Offset;
    public int yAlertButton2Offset;
    
    public int xAlertBug1Offset;
    public int yAlertBug1Offset;
    public int xAlertBug2Offset;
    public int yAlertBug2Offset;
    public int xAlertBug3Offset;
    public int yAlertBug3Offset;
    
    public int fontSizeMic = 14;
    public int fontSizeMare = 20;
    public int yIntreRanduri = 2;

//NEW BUG POZITION
    private int newBugPozXfrom;
    private int newBugPozXto;
    private int newBugPozYfrom;
    private int newBugPozYto;
    private Random rand = new Random();
    public int offsetOut;

//PAUSE BUTTON
    public int PauseButtonOffsetMargin;

//SCORE BOARD POSITION
    public int xScoresNrOffset;
    public int xScoresScoreOffset;
    public int xScoresDateOffset;
    public int yScoresOffset;
    public int yScoresPas;
    public int fontSizeXMare;

//SETTINGS BTN POSITION
    public int xSettingsButton;
    public int ySettingsButton;
    public int xSettingsBabyOffset;
    public int ySettingsBabyOffset;
    public int xSettingsEasyOffset;
    public int ySettingsEasyOffset;
    public int xSettingsMediumOffset;
    public int ySettingsMediumOffset;
    public int xSettingsHardOffset;
    public int ySettingsHardOffset;
    public int xSettingsDifficultyText;
    public int ySettingsDifficultyText;

    public Constante(int w, int h, int dpiClassification, int dif){
        this.w = w;
        this.h = h;
        this.dpiClassification = dpiClassification;

        this.setDifficulty(dif);
        
        //HOME BUTTONS
        int x_margini = w/9;
        this.xHomeButton = x_margini * 3;
        this.yHomeButton = (int) (this.xHomeButton / 1.9333);
        int bucata_de_jos = h/2 - h/10;
        int spatiu_intre_butoane = (bucata_de_jos - 2 * this.yHomeButton) /3;
        int offset_de_sus = h/2 + h/10;
        this.yHomeButtonPlay = offset_de_sus + spatiu_intre_butoane;
        this.yHomeButtonHelp = offset_de_sus + spatiu_intre_butoane;
        this.yHomeButtonScores = offset_de_sus + spatiu_intre_butoane + this.yHomeButton + spatiu_intre_butoane;
        this.yHomeButtonSettings = offset_de_sus + spatiu_intre_butoane + this.yHomeButton + spatiu_intre_butoane;

        this.xHomeButtonPlay = x_margini;
        this.xHomeButtonHelp = x_margini + this.xHomeButton + x_margini;
        this.xHomeButtonScores = x_margini;
        this.xHomeButtonSettings = x_margini + this.xHomeButton + x_margini;

        /* OLD
            int pas = h / 24 ;  //h/2 (jumatatea de jos) // impartit la 12 (2- butonu, 1 pauza)
            int first_offset = h /2;
            //Y OFFSET
            this.yHomeButtonOffset_play = first_offset;
            this.yHomeButtonOffset_scoreboard = first_offset + 3 * pas;
            this.yHomeButtonOffset_settings = first_offset + 6 * pas;
            this.yHomeButtonOffset_help = first_offset + 9 * pas;

            this.yHomeButton = 2 * pas;
            this.xHomeButton = w * 8 / 10;


        */

        //ALERT
        this.xAlert = w; //w * 80 / 100;     //80%
        this.xAlertOffset = 0; //w  / 10;    //10%
        this.yAlert = h /2;
        this.yAlertOffset = h / 4;

        this.AlertMargin = this.xAlert / 10;
        this.xAlertButton = (this.xAlert - 3 * this.AlertMargin ) /2;
        this.yAlertButton = this.xAlertButton / 2;
        this.xAlertButton1Offset = this.xAlertOffset + this.AlertMargin;                //din dreapta
        this.xAlertButton2Offset = this.xAlertOffset + this.xAlert - this.xAlertButton - this.AlertMargin;  //din stanga
        this.yAlertButton1Offset = this.yAlertOffset + this.yAlert - this.yAlertButton - this.AlertMargin;  //de jos in sus
        this.yAlertButton2Offset = this.yAlertButton1Offset;

        int defaultBugX = 25;
        //marime font in functie de dpi ecran
        switch (dpiClassification){
            case 120:   //ldpi
                this.fontSizeMic = 10;
                this.fontSizeMare = 13;
                this.fontSizeXMare = 16;
                defaultBugX = 33;
                this.offsetOut = 33;
                break;
            case 240:   //hdpi
                this.fontSizeMic = 20;
                this.fontSizeMare = 26;
                this.fontSizeXMare = 32;
                defaultBugX = 70;
                this.offsetOut = 70;
                break;
            case 320:   //xhdpi
                this.fontSizeMic = 23;
                this.fontSizeMare = 34;
                this.fontSizeXMare = 42;
                defaultBugX = 90;
                this.offsetOut = 90;
                break;
            case 160:   //mdpi
            default:
                this.fontSizeMic = 13;
                this.fontSizeMare = 17;
                this.fontSizeXMare = 21;
                defaultBugX = 45;
                this.offsetOut = 45;
                break;
        }

        this.yIntreRanduri =  this.fontSizeMic;

                //Pozitii bugs alert initial
                this.yAlertBug1Offset = this.yAlertBug2Offset = this.yAlertBug3Offset = this.yAlertOffset + 2 * this.AlertMargin + 2 * this.fontSizeMare;
        this.xAlertBug1Offset = this.AlertMargin;
        this.xAlertBug2Offset = this.xAlert / 2 - defaultBugX /2;
        this.xAlertBug3Offset = w - this.AlertMargin - defaultBugX;

        //HOME BUTTON SETTINGS & HELP & SCOREBOARD
        this.xHomeButtonOffset = (w - this.xHomeButton) / 2;
        this.yHomeButtonOffset = h - this.AlertMargin - this.yHomeButton;
        //this.xToHomeButtonOffset = 20;
        //this.yToHomeButtonOffsetDown = 20;

        //PAUSE BUTTON
        this.PauseButtonOffsetMargin = h / 80;

        //SCORE POSITION
        this.yScoresPas = h / 8;
        this.yScoresOffset = (int) (this.yScoresPas * 1.8);
        this.xScoresNrOffset = this.AlertMargin;
        this.xScoresScoreOffset = 3 * this.AlertMargin;
        this.xScoresDateOffset = 5 * this.AlertMargin;

        //SETTINGS BUTTONS POSITIONS
        this.xSettingsDifficultyText = this.AlertMargin;
        this.ySettingsDifficultyText = this.yScoresOffset;
        this.xSettingsButton = this.xHomeButton;
        this.ySettingsButton = this.yHomeButton;
        this.xSettingsBabyOffset = this.xHomeButtonPlay;
        this.xSettingsEasyOffset = this.xHomeButtonHelp;
        this.xSettingsMediumOffset = this.xHomeButtonScores;
        this.xSettingsHardOffset = this.xHomeButtonSettings;
        this.ySettingsBabyOffset = this.ySettingsEasyOffset = this.ySettingsDifficultyText + this.fontSizeXMare;
        this.ySettingsMediumOffset = this.ySettingsHardOffset = this.ySettingsBabyOffset + this.ySettingsButton + this.fontSizeXMare;


        //care e x from si x to unde pot aparea bug's )si y
        this.newBugPozXfrom = w/4;
        this.newBugPozXto = w * 3 / 4;
        this.newBugPozYfrom = h/4;
        this.newBugPozYto = h * 3 / 4;
        offsetOut = 20;
    }
    

    public int getNewXForBug(int parte){
        switch (parte){
            case 0:
                return this.newBugPozXfrom + rand.nextInt(this.newBugPozXto - this.newBugPozXfrom);
            case 1:
                return 0 - this.offsetOut;
            case 2:
                return this.newBugPozXfrom + rand.nextInt(this.newBugPozXto - this.newBugPozXfrom);                
            case 3:
                return w + offsetOut;
        }
        return 0;
    }
    public int getNewYForBug(int parte){
        switch (parte){
            case 0:
                return 0 - this.offsetOut;
            case 1:
                return this.newBugPozYfrom + rand.nextInt(this.newBugPozYto - this.newBugPozYfrom);
            case 2:
                return this.h + this.offsetOut;
            case 3:
                return this.newBugPozYfrom + rand.nextInt(this.newBugPozYto - this.newBugPozYfrom);
        }
        return 20;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        switch (difficulty){
            case 0:
            case 1:
                this.bug_speed = 1;
                this.bug_proportion = 3;
                break;
            case 3:
                this.bug_speed = 10;
                this.bug_proportion = 1.3f;
                break;
            case 2:
            default:
                this.bug_speed = 4;
                this.bug_proportion = 2;
                break;
        }
    }
}
