package com.replicanet;

import com.tailworks.ml.neuralnet.*;
import com.tailworks.ml.neuralnet.math.Vec;
import com.tailworks.ml.neuralnet.optimizer.GradientDescent;
import com.tailworks.ml.neuralnet.optimizer.Momentum;
import com.tailworks.ml.neuralnet.optimizer.Nesterov;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
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

        networkTrain();
    }

    private static void networkTrain() throws IOException {
//        int imageWidth = 256 , imageHeight = 256;
//        int imageWidth = 128 , imageHeight = 128;
        int imageWidth = 64 , imageHeight = 64;
//        int imageWidth = 32 , imageHeight = 32;
        int forInputSize = imageWidth * imageHeight * 3;
//        int forHiddenLayers = 2048;
//        int forHiddenLayers = 512;
//        int forHiddenLayers = 256;
//        int forHiddenLayers = 128;
        int forHiddenLayers = 64;
//        int forHiddenLayers = 32;
//        int batchUpdate = 1;
        int batchUpdate = 20;

        // https://towardsdatascience.com/part-3-implementation-in-java-7bd305faad0
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

        printResults(imageWidth, imageHeight, forInputSize, network);

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
        for (int iterations = 0 ; iterations < 200 ; iterations++) {
            Collections.shuffle(allFiles);
            for (final File fileEntry : allFiles) {
                Vec input = getDataFromImage(imageWidth, imageHeight, forInputSize, fileEntry);
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

        printResults(imageWidth, imageHeight, forInputSize, network);
    }

    private static void printResults(int imageWidth, int imageHeight, int forInputSize, NeuralNetwork network) throws IOException {
//        Vec testInput = getDataFromImage(imageWidth, imageHeight, forInputSize, new File("TestData/Bloxburg fishing/Fish/t_0.png"));
        Vec testInput = getDataFromImage(imageWidth, imageHeight, forInputSize, new File("TestData/Bloxburg fishing 2/Fish/t_1.png"));
//        Vec testInput = getDataFromImage(imageWidth, imageHeight, forInputSize, new File("TestData/Simple/Fish/t_2.png"));
        Result out1 = network.evaluate(testInput);
        System.out.println("Result fish: " + out1);
//        testInput = getDataFromImage(imageWidth, imageHeight, forInputSize, new File("TestData/Bloxburg fishing/Float/t_2.png"));
        testInput = getDataFromImage(imageWidth, imageHeight, forInputSize, new File("TestData/Bloxburg fishing 2/Float/t_0.png"));
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

    private static Vec getDataFromImage(int imageWidth, int imageHeight, int forInputSize, File fileEntry) throws IOException {
        System.out.println(fileEntry.getAbsolutePath());
        BufferedImage image = ImageIO.read(fileEntry);
        BufferedImage scaledImage = new BufferedImage(imageWidth, imageHeight, image.getType());
        Graphics2D g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, scaledImage.getWidth(), scaledImage.getHeight(), 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();

        ImageIO.write(scaledImage,"png", new File("target/t.png"));

        double[] forInput = new double[forInputSize];

        int i = 0;
        Raster raster = scaledImage.getData();
        for (int y = 0 ; y < scaledImage.getHeight() ; y++) {
            for (int x = 0; x < scaledImage.getWidth(); x++) {
                int[] pixel = raster.getPixel(x, y, (int[]) null);
                Color colour = new Color(pixel[0] , pixel[1] , pixel[2]);
                forInput[i++] = pixel[0] / 256.0f;
                forInput[i++] = pixel[1] / 256.0f;
                forInput[i++] = pixel[2] / 256.0f;
            }
        }

        Vec input = new Vec(forInput);
        return input;
    }

    private static void captureImages() throws AWTException, IOException, InterruptedException {
        Robot robot = new Robot();

        int i = 0;
        while (i < 100) {
            // Lowest graphics setting
            // Zoom out once with "O"
            // Cast
            // Right mouse button, rotate and tilt until the fishing float is in the far left hand middle of the screen
            // Right mouse button and down, as much overhead as possible
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        /*
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            rectangle = screenRect.union(gd.getDefaultConfiguration().getBounds());
        }
        */
            BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
            bufferedImage = bufferedImage.getSubimage(0,bufferedImage.getHeight()/4, bufferedImage.getWidth() / 6, bufferedImage.getHeight()/2);
            File file = new File("target/images/t_"+i+".png");
            Files.createDirectories(Paths.get(file.toURI()));
            boolean status = ImageIO.write(bufferedImage, "png", file);
            System.out.println("Screen Captured ? " + status + " File Path:- " + file.getAbsolutePath());
            Thread.sleep(500);
            i++;
        }
    }
}
