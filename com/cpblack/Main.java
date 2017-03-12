package com.cpblack;

import static com.cpblack.Connection2.client;
public class Main {
    public static boolean debug = false;
    public static String input = "";
    public static Thread runInput;
    public static Thread brain;
    public static void main(String[] args) throws InterruptedException {
        runInput = new Thread(new InputThread());
        runInput.start();
        Connection2.prepare();
        Connection2.initialize();
        /*
        while(true) {
            if (player.x != null && player.y != null && player.z != null) {
                client.getSession().send(new ClientPlayerPositionRotationPacket(player.isGrounded,player.x,player.y,player.z,player.yaw,player.pitch));
            }
            System.out.println(player.x + ", " + player.y);
            Thread.sleep((long)50);
        }
        */
        System.out.println("Connected: "+client.getSession().isConnected());
        //Thread.sleep((long)1000*120);
        //System.out.println("Quitting");
        //client.getSession().disconnect("Player Quit");
        //System.out.println("Connected: "+client.getSession().isConnected());
        //System.exit(1);
        //client.getSession().send(new ClientChatPacket("hi there :D"));
        brain = new Thread(new Brain());
        brain.start();
    }
}
