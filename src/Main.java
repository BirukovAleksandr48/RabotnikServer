import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static public ArrayList<Socket> sockets;
	static Scanner sc;
	static PrintWriter pw;
    static int port = 6666;
    
	public static void main(String[] args) 
	{
		try {
			sockets = new ArrayList<>();
			ServerSocket ss = new ServerSocket(port);

			while(true) {
				System.out.println("Wait for a client");
				Socket socket = ss.accept();
				System.out.println("Got a client");
				sockets.add(socket);
				
				new Thread(new UserThread(socket)).start();
			}
	   } catch (Exception e) {e.printStackTrace();}
	}
}
