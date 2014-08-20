package com.biskis.smashthatbug;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Dani Dudas
 * Date: 12/21/11
 * Time: 4:43 PM
 */
public class Scoreboard {
    private int SCOREBOARD_NR_ENTRYS = 5;
    private String SCOREBOARD_FILENAME = "smastthatbug_scoreboard";
    private static final String TAG = MyActivity.class.getSimpleName();

    private ArrayList<Scoreboard_entry> top;
    private Context context;

    public Scoreboard(Context context){
        top = new ArrayList<Scoreboard_entry>();
        this.context = context;
    }

    public void add(Scoreboard_entry se){
        Log.d(TAG, se.toString() );
        if(top.isEmpty())
            read_scoreboard();
        top.add(se);
        //ordonam
        for(int i=0; i<top.size()-1; i++)
            for(int j=i+1;j<top.size();j++){
                if(top.get(i).getScore() < top.get(j).getScore()){
                    Scoreboard_entry aux = new Scoreboard_entry();
                    aux.set(top.get(i));
                    top.get(i).set(top.get(j));
                    top.get(j).set(aux);
                }
            }
        //delete cele mai slabe rezultate
        if(top.size() >= SCOREBOARD_NR_ENTRYS){
            int nr = 0;
            int n= top.size();
            for(int i=0;i<n;i++) {
                if(nr >= SCOREBOARD_NR_ENTRYS){
                    top.remove(i);
                    i--;
                    n--;
                }
                nr++;
            }
        }

        save_to_file_scoreboard();
    }

    public void read_scoreboard(){
        top.clear();
        FileInputStream fin = null;
        byte[] config_bytes = new byte[100];
        String scoreboard_file = null;

        try {
            fin = context.openFileInput(SCOREBOARD_FILENAME);
            fin.read(config_bytes);
            fin.close();
            scoreboard_file = new String(config_bytes);
            Log.d(TAG, "### SCOREBOARD FILE [read]:");
            Log.d(TAG, scoreboard_file);
            //parsam scoreboardul

            String[] config_split = scoreboard_file.split("#");
            for(String c_split : config_split){
                if(c_split.length() > 5){
                    String[] c = c_split.split("%");
                    if(c.length >= 3){
                        //c[0] - scor, c[1] - data, c[2] -difucltatea
                        top.add(new Scoreboard_entry(Integer.valueOf(c[0]),c[1],c[2]));
                    }
                }
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private void save_to_file_scoreboard(){
        //Collections.sort(top);
        FileOutputStream out = null;
        try {
            out = context.openFileOutput(SCOREBOARD_FILENAME, Context.MODE_PRIVATE);
            int nr = 0;
            Log.d(TAG, "### SCOREBOARD FILE [write]:");
            for(Object s : top){
                if(nr < SCOREBOARD_NR_ENTRYS){
                    Scoreboard_entry se = (Scoreboard_entry) s;
                    String to_write = se.getScore() + "%" + se.getDate() + "%" + se.getDifficulty() + "#";
                    out.write(to_write.getBytes());
                    nr++;
                    Log.d(TAG, to_write);
                }
            }
            out.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    public ArrayList getTop() {
        return top;
    }

    public void setTop(ArrayList top) {
        this.top = top;
    }
}
