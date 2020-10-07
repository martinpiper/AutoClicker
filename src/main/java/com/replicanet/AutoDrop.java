package com.replicanet;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AutoDrop {
    public static void main(String args[]) {
        try {
            System.out.println("Will be dropping in... " + args[0] + " for: " + args[1] + " " + args[2]);
            Thread.sleep(Integer.parseInt(args[0]));
            System.out.println("Will be dropping... ");
            Robot robot = new Robot();
            Point originalPoint = MouseInfo.getPointerInfo().getLocation();
            while (true) {
                Point testPoint = MouseInfo.getPointerInfo().getLocation();
                if (Math.abs(testPoint.x - originalPoint.x) > 100) {
                    break;
                }

                robot.keyPress(KeyEvent.VK_Q);
                Thread.sleep(Integer.parseInt(args[1]));
                robot.keyRelease(KeyEvent.VK_Q);
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
