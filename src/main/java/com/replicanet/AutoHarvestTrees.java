package com.replicanet;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AutoHarvestTrees {
    public static void main(String args[]) {
        try {
            System.out.println("Pausing while pointer over game window...");
            while (false && MouseInfo.getPointerInfo().getLocation().x < 0) {
                System.out.println(MouseInfo.getPointerInfo().getLocation().x + "    " + MouseInfo.getPointerInfo().getLocation().y);
                Thread.sleep(100);
            }
            System.out.println("Will be harvesting trees... ");
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

                doMoveActionFor(robot, KeyEvent.VK_D, 2500);
                doKeyAction(robot, KeyEvent.VK_O);
                doKeyAction(robot, KeyEvent.VK_2);
                doPlanting(robot,-964,423);
                doPlanting(robot,-1015,438);
                doPlanting(robot,-1081,465);
                doPlanting(robot,-1177,503);
                doKeyAction(robot, KeyEvent.VK_2);
                AutoSmelt.reliableMoveTo(robot, originalPoint.x, originalPoint.y);
                doKeyAction(robot, KeyEvent.VK_I);
                doKeyAction(robot, KeyEvent.VK_1);
                doHarvestAction(robot);
                doKeyAction(robot, KeyEvent.VK_1);


                doMoveActionFor(robot, KeyEvent.VK_A, 2500);
                doKeyAction(robot, KeyEvent.VK_O);
                doKeyAction(robot, KeyEvent.VK_2);
                doPlanting(robot,-309 , 424);
                doPlanting(robot,-285,439);
                doPlanting(robot,-205,463);
                doPlanting(robot,-142,501);
                doKeyAction(robot, KeyEvent.VK_2);
                AutoSmelt.reliableMoveTo(robot, originalPoint.x, originalPoint.y);
                doKeyAction(robot, KeyEvent.VK_I);
                doKeyAction(robot, KeyEvent.VK_1);
                doHarvestAction(robot);
                doKeyAction(robot, KeyEvent.VK_1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        System.out.println("Stopped");
    }

    private static void doPlanting(Robot robot, int x, int y) throws InterruptedException {
        for (int xd = -20 ; xd <= 20 ; xd+=10) {
            for (int yd = -20 ; yd <= 20 ; yd+=10) {
                AutoSmelt.reliableMoveToFast(robot,x+xd,y+yd);
                doPlantAction(robot);
            }
        }
    }

    private static void doKeyAction(Robot robot, int vkO) throws InterruptedException {
        robot.keyPress(vkO);
        Thread.sleep(100);
        robot.keyRelease(vkO);
        Thread.sleep(100);
    }

    private static void doHarvestAction(Robot robot) throws InterruptedException {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(25000);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private static void doPlantAction(Robot robot) throws InterruptedException {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(10);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private static void doMoveActionFor(Robot robot, int vkD, int time) throws InterruptedException {
        robot.keyPress(vkD);
        Thread.sleep(time);
        robot.keyRelease(vkD);
        robot.keyPress(KeyEvent.VK_S);
        Thread.sleep(1000);
        robot.keyRelease(KeyEvent.VK_S);
    }
}
