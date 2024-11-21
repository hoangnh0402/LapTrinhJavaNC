package bai1;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private Socket socket;
	private StudentDatabase database;

	public ClientHandler(Socket socket, StudentDatabase database) {
		this.socket = socket;
		this.database = database;
	}

	@Override
	public void run() {
		try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
			String clientAddress = socket.getInetAddress().getHostAddress();
			int clientPort = socket.getPort();
			System.out.println("Connected to client: " + clientAddress + ":" + clientPort);

			while (true) {
				try {
					String action = (String) ois.readObject();
					switch (action) {
					case "ADD" -> {
						Student student = (Student) ois.readObject();
						database.addStudent(student);
						oos.writeObject("Student added successfully");
					}
					case "UPDATE" -> {
						String studentCode = (String) ois.readObject();
						Student updatedStudent = (Student) ois.readObject();
						database.updateStudent(studentCode, updatedStudent);
						oos.writeObject("Student updated successfully");
					}
					case "DELETE" -> {
						String studentCode = (String) ois.readObject();
						database.deleteStudent(studentCode);
						oos.writeObject("Student deleted Successfully");
					}
					case "LIST" -> {
						oos.writeObject(database.getAllStudents());
					}
					case "FIND" -> {
						String studentCode = (String) ois.readObject();
						Student foundStudent = database.findStudent(studentCode);
						oos.writeObject(foundStudent != null ? foundStudent : "Student not found");
					}
					case "EXIT" -> {
						System.out.println(clientAddress + ":" + clientPort + " disconnected.");
						oos.writeObject("Disconnected");
						return;
					}
					}
				} catch (EOFException e) {
					System.out.println("Client disconnected.");
					break;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
