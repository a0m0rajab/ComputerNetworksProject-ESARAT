package esarat.screenShare;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class client {

    Socket socket;
    boolean isRunning = true;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ekrani gormesi icin asagidaki bilgileri giriniz:");
        System.out.println("Port:");
        String port = sc.next();
        System.out.println("IP:");
        String ipAdd = sc.next();
        client clnt = new client();
        System.out.println("password:");
        String password = sc.next();
        clnt.start(ipAdd, Integer.parseInt(port), password);

        while (clnt.isRunning) {
            System.out.println("options:\n1:to end");
            if (sc.hasNext()) {
                if (sc.nextInt() == 1) {
                    clnt.close();
                }
            }
        }

    }

    public void close() {
        try {
            if (socket != null) {
                isRunning = false;
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(String serverAddr, int port, String password) {
        JFrame frame = new JFrame();
        ImagePanel panel = new ImagePanel();
        frame.setResizable(true);
        frame.add(panel);
        frame.pack();
         frame.setVisible(true);
        try {
            BufferedImage image;
            while (isRunning) {
                socket = new Socket(serverAddr, port);
                boolean result = checkPassword(socket, password);
                System.out.println(result);
                if (result) {
                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    image = ImageIO.read(inputStream);
                    panel.setImg(image);
                    panel.repaint();
                    socket.close();
                   
                } else {
                     frame.setVisible(false);
                    isRunning = false;
                    System.out.println("Wrong password");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkPassword(Socket socket, String password) {
        sendPassword(socket, password);
        return getPasswordChecker(socket);

    }

    public void sendPassword(Socket socket, String password) {
        DataOutputStream dOut;
        try {
            dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeUTF(password);
            dOut.flush(); // Send off the data
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean getPasswordChecker(Socket socket) {
        DataInputStream dIn;
        try {
            dIn = new DataInputStream(socket.getInputStream());
            boolean result = dIn.readBoolean();
            System.out.println(result);
            return result;
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }
}
