package com.replicanet;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.FileReader;
import java.io.IOException;

public class AutoSmelt {
    public static void main(String args[]) {
        try {
            System.getProperties().load(new FileReader("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (args.length < 4) {
                while (true) {
                    Point point = MouseInfo.getPointerInfo().getLocation();
                    System.out.println(point.x + " " + point.y);
                    Thread.sleep(1000);
                }
            }
            System.out.println("Will be smelting in... (Give target window focus)" + args[0]);
            Thread.sleep(Integer.parseInt(args[0]));
            System.out.println("Will be clicking... ");
            Robot robot = new Robot();
            for (int k = 0; k < Integer.parseInt(args[1]); k++) {
                System.out.println("Place fuel: " + k + "/" + args[1]);
                reliableMoveTo(robot,args[2], args[3]);
                clickItem(robot);

                for (int i = 0; i < Integer.parseInt(args[4]); i++) {
                    System.out.println("Placing items... " + i + "/" + args[4]);
                    reliableMoveTo(robot,args[6], args[7]);
                    for (int j = 0; j < Integer.parseInt(args[5]); j++) {
                        clickItem(robot);
                    }

                    System.out.println("Waiting... " + args[8]);
                    Thread.sleep(Integer.parseInt(args[8]));

                    System.out.println("Getting items...");
                    reliableMoveTo(robot,args[10], args[11]);
                    for (int j = 0; j < Integer.parseInt(args[9]); j++) {
                        clickItem(robot);
                    }
                }
            }
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Stopped");
    }

    public static void clickItem(Robot robot) {
        System.out.println("Click!");
        try {
            Thread.sleep(100);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(100);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String resolveValue(String value) {
        // If it doesn't exist then just return the value without change
        return System.getProperty(value,value);
    }

    public static void reliableMoveTo(Robot robot, String xpos, String ypos) throws InterruptedException {
        int ixpos = Integer.parseInt(resolveValue(xpos));
        int iypos = Integer.parseInt(resolveValue(ypos));
        reliableMoveTo(robot , ixpos, iypos);
    }
    public static void reliableMoveTo(Robot robot, int ixpos, int iypos) throws InterruptedException {
        // This jiggles the mouse around a bit for the GUI to catch-up with the inputs
        Thread.sleep(100);
        robot.mouseMove(ixpos+2, iypos+2);
        Thread.sleep(100);
        robot.mouseMove(ixpos-2, iypos-2);
        Thread.sleep(100);
        robot.mouseMove(ixpos, iypos);
        Thread.sleep(100);
    }

    public static void reliableMoveToFast(Robot robot, int ixpos, int iypos) throws InterruptedException {
        // This jiggles the mouse around a bit for the GUI to catch-up with the inputs
        Thread.sleep(10);
        robot.mouseMove(ixpos+2, iypos+2);
        Thread.sleep(10);
        robot.mouseMove(ixpos-2, iypos-2);
        Thread.sleep(10);
        robot.mouseMove(ixpos, iypos);
        Thread.sleep(10);
    }
}
