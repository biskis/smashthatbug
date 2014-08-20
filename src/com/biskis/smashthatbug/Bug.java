package com.biskis.smashthatbug;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Dani Dudas
 * Date: 11/17/11
 * Time: 10:09 PM
 */
public class Bug {
    private Bitmap bitmap;
    private float x;
    private float y;
    private boolean isActive;
    Speed speed;
    private boolean isOutside;
    private boolean can_be_touch;
    private int id;
    private int score;
    //todo del this after modificari
    private boolean touched;
    private int level;
    private int lastUpdate;
    private float angle;
    private double sin;
    private float speedBoost;


    public Bug(Bitmap bitmap, int x, int y, int id){
        Random rand = new Random();

        this.x = x;
        this.y = y;
        this.isOutside = false;
        this.isActive = true;

        this.id = id;
        if(id < 50)
            can_be_touch = true;
        else
            can_be_touch = false;

        this.score = id;

        //todo dani viteza minima si max in functie de id gaza
        this.speed = new Speed(x,y,min_speed(),max_speed());

        //bitmap in functie de unghi

        //this.bitmap = bitmap;

        Matrix mtx = new Matrix();
        mtx.postRotate(get_angle());
        this.bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);


        //this.level = level;
        //this.lastUpdate = 80 + rand.nextInt(40);    //intre 80 -> 120;

        /*
        //set speed.
        //viteza X

        float viteza = 0.2f + (rand.nextInt(level) + rand.nextFloat()) * 0.7f;
        speed.setXv(viteza * 2);
        //viteza Y
        viteza = 0.2f + (rand.nextInt(level) + rand.nextFloat()) * 0.7f;
        speed.setYv(viteza * 2);


        //set direction
        if(this.x > 0)
            speed.setxDirection(Speed.DIRECTION_LEFT);
        if(this.y > 0)
            speed.setyDirection(Speed.DIRECTION_UP);
        if(this.x > 0 && this.x < Constante.MaxX)
            if(rand.nextInt(2) == 1)
                speed.setyDirection(Speed.DIRECTION_DOWN);
            else
                speed.setyDirection(Speed.DIRECTION_UP);
        if(this.y > 0 && this.y < Constante.MaxY)
            if(rand.nextInt(2) == 1)
                speed.setxDirection(Speed.DIRECTION_RIGHT);
            else
                speed.setxDirection(Speed.DIRECTION_LEFT);
        */
    }
    private float get_angle(){
        float angle = 0;
        double sin = 0;
        double x = this.getSpeed().getxDirection();
        double y = this.getSpeed().getyDirection();
        double ip = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        if(ip > 0){
            if((y >= 0 && x >=0) || (y < 0 && x < 0 )){
                sin = Math.abs(y) / ip;
            } else {
                sin = Math.abs(x) / ip;
            }
            //verificam in ce cadran e.
            angle = (float) Math.asin(sin);
            angle = (float) ((angle * 180 ) / Math.PI);
            if(x >=0 && y >= 0)
                angle += 90;
            else if(x < 0 && y >= 0 )
                angle += 180;
            else if( x < 0 && y < 0)
                angle += 270;
        }
        this.angle = angle;
        this.sin = sin;
        return angle;
    }
    private int min_speed(){
        switch (this.id){
            case 1:
                return 10;
            case 2:
                return 20;
            case 3:
                return 30;
            case 4:
                return 40;
            case 5:
                return 50;


            case 50:
                return 10;
            case 51:
                return 20;
            case 52:
                return 30;
        }
        return 20;
    }
    private int max_speed(){
        switch (this.id){
            case 1:
                return 20;
            case 2:
                return 30;
            case 3:
                return 40;
            case 4:
                return 50;
            case 5:
                return 60;


            case 50:
                return 20;
            case 51:
                return 30;
            case 52:
                return 40;
        }
        return 25;
    }

    public boolean isOutside() {
        return isOutside;
    }

    public void setOutside(boolean outside) {
        isOutside = outside;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, (int)x - (bitmap.getWidth() /2), (int)y - (bitmap.getHeight() / 2), null);
    }

    public boolean handleActionDown(int eventX, int eventY){
        if(eventX >= (x - bitmap.getWidth() ) && eventX <= (x + bitmap.getWidth())){
            if(eventY >= (y - bitmap.getHeight()) && eventY <= (y + bitmap.getHeight())){
                return true;
                //setTouched(true);
            } else {
                //setTouched(false);
            }
        } else {
            //setTouched(false);
        }
        return false;
    }
    public boolean handleActionDownEnemy(int eventX, int eventY){
        if(eventX >= (x - bitmap.getWidth() / 2 ) && eventX <= (x + bitmap.getWidth() / 2)){
            if(eventY >= (y - bitmap.getHeight() / 2) && eventY <= (y + bitmap.getHeight() / 2)){
                return true;
                //setTouched(true);
            } else {
                //setTouched(false);
            }
        } else {
            //setTouched(false);
        }
        return false;
    }

    public void update(){
        //if(this.isActive){
            x += speed.getVelocity() * speed.getxDirection() * Constante.velocityAmplifier * speedBoost;
            y += speed.getVelocity() * speed.getyDirection() * Constante.velocityAmplifier * speedBoost;

            /*
            x += (speed.getXv() * speed.getxDirection());
            y += (speed.getYv() * speed.getyDirection());
            */
            //lastUpdate -- ;
        //}
    }

    public boolean can_be_touch() {
        return can_be_touch;
    }

    public void setCan_be_touch(boolean can_be_touch) {
        this.can_be_touch = can_be_touch;
    }

    public double getSin() {
        return sin;
    }

    public void setSin(double sin) {
        this.sin = sin;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
    //todo delete this dupa modificari
    /*
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    public boolean isTouched(){
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public int getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    */

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public float getSpeedBoost() {
        return speedBoost;
    }

    public void setSpeedBoost(float speedBoost) {
        this.speedBoost = speedBoost;
    }

}
