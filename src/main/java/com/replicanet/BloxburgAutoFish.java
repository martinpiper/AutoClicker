package com.replicanet;

import com.tailworks.ml.neuralnet.*;
import com.tailworks.ml.neuralnet.math.Vec;
import com.tailworks.ml.neuralnet.optimizer.Nesterov;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.tailworks.ml.neuralnet.Activation.*;

public class BloxburgAutoFish {

    public static void main(String args[]) throws Exception {

//        captureImages();

        networkRun();
    }

    private static void networkRun() throws IOException, ClassNotFoundException, AWTException, InterruptedException {
//        int imageWidth = 256 , imageHeight = 256;
//        int imageWidth = 128 , imageHeight = 128;
        int imageWidth = 64 , imageHeight = 64;
//        int imageWidth = 32 , imageHeight = 32;
//        int forInputSize = imageWidth * imageHeight * 3;
        int forInputSize = imageWidth * imageHeight;
//        int forHiddenLayers = 2048;
//        int forHiddenLayers = 512;
//        int forHiddenLayers = 256;
//        int forHiddenLayers = 128;
        int forHiddenLayers = 64;
//        int forHiddenLayers = 32;
//        int batchUpdate = 1;
        int batchUpdate = 20;

        // https://towardsdatascience.com/part-3-implementation-in-java-7bd305faad0
        NeuralNetwork network = getNeuralNetwork(forInputSize, forHiddenLayers);

        readNetwork(network,"target/t.net");

//        printResults(imageWidth, imageHeight, forInputSize, network);

//        trainNetwork(imageWidth, imageHeight, forInputSize, batchUpdate, network);
//        writeNetwork(network,"target/t.net");
//        readNetwork(network,"target/t.net");

        System.out.println("Test network");
        printResults(imageWidth, imageHeight, forInputSize, network);


//        NeuralNetwork newNetwork = getNeuralNetwork(forInputSize, forHiddenLayers);
//        readNetwork(newNetwork,"target/t.net");
//        System.out.println("Test network again");
//        printResults(imageWidth, imageHeight, forInputSize, newNetwork);

        runLiveChecks(imageWidth, imageHeight, forInputSize, network);
    }

    private static void runLiveChecks(int imageWidth, int imageHeight, int forInputSize, NeuralNetwork network) throws AWTException, IOException, InterruptedException {
        int hits = 0;
        int timeout = 0;
        while (true) {
            BufferedImage image = getBufferedImage();
            Vec testInput = getVecFromImage(imageWidth, imageHeight, forInputSize,image);
            Result out = network.evaluate(testInput);
            System.out.println("Live result: " + out);
            if (out.getOutput().getData()[0] >= 0.7) {
                hits++;
            } else {
                hits = 0;
            }
            if (hits > 2 || timeout > 500) {
                System.out.println("Fishing... " + hits + " : " + timeout);
                hits = 0;
                timeout = 0;
                Robot robot = new Robot();

                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(10);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                Thread.sleep(3000);

                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                Thread.sleep(10);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                Thread.sleep(2000);
            } else {
                timeout++;
            }
            Thread.sleep(100);
        }
    }

    private static void readNetwork(NeuralNetwork newNetwork, String filename) throws IOException {
        System.out.println("Read network");
        FileInputStream fileIn = new FileInputStream(filename);
        DataInputStream dataIn = new DataInputStream(fileIn);
        newNetwork.read(dataIn);
        dataIn.close();
        fileIn.close();
    }

    private static void writeNetwork(NeuralNetwork network, String filename) throws IOException {
        System.out.println("Write network");
        FileOutputStream fileOut = new FileOutputStream(filename);
        DataOutputStream dataOut = new DataOutputStream(fileOut);
        network.write(dataOut);
        dataOut.flush();
        fileOut.close();
    }

    private static void trainNetwork(int imageWidth, int imageHeight, int forInputSize, int batchUpdate, NeuralNetwork network) throws IOException {
        //        List<File> filesList1 = getFilesFromDirectory("TestData/Bloxburg fishing/Fish");
//        List<File> filesList2 = getFilesFromDirectory("TestData/Bloxburg fishing/Float");
        List<File> filesList1 = getFilesFromDirectory("TestData/Bloxburg fishing 2/Fish");
        List<File> filesList2 = getFilesFromDirectory("TestData/Bloxburg fishing 2/Float");
//        List<File> filesList1 = getFilesFromDirectory("TestData/Simple/Fish");
//        List<File> filesList2 = getFilesFromDirectory("TestData/Simple/Float");
        List<File> allFiles = new ArrayList<>();
        allFiles.addAll(filesList1);
        allFiles.addAll(filesList2);

        int batchCount = batchUpdate;
        for (int iterations = 0 ; iterations < 100 ; iterations++) {
            Collections.shuffle(allFiles);
            for (final File fileEntry : allFiles) {
                Vec input = getDataFromImageFile(imageWidth, imageHeight, forInputSize, fileEntry);
                Vec expected = new Vec(0.0, 0.0);
                if (fileEntry.getParent().endsWith("Fish")) {
                    expected = new Vec(1.0, 0.0);
                } else if (fileEntry.getParent().endsWith("Float")) {
                    expected = new Vec(0.0, 1.0);
                }
                Result out2 = network.evaluate(input, expected);
                System.out.println(iterations + ":" + out2);

                if (--batchCount <= 0) {
                    System.out.println("Learing...");
                    network.updateFromLearning();
                    batchCount = batchUpdate;
                }
            }
        }
        network.updateFromLearning();
    }

