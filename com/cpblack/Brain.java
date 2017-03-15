package com.cpblack;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;

import static com.cpblack.Connection2.client;
import static com.cpblack.Main.input;

/**
 * Created by Caleb on 3/11/2017.
 */
public class Brain implements Runnable {

    @Override
    public void run() {
        while (true) {
            /** BEGINNING OF CYClE */
            handleInput();


            /** END OF CYCLE */
            try {
                Thread.sleep((long) 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleInput() {
        if (input != "") {
            if (input.substring(0, 1).equalsIgnoreCase("~")) {
                input = input.substring(1);
                if (input.equalsIgnoreCase("isconnected") | input.equalsIgnoreCase("connection")) {
                    System.out.println("Connected: " + client.getSession().isConnected());
                } if (input.equalsIgnoreCase("coords") | input.equalsIgnoreCase("location")){
                    System.out.println("Coordinates: +"+inGame.player.x+", "+inGame.player.y+", "+inGame.player.z)
                } else if (input.equalsIgnoreCase("exit") | input.equalsIgnoreCase("quit")) {
                    System.out.println("Closing Connection.");
                    client.getSession().disconnect("Quitting.");
                    System.out.println("Exiting Application");
                    System.exit(1);
                } else if (input.equalsIgnoreCase("debug")) {
                    Main.debug = !Main.debug;
                    System.out.println("Debug: " + Main.debug);
                } else if (input.equalsIgnoreCase("help")){
                    System.out.println("Commands: ~connection, ~location, ~debug, ~exit, ~help");
                } else {
                    System.out.println("Unknown Command: " + input);
                }
            } else {
                System.out.println("Saying: " + input);
                client.getSession().send(new ClientChatPacket(input));
            }
            input = "";
        }
    }
}
