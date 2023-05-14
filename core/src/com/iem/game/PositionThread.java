package com.iem.game;

public class PositionThread  extends  Thread{

    public static boolean flag = true;

    @Override
    public void run(){
        try {
            while (flag) {
                Thread.sleep(200);
                MultiplayerGameScreen.sendPosition();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
