package com.replicanet;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AutoClickerJump {
    public static void main(String args[]) {
        try {
            System.out.println("Will be clicking in... " + args[0] + " for: " + args[1] + " " + args[2]);
            Thread.sleep(Integer.parseInt(args[0]));
            System.out.println("Will be clicking... ");
            Robot robot = new Robot();
            Point originalPoint = MouseInfo.getPointerInfo().getLocation();
            int jump = 0;
            while (true) {
                Point testPoint = MouseInfo.getPointerInfo().getLocation();
                if (Math.abs(testPoint.x - originalPoint.x) > 100) {
                    break;
                }

                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(Integer.parseInt(args[1]));
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(Integer.parseInt(args[2]));
                jump++;
                if (jump > Integer.parseInt(args[3])) {
                    jump = 0;
                    robot.keyPress(KeyEvent.VK_SPACE);
                    Thread.sleep(Integer.parseInt(args[4]));
                    robot.keyRelease(KeyEvent.VK_SPACE);
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
