package com.replicanet;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AutoWorldZeroAttack {
    public static void main(String args[]) {
        try {
            System.out.println("Will be clicking... ");
            Thread.sleep(5000);
            System.out.println("Will be clicking... ");
            Robot robot = new Robot();
            Point originalPoint = MouseInfo.getPointerInfo().getLocation();
            while (true) {
                Point testPoint = MouseInfo.getPointerInfo().getLocation();
                if (Math.abs(testPoint.x - originalPoint.x) > 100) {
                    break;
                }

                for (int i = 0; i < 5 ; i++) {
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    Thread.sleep(50);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    Thread.sleep(250);
                }
                for (int i = 0; i < 5 ; i++) {
                    robot.keyPress(KeyEvent.VK_E);
                    Thread.sleep(50);
                    robot.keyRelease(KeyEvent.VK_E);
                    Thread.sleep(50);
                }
                for (int i = 0; i < 5 ; i++) {
                    robot.keyPress(KeyEvent.VK_R);
                    Thread.sleep(50);
                    robot.keyRelease(KeyEvent.VK_R);
                    Thread.sleep(50);
                }
                for (int i = 0; i < 5 ; i++) {
                    robot.keyPress(KeyEvent.VK_F);
                    Thread.sleep(50);
                    robot.keyRelease(KeyEvent.VK_F);
                    Thread.sleep(50);
                }
                for (int i = 0; i < 5 ; i++) {
                    robot.keyPress(KeyEvent.VK_1);
                    Thread.sleep(50);
                    robot.keyRelease(KeyEvent.VK_1);
                    Thread.sleep(50);
                }
                for (int i = 0; i < 5 ; i++) {
                    robot.keyPress(KeyEvent.VK_X);
                    Thread.sleep(50);
                    robot.keyRelease(KeyEvent.VK_X);
                    Thread.sleep(50);
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
