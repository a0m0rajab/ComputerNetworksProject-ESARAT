import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class server {
    ServerSocket  server;
    Socket socket;
    boolean isRunning = true;
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ekrani paylasmasi icin asagidaki bilgileri giriniz:");
        System.out.println("Port:");
        String port = sc.next();
        server srv = new server();
        srv.start(Integer.parseInt(port));
        while (true) {
            System.out.println("input:");
            if (sc.hasNext()) {
               
            }
        }


    }

    private void close(){
		try {
            isRunning= false;
			socket.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    private void start(int port) {
       
		try {
			server = new ServerSocket(port);
			Robot rbt = new Robot();

			while(isRunning){
				try{
					socket = server.accept();
					InetAddress addr = socket.getInetAddress();
					System.out.println("Baglandi " + addr.getCanonicalHostName()+ ":" + addr.getHostAddress());
					ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream());
					BufferedImage img = rbt.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					ImageIO.write(img, "jpg", outstream);
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}  catch (AWTException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} 

	}
}