package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class ImageHandler {
    private String filePath;
        private String[] fileInfo;
        private int[][] pixelData;
        private BufferedImage img;
        private BufferedImage processedImage;
        protected LogManager logFile;

        public ImageHandler(String filePath) {
            this.filePath = filePath;
            this.logFile = new LogManager();
            this.logFile.createLogFile();
        }

        // Load the image and store pixel data
        public void loadImage() {
            try {
                img = ImageIO.read(new File(filePath));
                logFile.writeLog("File loaded: " + filePath);

                fileInfo = filePath.split("\\.");
                int width = img.getWidth();
                int height = img.getHeight();
                logFile.writeLog("File name: " + fileInfo[0]);
                logFile.writeLog("File type: " + fileInfo[1]);
                logFile.writeLog("File dimensions: " + width + "x" + height);

                if (!fileInfo[1].equals("png")) {
                    img = convertToPNG();
                    fileInfo[1] = "png";
                }

                processedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                pixelData = new int[width * height][3];

                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        int pixel = img.getRGB(i, j);
                        pixelData[i * height + j][0] = (pixel >> 16) & 0xff;
                        pixelData[i * height + j][1] = (pixel >> 8) & 0xff;
                        pixelData[i * height + j][2] = (pixel) & 0xff;
                        processedImage.setRGB(i, j, pixel);
                    }
                }
                logFile.writeLog("Pixel data stored.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Convert JPG to PNG
        public BufferedImage convertToPNG() {
            try {
                File outputfile = new File(fileInfo[0] + ".png");
                ImageIO.write(img, "png", outputfile);
                logFile.writeLog("File converted to PNG: " + fileInfo[0] + ".png");
                return ImageIO.read(new File(fileInfo[0] + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Save the processed image
        public void saveImage() {
            try {
                File outputfile = new File(fileInfo[0] + "_encrypted." + fileInfo[1]);
                ImageIO.write(processedImage, fileInfo[1], outputfile);
                logFile.writeLog("File saved: " + fileInfo[0] + "_encrypted." + fileInfo[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Write pixel data to a text file
        public void writePixelData() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileInfo[0] + "_pixel_data.txt")))) {
                for (int i = 0; i < pixelData.length; i++) {
                    writer.write("Pixel " + i + ": " + pixelData[i][0] + " " + pixelData[i][1] + " " + pixelData[i][2] + "*\n");
                }
                logFile.writeLog("Pixel data written to file: " + fileInfo[0] + "_pixel_data.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Change the pixel at the given index
        protected void changePixel(int index, int[] pixel) {
            int y = index % img.getWidth();
            int x = index / img.getWidth();
            Color newColor = new Color(pixel[0], pixel[1], pixel[2]);
            processedImage.setRGB(x, y, newColor.getRGB());
            pixelData[index] = pixel;
            logFile.writeLog("Pixel " + index + " changed to: " + pixel[0] + " " + pixel[1] + " " + pixel[2]);
        }

        // Getters
        protected int[][] getPixelData() {
            return pixelData;
        }

        protected BufferedImage getImage() {
            return img;
        }

        protected BufferedImage getProcessedImage() {
            return processedImage;
        }
}
