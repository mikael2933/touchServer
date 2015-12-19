package xyz.multimikael;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.InputEvent;

public class Main {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ObjectInputStream inputStream;
        Socket connectionSocket;
        String command = null;
        Robot robot = null;

        try{
            robot = new Robot();
        } catch (AWTException awtException) {
            System.out.println(awtException);
        }

        System.out.println("Starting Server...");
        try{
            serverSocket = new ServerSocket(4444);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        System.out.println(serverSocket.getInetAddress().getHostName());
        System.out.println("Waiting for connection...");
        try{
            connectionSocket = serverSocket.accept();
            inputStream = new ObjectInputStream(connectionSocket.getInputStream());
            System.out.println("Connected to: " + connectionSocket.getInetAddress().getHostName());
            do {
                try {
                    command = (String) inputStream.readObject();
                    System.out.println(command);
                    if (command.equals("leftClick")) {
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    } else if (command.equals("rightClick")) {
                        robot.mousePress(InputEvent.BUTTON3_MASK);
                        robot.mouseRelease(InputEvent.BUTTON3_MASK);
                    }
                    if (command.matches("x:(.*)")) {
                        String[] parts = command.split(":");
                        double newX = MouseInfo.getPointerInfo().getLocation().getX() + Double.parseDouble(parts[1]);
                        robot.mouseMove((int)newX, (int)MouseInfo.getPointerInfo().getLocation().getY());
                    }
                    if (command.matches("y:(.*)")) {
                        String[] parts = command.split(":");
                        double newY = MouseInfo.getPointerInfo().getLocation().getY() + Double.parseDouble(parts[1]);
                        robot.mouseMove((int)MouseInfo.getPointerInfo().getLocation().getX(),(int)newY);
                    }
                } catch (ClassNotFoundException classNotFoundException) {
                    System.out.println("ClassNotFoundException");
                }
            } while (!command.equals("$end"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
