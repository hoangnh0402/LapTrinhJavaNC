package bai1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) {
		int port = 12345;
		StudentDatabase database = new StudentDatabase();

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("Server is running on port " + port);

			while (true) {
				Socket socket = serverSocket.accept();
				new Thread(new ClientHandler(socket, database)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
