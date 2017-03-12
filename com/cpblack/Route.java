package com.cpblack;

import static java.lang.Math.abs;

/**
 * Created by Caleb on 3/11/2017.
 */
public class Route {
    public double[] startingPoint;
    public double[] currentPos;
    public double[] destination;
    public double speed = 0.05;
    public void initialize(double[] currentPosIn, double[] startingPointIn, double[] destinationIn){
        this.currentPos = currentPosIn;
        this.startingPoint = startingPointIn;
        this.destination = destinationIn;
    }
    private static double[] getTargetPosition(double[] destinationIn,double[] currentPosIn, double speedIn){
        double[] net = {destinationIn[0] - currentPosIn[0],destinationIn[1] - currentPosIn[1],destinationIn[2] - currentPosIn[2]};
        int[] getDirection = direction(net);
        double[] moveAmount = {0,0,0};
        if (abs(net[0]) < speedIn) {
            moveAmount[0] = destinationIn[0] - currentPosIn[0];
        } else {
            moveAmount[0] = getDirection[0] * speedIn;
        }

        if (abs(net[1]) < speedIn) {
            moveAmount[1] = destinationIn[1] - currentPosIn[1];
        } else {
            moveAmount[1] = getDirection[1] * speedIn;
        }

        if (abs(net[2]) < speedIn) {
            moveAmount[2] = destinationIn[2] - currentPosIn[2];
        } else {
            moveAmount[2] = getDirection[2] * speedIn;
        }
        return new double[]{currentPosIn[0] + moveAmount[0],currentPosIn[1] + moveAmount[1],currentPosIn[2] + moveAmount[2]};
    }
    private static int[] direction(double[] netIn){
        return new int[]{convertNet(netIn[0]),convertNet(netIn[1]),convertNet(netIn[2])};
    }
    private static int convertNet(double input){
        int output = 100000;
        if (input > 0) {
            output = 1;
        } else if (input < 0) {
            output = -1;
        } else if (input == 0) {
            output = 0;
        }
        return output;
    }
    public static boolean isBlocked(double[] positionIn){

        return true;
    }


}
