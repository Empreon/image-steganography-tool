package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import core.EncryptedImage;
import core.LogManager;

public class EncryptionWindow extends JFrame {
    private JTextField textfield;
    private JLabel messageLabel;
    private JLabel imageLabel;
    private String filePath = "image.png"; // Change with the image's name
    private String message;
    private LogManager logFile;

    public EncryptionWindow() {
        // Create a log file
        this.logFile = new LogManager();
        this.logFile.createLogFile();

        // Set the window properties
        setTitle("Image Encryption");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        logFile.writeLog("Window created.");

        // Add components to the window
        imageLabel = new JLabel();
        imageLabel.setBounds(165, 75, 50, 50);
        add(imageLabel);
        displayImage(filePath);

        messageLabel = new JLabel("Enter the message to encrypt:");
        messageLabel.setBounds(45, 135, 300, 25);
        add(messageLabel);


        textfield = new JTextField();
        textfield.setBounds(45, 160, 300, 25);
        add(textfield);

        JButton encrypButton = new JButton("Encrypt Image");
        encrypButton.setBounds(115, 190, 150, 25);
        add(encrypButton);

        // Add action listener to the button
        encrypButton.addActionListener(e -> {
            buttonAction();
        });

        // Close the log file when the window is closed
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                logFile.closeLogFile();
            }
        });
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInformationMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void clearTextField() {
        textfield.setText("");
    }

    public boolean textLengthCheck(String message) {
        if (message.length() == 0) {
            showErrorMessage("Message Length: " + message.length() + "\nPlease enter a message.");
            logFile.writeLog("Message length check failed. Message length: " + message.length());
            return false;
        }
        if (message.length() > 255) {
            showErrorMessage("Message Length: " + message.length() + "\nMessage length should be less than 256 characters.");
            logFile.writeLog("Message length check failed. Message length: " + message.length());
            return false;
        }
        return true;
    }

    public boolean textASCIIValueCheck(String message) {
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) > 255) {
                showErrorMessage("Invalid character: " + message.charAt(i) + "\nPlease enter ASCII characters only.");
                logFile.writeLog("ASCII value check failed. Invalid character: " + message.charAt(i));
                return false;
            }
        }
        return true;
    }
    
    private void displayImage(String filePath) {
        try {
            BufferedImage img = ImageIO.read(new File(filePath));
            ImageIcon icon = new ImageIcon(img);
            imageLabel.setIcon(icon);
        } catch (IOException e) {
            showErrorMessage("Failed to load image: " + e.getMessage());
        }
    }

    private void closeWindow() {
        logFile.writeLog("Window closed.");
        this.dispose();
    }

    private void buttonAction() {
        message = textfield.getText();
        if(textASCIIValueCheck(message)) {
            if (textLengthCheck(message)) {
                EncryptedImage encryptedImage = new EncryptedImage(filePath, message);
                encryptedImage.loadImage();
                encryptedImage.encryptMessage();
                showInformationMessage("Message has been successfully encrypted.");
                closeWindow();
            }
        }
        clearTextField();
    }
}