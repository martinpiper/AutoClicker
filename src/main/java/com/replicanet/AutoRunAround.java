package com.replicanet;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

public class AutoRunAround {
    public static void main(String args[]) {
        try {
            System.out.println("Will be clicking in... " );
            Thread.sleep(5000);
            System.out.println("Will be clicking... ");
            Robot robot = new Robot();
            Point originalPoint = MouseInfo.getPointerInfo().getLocation();
            Random random = new Random();
            while (true) {
                Point testPoint = MouseInfo.getPointerInfo().getLocation();
                if (Math.abs(testPoint.x - originalPoint.x) > 100) {
                    break;
                }

                if (random.nextBoolean()) {
                    robot.keyPress(KeyEvent.VK_W);
                    Thread.sleep(4000);
                    robot.keyRelease(KeyEvent.VK_W);
                }
                if (random.nextBoolean()) {
                    robot.keyPress(KeyEvent.VK_D);
                    Thread.sleep(3000);
                    robot.keyRelease(KeyEvent.VK_D);
                }
                if (random.nextBoolean()) {
                    robot.keyPress(KeyEvent.VK_S);
                    Thread.sleep(1000);
                    robot.keyRelease(KeyEvent.VK_S);
                }
                if (random.nextBoolean()) {
                    robot.keyPress(KeyEvent.VK_A);
                    Thread.sleep(2000);
                    robot.keyRelease(KeyEvent.VK_A);
                }
                if (random.nextBoolean()) {
                    robot.keyPress(KeyEvent.VK_SPACE);
                    Thread.sleep(2000);
                    robot.keyRelease(KeyEvent.VK_SPACE);
                }

                if (random.nextBoolean()) {
                    Thread.sleep(10000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        System.out.println("Stopped");
    }
}
