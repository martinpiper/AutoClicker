package com.replicanet;

import java.awt.*;
import java.awt.event.InputEvent;

public class AutoClicker {
    public static void main(String args[]) {
        try {
            System.out.println("Will be clicking in... " + args[0] + " for: " + args[1] + " " + args[2]);
            Thread.sleep(Integer.parseInt(args[0]));
            System.out.println("Will be clicking... ");
            Robot robot = new Robot();
            Point originalPoint = MouseInfo.getPointerInfo().getLocation();
            while (true) {
                Point testPoint = MouseInfo.getPointerInfo().getLocation();
                if (Math.abs(testPoint.x - originalPoint.x) > 100) {
                    break;
                }

                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(Integer.parseInt(args[1]));
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(Integer.parseInt(args[2]));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        System.out.println("Stopped");
    }
}
