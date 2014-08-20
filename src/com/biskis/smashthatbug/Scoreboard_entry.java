package com.biskis.smashthatbug;

//import android.content.res.ObbInfo;

/**
 * Created by IntelliJ IDEA.
 * User: Dani Dudas
 * Date: 12/21/11
 * Time: 4:44 PM
 */
public class Scoreboard_entry {
    private int score;
    private String date;
    private String difficulty;

    public Scoreboard_entry(){
        score = 0;
    }
    public Scoreboard_entry(int score, String date, String difficulty) {
        this.score = score;
        this.date = date;
        this.difficulty = difficulty;
    }
    
    public void set(Scoreboard_entry se){
        this.score = se.score;
        this.date = se.date;
        this.difficulty = se.difficulty;
    }

    public int compareTo(Object obj){
        Scoreboard_entry scoreboard_entry = (Scoreboard_entry) obj;
        if(this.score < scoreboard_entry.score)
            return  -1;
        if(this.score > scoreboard_entry.score)
            return  1;
        return 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public String toString(){
        return "score: " + this.score + " dificulty: " + this.difficulty + " data: " +  this.date;
    }
}
