package com.replicanet;

import com.tailworks.ml.neuralnet.*;
import com.tailworks.ml.neuralnet.math.Vec;
import com.tailworks.ml.neuralnet.optimizer.Nesterov;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static com.tailworks.ml.neuralnet.Activation.*;

public class BloxburgAutoMine {

    public static void main(String args[]) throws Exception {

        // Set graphics details to "maximum", setting to "1 dot (minimum)" will cause the glitch where it is possible to drop out of the mine
//        captureImages();

        networkRun();
    }

    private static void networkRun() throws IOException, ClassNotFoundException, AWTException, InterruptedException {
//        int imageWidth = 256 , imageHeight = 256;
//        int imageWidth = 128 , imageHeight = 128;
        int imageWidth = 96 , imageHeight = 96;
//        int imageWidth = 64 , imageHeight = 64;
//        int imageWidth = 32 , imageHeight = 32;
//        int forInputSize = imageWidth * imageHeight * 3;
        int forInputSize = imageWidth * imageHeight;
//        int forHiddenLayers = 2048;
        int forHiddenLayers = 512;
//        int forHiddenLayers = 384;
//        int forHiddenLayers = 256;
//        int forHiddenLayers = 192;
//        int forHiddenLayers = 128;
//        int forHiddenLayers = 64;
//        int forHiddenLayers = 32;
//        int batchUpdate = 1;
        int batchUpdate = 20;

        // https://towardsdatascience.com/part-3-implementation-in-java-7bd305faad0
        NeuralNetwork network = getNeuralNetwork(forInputSize, forHiddenLayers);

        readNetwork(network,"target/BloxburgMine.net");

        printResults(imageWidth, imageHeight, forInputSize, network);

//        trainNetwork(imageWidth, imageHeight, forInputSize, batchUpdate, network, "target/BloxburgMine.net");
        // Although the network can be written after every iteration by trainNetwork(...,writeName), the below can force a write as well:
//        writeNet\work(network,"target/BloxburgMine.net");

//        readNetwork(network,"target/BloxburgMine.net");

        System.out.println("Test network");
        printResults(imageWidth, imageHeight, forInputSize, network);

        runLiveChecks(imageWidth, imageHeight, forInputSize, network);
    }

    private static void runLiveChecks(int imageWidth, int imageHeight, int forInputSize, NeuralNetwork network) throws AWTException, IOException, InterruptedException {
        int captureIndex = 0;
        Robot robot = new Robot();
        System.out.println("Will be Bloxburg mining...");
        Thread.sleep(5000);
        Point originalPoint = MouseInfo.getPointerInfo().getLocation();
        originalPoint = MouseInfo.getPointerInfo().getLocation();
        Random random = new Random();
        while (originalPoint.x > 0) {
            Result out = getResultFromScreen(imageWidth, imageHeight, forInputSize, network);
            System.out.println("Live result: " + out);
            // Checks for "TNT"
            if (out.getOutput().getData()[0] >= 0.6) {
                captureIndex = avoidTNT(captureIndex, robot);
            } else {
                // We execute a couple of small turns and tests to really check the intended target is not a TNT block
                // Small turn
                robot.keyPress(KeyEvent.VK_LEFT);
                Thread.sleep(20);
                robot.keyRelease(KeyEvent.VK_LEFT);

                out = getResultFromScreen(imageWidth, imageHeight, forInputSize, network);
                System.out.println("Live result: " + out);
                // Checks for "Mine"
                if (out.getOutput().getData()[0] >= 0.6) {
                    captureIndex = avoidTNT(captureIndex, robot);
                } else {
                    // Small turn
                    robot.keyPress(KeyEvent.VK_LEFT);
                    Thread.sleep(20);
                    robot.keyRelease(KeyEvent.VK_LEFT);

                    out = getResultFromScreen(imageWidth, imageHeight, forInputSize, network);
                    System.out.println("Live result: " + out);
                    // Checks for "TNT"
                    if (out.getOutput().getData()[0] >= 0.6) {
                        captureIndex = avoidTNT(captureIndex, robot);
                    } else {
                        captureImage("Mine/" , captureIndex++);
                        System.out.println("Mining!");
                        robot.keyPress(KeyEvent.VK_E);
                        robot.keyRelease(KeyEvent.VK_E);
                        robot.keyPress(KeyEvent.VK_E);
                        robot.keyRelease(KeyEvent.VK_E);
                        Thread.sleep(23000);
                    }
                }
            }

            if(random.nextBoolean()) {
                System.out.println("Turn left");
                robot.keyPress(KeyEvent.VK_LEFT);
                Thread.sleep(250);
                robot.keyRelease(KeyEvent.VK_LEFT);
            } else if(random.nextBoolean()) {
                System.out.println("Turn right");
                robot.keyPress(KeyEvent.VK_RIGHT);
                Thread.sleep(250);
                robot.keyRelease(KeyEvent.VK_RIGHT);
            }
            if(random.nextBoolean()) {
                System.out.println("Forward");
                robot.keyPress(KeyEvent.VK_UP);
                Thread.sleep(1500);
                robot.keyRelease(KeyEvent.VK_UP);
            }

            originalPoint = MouseInfo.getPointerInfo().getLocation();
            Thread.sleep(100);
        }
    }

