package com.biskis.smashthatbug;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Dani Dudas
 * Date: 11/17/11
 * Time: 10:33 PM
 */
public class Speed {
    private float velocity = 1;
    private float xDirection = 1;
    private float yDirection = 1;

    /*
    public static final int DIRECTION_RIGHT = 1;
    public static final int DIRECTION_LEFT  = -1;
    public static final int DIRECTION_UP    = -1;
    public static final int DIRECTION_DOWN  = 1;

    private float xv = 1;
    private float yv = 1;

    private int xDirection = DIRECTION_RIGHT;
    private int yDirection = DIRECTION_DOWN;
    */

    public Speed(){
    }

    public Speed(int posX, int posY, int minSpeed, int maxSpeed){
        //calculam viteza si directia de mers.
        Random rand = new Random();
        velocity = (float)(minSpeed + rand.nextInt(maxSpeed - minSpeed) ) / 100;
        if(posX < 0){
            //stanga
            do {
                xDirection = (float)(0 + rand.nextInt(100)) / 100;
                yDirection = (float)(-100 + rand.nextInt(200)) / 100;
            } while(Math.abs(xDirection) + Math.abs(yDirection) < 0.4f);
        } else  if(posY < 0){
            //sus
            do {
                xDirection = (float)(-90 + rand.nextInt(180)) / 100;
                yDirection = (float)(20 + rand.nextInt(80)) / 100;
            } while(Math.abs(xDirection) + Math.abs(yDirection) < 0.4f);
        } else if(posX > 300){
            // dreapta
            do {
                xDirection = (float)(-100 + rand.nextInt(100)) / 100;
                yDirection = (float)(-100 + rand.nextInt(200)) / 100;
            } while(Math.abs(xDirection) + Math.abs(yDirection) < 0.4f);
        } else {
            //jos
            do {
                xDirection = (float)(-90 + rand.nextInt(180)) / 100;
                yDirection = (float)(-100 + rand.nextInt(80)) / 100;
            } while(Math.abs(xDirection) + Math.abs(yDirection) < 0.4f);
        }
    }
    /*
    public Speed(float xv, float yv){
        this.xv = xv;
        this.yv = yv;
    }


    public void toggleXDirection(){
        xDirection = xDirection * -1;
    }
    public void toggleYDirection(){
        yDirection = yDirection * -1;
    }

    //getter si setter
    public float getXv() {
        return xv;
    }

    public void setXv(float xv) {
        this.xv = xv;
    }

    public float getYv() {
        return yv;
    }

    public void setYv(float yv) {
        this.yv = yv;
    }
    */
    public float getxDirection() {
        return xDirection;
    }

    public void setxDirection(float xDirection) {
        this.xDirection = xDirection;
    }

    public float getyDirection() {
        return yDirection;
    }

    public void setyDirection(float yDirection) {
        this.yDirection = yDirection;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }
}
