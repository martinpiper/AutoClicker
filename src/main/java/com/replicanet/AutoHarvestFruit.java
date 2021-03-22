package com.replicanet;

import java.awt.*;
import java.awt.event.KeyEvent;

public class AutoHarvestFruit {
    public static void main(String args[]) {
        try {
            System.out.println("Will be harvesting... ");
            Thread.sleep(5000);
            Robot robot = new Robot();
            Point originalPoint = MouseInfo.getPointerInfo().getLocation();
            int times = 0;
            while (true) {
                System.out.println("Times... " + times++);
                Point testPoint = MouseInfo.getPointerInfo().getLocation();
                if (Math.abs(testPoint.x - originalPoint.x) > 100) {
                    break;
                }

                robot.keyPress(KeyEvent.VK_F);
                Thread.sleep(1000);
                robot.keyRelease(KeyEvent.VK_F);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        System.out.println("Stopped");
    }
}
