package core;

import java.util.ArrayList;
import java.util.Collections;

public class EncryptedImage extends ImageHandler{
        private String message;
        private int key;
        private int messageLength;
        private ArrayList<Integer> randomArray;

        public EncryptedImage(String filePath, String message) {
            super(filePath);
            super.logFile.createLogFile();
            this.message = message;
            this.messageLength = message.length();
            this.randomArray = new ArrayList<>(256);
        }

        // Generate a random key
        private void generateRandomKey() {
            key = (int) (Math.random() * 256);
            logFile.writeLog("Random key generated: " + key);
        }

        // Change the blue values of the pixels
        private void changeBlueValues() {
            int[][] pixelData = getPixelData();
            for (int i = 0; i < getImage().getWidth(); i++) {
                for (int j = 0; j < getImage().getHeight(); j++) {
                    int index = i * getImage().getHeight() + j;
                    int[] pixel = pixelData[index];
                    if (pixel[2] == key) {
                        if (pixel[2] == 0) {
                            pixel[2] = 1;
                        } else {
                            pixel[2] = pixel[2] - 1;
                        }
                        changePixel(index, pixel);
                    }
                }
            }
            logFile.writeLog("Blue values changed.");
        }

        // Set the information of the zero pixel
        private void setZeroPixel() {
            int[] zeroPixel = {messageLength, key, getPixelData()[0][2]};
            changePixel(0, zeroPixel);
            logFile.writeLog("Zero pixel set: " + messageLength + " " + key + " " + getPixelData()[0][2]);
        }

        // Generate a random array
        private void generateRandomArray() {
            ArrayList<Integer> fullArray = new ArrayList<>();
            for (int i = 0; i < 256; i++) {
                fullArray.add(i);
            }
            Collections.shuffle(fullArray);
            randomArray = new ArrayList<>(fullArray.subList(0, messageLength));
            logFile.writeLog("Random array generated.");
        }

        // Counting sort algorithm
        private void countingSort() {
            int max = 255; // Maximum possible value

            ArrayList<Integer> count = new ArrayList<>(max + 1);

            for (int i = 0; i <= max; i++) {
                count.add(0);
            }

            for (int num : randomArray) {
                count.set(num, count.get(num) + 1);
            }

            int index = 0;
            for (int i = 0; i <= max; i++) {
                if (count.get(i) > 0) {
                    randomArray.set(index++, i);
                }
            }
            logFile.writeLog("Random array sorted.");
        }

        // Encrypt the message
        public void encryptMessage() {
            ArrayList<Integer> indexList = new ArrayList<>();
            for (int i = 1; i < getImage().getWidth() * getImage().getHeight(); i++) {
                indexList.add(i);
            }
            Collections.shuffle(indexList);

            generateRandomKey();
            changeBlueValues();
            generateRandomArray();
            countingSort();
            setZeroPixel();

            int[][] pixelData = getPixelData();
            for (int i = 0; i < messageLength; i++) {
                int index = indexList.get(i);
                int[] pixel = pixelData[index];
                pixel[0] = randomArray.get(i);
                pixel[1] = (int) message.charAt(i);
                pixel[2] = key;
                changePixel(index, pixel);
            }
            logFile.writeLog("Message encrypted.");
            saveImage();
        }
}
