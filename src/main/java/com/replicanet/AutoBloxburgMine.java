package com.replicanet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class AutoBloxburgMine {
    public static void main(String args[]) {
        try {
            Robot robot = new Robot();
            Point originalPoint = MouseInfo.getPointerInfo().getLocation();
            while (originalPoint.x > 0) {
                System.out.println(originalPoint.x + " , " + originalPoint.y);
                Thread.sleep(250);
                originalPoint = MouseInfo.getPointerInfo().getLocation();
            }
            System.out.println("Will be Bloxburg mining...");
            Thread.sleep(5000);
            originalPoint = MouseInfo.getPointerInfo().getLocation();
            Random random = new Random();
            while (originalPoint.x > 0) {
                System.out.println("Picking...");
                for (int i = 0 ; i < 2 ; i++) {
                    robot.keyPress(KeyEvent.VK_E);
                    robot.keyRelease(KeyEvent.VK_E);
                    robot.keyPress(KeyEvent.VK_E);
                    robot.keyRelease(KeyEvent.VK_E);
                    Thread.sleep(10000);
                }

                System.out.println("Movement...");
                if(random.nextBoolean()) {
                    System.out.println("Turn left");
                    robot.keyPress(KeyEvent.VK_LEFT);
                    Thread.sleep(500);
                    robot.keyRelease(KeyEvent.VK_LEFT);
                }
                if(random.nextBoolean()) {
                    System.out.println("Forward");
                    robot.keyPress(KeyEvent.VK_UP);
                    Thread.sleep(1500);
                    robot.keyRelease(KeyEvent.VK_UP);
                }

                originalPoint = MouseInfo.getPointerInfo().getLocation();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        System.out.println("Stopped");
    }
}
