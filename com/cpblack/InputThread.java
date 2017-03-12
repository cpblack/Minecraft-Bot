package com.cpblack;

/**
 * Created by Caleb on 3/11/2017.
 */


import java.util.Scanner;

class InputThread implements Runnable {
    private boolean execute = true;
    @Override
    public void run() {
        Scanner scan = new Scanner(System.in);
        while (this.execute) {
            Main.input = scan.nextLine();
        }
        scan.close();
    }
    public void stopRunning() {
        this.execute = false;
    }
}
