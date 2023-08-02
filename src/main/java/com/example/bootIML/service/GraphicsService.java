package com.example.bootIML.service;

import com.example.bootIML.graphics3D.Cube;
import com.example.bootIML.graphics3D.Matrix4;
import com.example.bootIML.graphics3D.Triangle;
import com.example.bootIML.graphics3D.Vertex;
import com.example.bootIML.jcodec.javase.api.awt.AWTSequenceEncoder;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class GraphicsService {
    double heading = 0.1;
    double pitch = 0.1;
    double roll = 0.1;
    int width = 360;
    int height = 360;
    Matrix4 transform;
    Matrix4[] xyzoTransform;
    Matrix4 currentTransform = new Matrix4(new double[] {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    });

    public byte[] createSpinCube (int x, int y, int z, int angularVelocity) {

        xyzoTransform = initTransform(heading, pitch, roll);
        transform = xyzoTransform[0]
                .multiply(xyzoTransform[1])
                .multiply(xyzoTransform[2]);

        String resultFile = System.getProperty("java.io.tmpdir") + File.separator + "spincubevideo.mp4";
        byte[] fileContent = new byte[0];
        try {
            AWTSequenceEncoder encoder = AWTSequenceEncoder.create2997Fps(new File(resultFile));
            for (int i = 0; i < 100; i++) {
                if (i % 1 == 0) {
                    transform = transform
                            .multiply(xyzoTransform[0])
                            .multiply(xyzoTransform[1])
                            .multiply(xyzoTransform[2]);
                    currentTransform = transform.multiply(xyzoTransform[3]);
                }
                BufferedImage img = drawImage(x, y, z, angularVelocity);
                // write it to the writer
                encoder.encodeImage(img);
            }
            encoder.finish();
            fileContent = FileUtils.readFileToByteArray(new File(resultFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    };

    BufferedImage drawImage (int pivotx, int pivoty, int pivotz, int angularVelocity) {

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        double[] zBuffer = new double[img.getWidth() * img.getHeight()];
        // initialize array with extremely far away depths
        for (int q = 0; q < zBuffer.length; q++) {
            zBuffer[q] = Double.NEGATIVE_INFINITY;
        }
        double viewportWidth = width;
        double viewportHeight = height;
        double fovAngle = Math.toRadians(60);
        double fov = Math.tan(fovAngle / 2) * 170;
        Cube cube = new Cube(100);
        
        for (Triangle t : cube.tris) {
            Vertex v1 = currentTransform.transform(t.v1);
            Vertex v2 = currentTransform.transform(t.v2);
            Vertex v3 = currentTransform.transform(t.v3);

            Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z, v2.w - v1.w);
            Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z, v3.w - v1.w);
            Vertex norm = new Vertex(
                    ab.y * ac.z - ab.z * ac.y,
                    ab.z * ac.x - ab.x * ac.z,
                    ab.x * ac.y - ab.y * ac.x,
                    1
            );
            double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
            norm.x /= normalLength;
            norm.y /= normalLength;
            norm.z /= normalLength;

            double angleCos = Math.abs(norm.z);

            v1.x = v1.x / (-v1.z) * fov;
            v1.y = v1.y / (-v1.z) * fov;
            v2.x = v2.x / (-v2.z) * fov;
            v2.y = v2.y / (-v2.z) * fov;
            v3.x = v3.x / (-v3.z) * fov;
            v3.y = v3.y / (-v3.z) * fov;

            v1.x += viewportWidth / 2;
            v1.y += viewportHeight / 2;
            v2.x += viewportWidth / 2;
            v2.y += viewportHeight / 2;
            v3.x += viewportWidth / 2;
            v3.y += viewportHeight / 2;

            int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
            int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
            int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
            int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

            double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                    double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                    double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
                    if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                        double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                        int zIndex = y * img.getWidth() + x;
                        if (zBuffer[zIndex] < depth) {
                            img.setRGB(x, y, getShade(t.color, angleCos).getRGB());
                            zBuffer[zIndex] = depth;
                        }
                    }
                }
            }

        }
        return img;
    }

    public static Color getShade(Color color, double shade) {
        double redLinear = Math.pow(color.getRed(), 2.4) * shade;
        double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
        double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;

        int red = (int) Math.pow(redLinear, 1/2.4);
        int green = (int) Math.pow(greenLinear, 1/2.4);
        int blue = (int) Math.pow(blueLinear, 1/2.4);

        return new Color(red, green, blue);
    }

    Matrix4[] initTransform(double heading, double pitch, double roll) {
        Matrix4 headingTransform = new Matrix4(new double[] {
                Math.cos(heading), 0, -Math.sin(heading), 0,
                0, 1, 0, 0,
                Math.sin(heading), 0, Math.cos(heading), 0,
                0, 0, 0, 1
        });
        Matrix4 pitchTransform = new Matrix4(new double[] {
                1, 0, 0, 0,
                0, Math.cos(pitch), Math.sin(pitch), 0,
                0, -Math.sin(pitch), Math.cos(pitch), 0,
                0, 0, 0, 1
        });
        Matrix4 rollTransform = new Matrix4(new double[] {
                Math.cos(roll), -Math.sin(roll), 0, 0,
                Math.sin(roll), Math.cos(roll), 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        });

        Matrix4 panOutTransform = new Matrix4(new double[] {
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                0, 0, -400, 1
        });

        Matrix4[] xyzoTransform = new Matrix4[]  {headingTransform, pitchTransform, rollTransform, panOutTransform};
        return xyzoTransform;
    }
}