    private static int avoidTNT(int captureIndex, Robot robot) throws AWTException, IOException, InterruptedException {
        captureImage("TNT/" , captureIndex++);
        System.out.println("Avoid TNT: Turn left");
        robot.keyPress(KeyEvent.VK_LEFT);
        Thread.sleep(250);
        robot.keyRelease(KeyEvent.VK_LEFT);
        return captureIndex;
    }

    private static Result getResultFromScreen(int imageWidth, int imageHeight, int forInputSize, NeuralNetwork network) throws AWTException, IOException {
        BufferedImage image = getBufferedImage();
        Vec testInput = getVecFromImage(imageWidth, imageHeight, forInputSize,image);
        Result out = network.evaluate(testInput);
        return out;
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

    private static void trainNetwork(int imageWidth, int imageHeight, int forInputSize, int batchUpdate, NeuralNetwork network, String writeName) throws IOException {
        List<File> filesList1 = getFilesFromDirectory("TestData/Bloxburg mine/TNT");
        List<File> filesList2 = getFilesFromDirectory("TestData/Bloxburg mine/Mine");
        int min = Math.min(filesList1.size(),filesList2.size());


        int batchCount = batchUpdate;
        for (int iterations = 0 ; iterations < 1000 ; iterations++) {
            // Repeatedly balance out the list to teach by selecting new items from the lists
            Collections.shuffle(filesList1);
            Collections.shuffle(filesList2);
            List<File> allFiles = new ArrayList<>();
            allFiles.addAll(filesList1.subList(0,min));
            allFiles.addAll(filesList2.subList(0,min));
            Collections.shuffle(allFiles);
            for (final File fileEntry : allFiles) {
                Vec input = getDataFromImageFile(imageWidth, imageHeight, forInputSize, fileEntry);
                Vec expected = new Vec(0.0, 0.0);
                if (fileEntry.getParent().endsWith("TNT")) {
                    expected = new Vec(1.0, 0.0);
                } else if (fileEntry.getParent().endsWith("Mine")) {
                    expected = new Vec(0.0, 1.0);
                }
                Result out2 = network.evaluate(input, expected);
                String suffix = "";
                if (out2.getCost() > 0.2) {
                    suffix = "                            **********";
                }
                System.out.println(iterations + ":" + out2 + suffix);

                if (--batchCount <= 0) {
                    System.out.println("Learning...");
                    network.updateFromLearning();
                    batchCount = batchUpdate;
                }
            }
            if (writeName != null) {
                writeNetwork(network, writeName);
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

                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))
                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))
                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))
                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))

                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))
                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))
                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))
                        .addLayer(new Layer(forHiddenLayers, Leaky_ReLU))

//                        .addLayer(new Layer(forHiddenLayers, ReLU))
//                        .addLayer(new Layer(forHiddenLayers, ReLU))
//                        .addLayer(new Layer(forHiddenLayers, ReLU))
//                        .addLayer(new Layer(forHiddenLayers, ReLU))

//                        .addLayer(new Layer(forHiddenLayers, Sigmoid, 0.5))
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
        Vec testInput = getDataFromImageFile(imageWidth, imageHeight, forInputSize, new File("TestData/Bloxburg mine/TNT/a_29.png"));
        Result out1 = network.evaluate(testInput);
        System.out.println("Result fish: " + out1);
        testInput = getDataFromImageFile(imageWidth, imageHeight, forInputSize, new File("TestData/Bloxburg mine/Mine/a_88.png"));
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
                // The TNT is red so the more intense (or bigger) the red "box" seen on the screen the more we will turn away from it
                forInput[i++] = colour.getRed() / 256.0f;
//                forInput[i++] = colour.getGreen() / 256.0f;
//                forInput[i++] = colour.getBlue() / 256.0f;
                scaledImage.setRGB(x,y,colour.getRGB());
            }
        }

        ImageIO.write(scaledImage,"png", new File("target/t.png"));

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
            captureImage("",i);
            Thread.sleep(500);
            i++;
        }
    }

    private static void captureImage(String directory , int i) throws AWTException, IOException {
        BufferedImage bufferedImage = getBufferedImage();
        File file = new File("target/images/"+directory+"aa_"+ i +".png");
        Files.createDirectories(Paths.get(file.toURI()));
        boolean status = ImageIO.write(bufferedImage, "png", file);
        System.out.println("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
    }

    private static BufferedImage getBufferedImage() throws AWTException {
        Robot robot = new Robot();

        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
        bufferedImage = bufferedImage.getSubimage(bufferedImage.getWidth() / 4,bufferedImage.getHeight()/6, bufferedImage.getWidth() / 2, (bufferedImage.getHeight() * 2)/3);
        return bufferedImage;
    }
}