    private static NeuralNetwork getNeuralNetwork(int forInputSize, int forHiddenLayers) {
        NeuralNetwork network =
                new NeuralNetwork.Builder(forInputSize)
                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))
                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))
                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))
                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))

//                        .addLayer(new Layer(forHiddenLayers, ReLU))
//                        .addLayer(new Layer(forHiddenLayers, ReLU))
//                        .addLayer(new Layer(forHiddenLayers, ReLU))
//                        .addLayer(new Layer(forHiddenLayers, ReLU))

                        .addLayer(new Layer(forHiddenLayers, Sigmoid, 0.5))
//                        .addLayer(new Layer(forHiddenLayers, Sigmoid, 0.5))
//                        .addLayer(new Layer(forHiddenLayers, Sigmoid, 0.5))

                        .addLayer(new Layer(2, Softmax))
                        .initWeights(new Initializer.XavierNormal())
                        .setCostFunction(new CostFunction.Quadratic())

                        .setOptimizer(new Nesterov(0.02, 0.87))
                        .l2(0.00010)

//                        .setOptimizer(new GradientDescent(0.01))

//                        .setOptimizer(new Momentum(0.1 , 0.5))

                        .create();
        return network;
    }

    private static void printResults(int imageWidth, int imageHeight, int forInputSize, NeuralNetwork network) throws IOException {
//        Vec testInput = getDataFromImage(imageWidth, imageHeight, forInputSize, new File("TestData/Bloxburg fishing/Fish/t_0.png"));
        Vec testInput = getDataFromImageFile(imageWidth, imageHeight, forInputSize, new File("TestData/Bloxburg fishing 2/Fish/t_1.png"));
//        Vec testInput = getDataFromImage(imageWidth, imageHeight, forInputSize, new File("TestData/Simple/Fish/t_2.png"));
        Result out1 = network.evaluate(testInput);
        System.out.println("Result fish: " + out1);
//        testInput = getDataFromImage(imageWidth, imageHeight, forInputSize, new File("TestData/Bloxburg fishing/Float/t_2.png"));
        testInput = getDataFromImageFile(imageWidth, imageHeight, forInputSize, new File("TestData/Bloxburg fishing 2/Float/t_0.png"));
//        testInput = getDataFromImage(imageWidth, imageHeight, forInputSize, new File("TestData/Simple/Float/t_0.png"));
        out1 = network.evaluate(testInput);
        System.out.println("Result float: " + out1);
    }

    private static List<File> getFilesFromDirectory(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();
        List<File> filesList = Arrays.asList(files);
        return filesList;
    }

    private static Vec getDataFromImageFile(int imageWidth, int imageHeight, int forInputSize, File fileEntry) throws IOException {
        System.out.println(fileEntry.getAbsolutePath());
        BufferedImage image = ImageIO.read(fileEntry);
        return getVecFromImage(imageWidth, imageHeight, forInputSize, image);
    }

    private static Vec getVecFromImage(int imageWidth, int imageHeight, int forInputSize, BufferedImage image) throws IOException {
        BufferedImage scaledImage = new BufferedImage(imageWidth, imageHeight, image.getType());
        Graphics2D g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();

        double[] forInput = new double[forInputSize];

        int i = 0;
        for (int y = 0 ; y < scaledImage.getHeight() ; y++) {
            for (int x = 0; x < scaledImage.getWidth(); x++) {
                int rgb = scaledImage.getRGB(x,y);
                Color colour = new Color(rgb);
                colour = new Color(colour.getRed(),0,0);
                forInput[i++] = colour.getRed() / 256.0f;
                // The rippling water effect has a lot of green and blue noise, so we only consider the red channel
//                forInput[i++] = colour.getGreen() / 256.0f;
//                forInput[i++] = colour.getBlue() / 256.0f;
                scaledImage.setRGB(x,y,colour.getRGB());
            }
        }

//        ImageIO.write(scaledImage,"png", new File("target/t.png"));

        Vec input = new Vec(forInput);
        return input;
    }

    private static void captureImages() throws AWTException, IOException, InterruptedException {
        int i = 0;
        while (i < 10000) {
            // Lowest graphics setting
            // Zoom out once with "O"
            // Cast
            // Right mouse button, rotate and tilt until the fishing float is in the far left hand middle of the screen
            // Right mouse button and down, as much overhead as possible
            BufferedImage bufferedImage = getBufferedImage();
            File file = new File("target/images/a_"+i+".png");
            Files.createDirectories(Paths.get(file.toURI()));
            boolean status = ImageIO.write(bufferedImage, "png", file);
            System.out.println("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
            Thread.sleep(500);
            i++;
        }
    }

    private static BufferedImage getBufferedImage() throws AWTException {
        Robot robot = new Robot();

        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        /*
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            rectangle = screenRect.union(gd.getDefaultConfiguration().getBounds());
        }
        */
        BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
        bufferedImage = bufferedImage.getSubimage(0,bufferedImage.getHeight()/4, bufferedImage.getWidth() / 6, bufferedImage.getHeight()/2);
        return bufferedImage;
    }
}
