package com.example.bootIML.jcodec;

import com.example.bootIML.jcodec.javase.api.awt.AWTSequenceEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println("jcodecDemo "+ com.example.bootIML.jcodec.Main.class.getPackage().getName());
        System.out.println();

        try {
            testWriting(new File("video.mp4"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void testWriting(File file) throws IOException {
        System.out.println("Writing " + file);

        // Create a buffered image for this format
        BufferedImage img = createImage(24,320, 160);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AWTSequenceEncoder encoder = AWTSequenceEncoder.create2997Fps(file);
        try {
            // initialize the animation
            Random rnd = new Random(0); // use seed 0 to get reproducable output
            g.setBackground(Color.WHITE);
            g.clearRect(0, 0, img.getWidth(), img.getHeight());

            for (int i = 0; i < 100; i++) {
                // Create an animation frame
                g.setColor(new Color(rnd.nextInt()));
                g.fillOval(rnd.nextInt(img.getWidth() - 30), rnd.nextInt(img.getHeight() - 30), 30, 30);

                // write it to the writer
                encoder.encodeImage(img);
            }

        } finally {
            // Close the writer
            if (encoder != null) {
                encoder.finish();
            }

            // Dispose the graphics object
            g.dispose();
        }
    }

    /** Creates a buffered image of the specified depth with a random color palette.*/
    private static BufferedImage createImage(int depth, int width, int height) {

        Random rnd = new Random(0); // use seed 0 to get reproducable output
        BufferedImage img;
        switch (depth) {
            case 24:
            default: {
                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                break;
            }
            case 8: {
                byte[] red = new byte[256];
                byte[] green = new byte[256];
                byte[] blue = new byte[256];
                for (int i = 0; i < 255; i++) {
                    red[i] = (byte) rnd.nextInt(256);
                    green[i] = (byte) rnd.nextInt(256);
                    blue[i] = (byte) rnd.nextInt(256);
                }
                rnd.setSeed(0); // set back to 0 for reproducable output
                IndexColorModel palette = new IndexColorModel(8, 256, red, green, blue);
                img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, palette);
                break;
            }
            case 4: {
                byte[] red = new byte[16];
                byte[] green = new byte[16];
                byte[] blue = new byte[16];
                for (int i = 0; i < 15; i++) {
                    red[i] = (byte) rnd.nextInt(16);
                    green[i] = (byte) rnd.nextInt(16);
                    blue[i] = (byte) rnd.nextInt(16);
                }
                rnd.setSeed(0); // set back to 0 for reproducable output
                IndexColorModel palette = new IndexColorModel(4, 16, red, green, blue);
                img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, palette);
                break;
            }
        }
        return img;
    }
}
