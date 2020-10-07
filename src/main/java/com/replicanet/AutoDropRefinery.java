package com.replicanet;

import java.awt.*;
import java.awt.event.KeyEvent;

public class AutoDropRefinery {
    public static void main(String args[]) {
        try {
            int times = Integer.parseInt(args[1]);
            System.out.println("Will be dropping in... " + args[0] + " for: " + args[1] + " " + args[2]);
            Thread.sleep(Integer.parseInt(args[0]));
            System.out.println("Will be dropping... ");
            Robot robot = new Robot();
            Point originalPoint = MouseInfo.getPointerInfo().getLocation();
            while (times-- > 0) {
                System.out.println("Times left... " + times);
                Point testPoint = MouseInfo.getPointerInfo().getLocation();
                if (Math.abs(testPoint.x - originalPoint.x) > 100) {
                    break;
                }

                robot.keyPress(KeyEvent.VK_1);
                robot.keyRelease(KeyEvent.VK_1);
                int times1 = Integer.parseInt(args[2]);
                while (times1-- > 0) {
                    robot.keyPress(KeyEvent.VK_Q);
                    robot.keyRelease(KeyEvent.VK_Q);
                    Thread.sleep(500);
                }
                robot.keyPress(KeyEvent.VK_1);
                robot.keyRelease(KeyEvent.VK_1);

                robot.keyPress(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_2);
                times1 = Integer.parseInt(args[3]);
                while (times1-- > 0) {
                    robot.keyPress(KeyEvent.VK_Q);
                    robot.keyRelease(KeyEvent.VK_Q);
                    Thread.sleep(500);
                }
                robot.keyPress(KeyEvent.VK_2);
                robot.keyRelease(KeyEvent.VK_2);

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
