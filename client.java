import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.util.Scanner;

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
        clnt.start(ipAdd, Integer.parseInt(port));
        
        while (clnt.isRunning) {
            System.out.println("options:\n1:to end");
            if (sc.hasNext()) {
               if(sc.nextInt() == 1){
                   clnt.close();
               }
            }
        }


    }
    private void close(){
		try {
            isRunning= false;
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    private void start(String serverAddr, int port) {
		JFrame frame = new JFrame();
		ImagePanel panel = new ImagePanel();
		frame.setResizable(true);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		try {
			BufferedImage image;
			while(isRunning){
				socket = new Socket(serverAddr, port);
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				image = ImageIO.read(inputStream);
				panel.setImg(image);
				panel.repaint();
				socket.close();
			}


		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}