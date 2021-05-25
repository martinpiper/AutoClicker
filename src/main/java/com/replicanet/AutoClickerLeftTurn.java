package com.replicanet;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AutoClickerLeftTurn {
    public static void main(String args[]) {
        try {
            System.out.println("Will be clicking and turning left in... " + args[0] + " for: " + args[1] + " " + args[2]);
            Thread.sleep(Integer.parseInt(args[0]));
            System.out.println("Will be clicking... ");
            Robot robot = new Robot();
            Point originalPoint = MouseInfo.getPointerInfo().getLocation();
            while (true) {
                Point testPoint = MouseInfo.getPointerInfo().getLocation();
                if (Math.abs(testPoint.x - originalPoint.x) > 100) {
                    break;
                }

                for (int i = 0 ; i < 3 ; i++ ) {
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    Thread.sleep(Integer.parseInt(args[1]));
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    Thread.sleep(Integer.parseInt(args[2]));
                }

                robot.keyPress(KeyEvent.VK_LEFT);
                Thread.sleep(Integer.parseInt(args[1]));
                robot.keyRelease(KeyEvent.VK_LEFT);
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
