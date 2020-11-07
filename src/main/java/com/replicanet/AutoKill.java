package com.replicanet;

import java.awt.*;
import java.awt.event.KeyEvent;

public class AutoKill {
    public static void main(String args[]) {
        try {
            Robot robot = new Robot();
            Point originalPoint = MouseInfo.getPointerInfo().getLocation();
            while (originalPoint.x > 0) {
                System.out.println(originalPoint.x + " , " + originalPoint.y);
                Thread.sleep(250);
                originalPoint = MouseInfo.getPointerInfo().getLocation();
            }
            System.out.println("Will be killing...");
            Thread.sleep(5000);
            originalPoint = MouseInfo.getPointerInfo().getLocation();
            while (originalPoint.x > 0) {
//                AutoSmelt.reliableMoveTo(robot,392,596);

                AutoSmelt.clickItem(robot);
                robot.keyPress(KeyEvent.VK_Q);
                robot.keyRelease(KeyEvent.VK_Q);
                Thread.sleep(1000);
                robot.keyPress(KeyEvent.VK_R);
                robot.keyRelease(KeyEvent.VK_R);

//                Thread.sleep(2000);
//                AutoSmelt.reliableMoveTo(robot, 1618, 617);

                AutoSmelt.clickItem(robot);
                robot.keyPress(KeyEvent.VK_E);
                robot.keyRelease(KeyEvent.VK_E);
                Thread.sleep(1000);
                robot.keyPress(KeyEvent.VK_R);
                robot.keyRelease(KeyEvent.VK_R);

//                Thread.sleep(22000);

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
