package esarat.screenShare;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class server {

    ServerSocket server;
    Socket socket;
    boolean isRunning = true;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ekrani paylasmasi icin asagidaki bilgileri giriniz:");
        System.out.println("Port:");
        String port = sc.next();
        server srv = new server();
        System.out.println("Password:");
        String password = sc.next();
        srv.start(Integer.parseInt(port), password);
        while (true) {
            System.out.println("input:");

        }

    }

    public void close() {
        System.out.println("close called");
        try {
            if (socket != null) {
                isRunning = false;
                socket.close();
                server.close();

                System.out.println("Server socket closed");
            }
        } catch (IOException ex) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void start(int port, String password) {
        System.out.println("Share started");
        try {
            server = new ServerSocket(port);
            Robot rbt = new Robot();

            while (isRunning) {
                socket = server.accept();
                InetAddress addr = socket.getInetAddress();
                if (passwordWarpper(socket, password)) {
                    System.out.println("Baglandi " + addr.getCanonicalHostName() + ":" + addr.getHostAddress());
                    ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
                    BufferedImage img = rbt.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                    ImageIO.write(img, "jpg", outstream);
                    socket.close();
                } else {
                    isRunning = false;
                    socket.close();

                }
            }

        } catch (AWTException | IOException e) {
            Logger.getLogger(server.class.getName()).log(Level.SEVERE, null, e);

        }

    }

    public boolean passwordWarpper(Socket socket, String password) {
        Boolean statues = getPassword(socket, password);
        sendStatues(socket, statues);
        return statues;
    }

    public boolean getPassword(Socket socket, String password) {
        DataInputStream dIn;
        String clientPassword;
        try {
            dIn = new DataInputStream(socket.getInputStream());
            clientPassword = dIn.readUTF();
            return clientPassword.equals(password);
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void sendStatues(Socket socket, boolean statues) {
        DataOutputStream dOut;
        try {
            dOut = new DataOutputStream(socket.getOutputStream());
            dOut.writeBoolean(statues);
            dOut.flush(); // Send off the data
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
