import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Server {
	private static final int PORT = 12345;
	private static DatabaseHandler database;

	public static void main(String[] args) {
		try {
			database = new DatabaseHandler();
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Server is listening on port " + PORT);

			JFrame frame = new JFrame("Server - Student Management");
			JTable table = new JTable(
					new DefaultTableModel(new Object[] { "MSV", "Ho ten", "Nganh", "Ngon ngu", "GPA" }, 0));
			frame.add(new JScrollPane(table));
			frame.setSize(600, 400);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);

			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setRowCount(0);
			List<Student> students = database.getAllStudents();
			for (Student s : students) {
				model.addRow(new Object[] { s.getId(), s.getName(), s.getMajor(), s.getLanguage(), s.getGpa() });
			}

			while (true) {
				Socket clientSocket = serverSocket.accept();
				new Thread(new ClientHandler(clientSocket, table)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class ClientHandler implements Runnable {
		private Socket socket;
		private JTable table;

		public ClientHandler(Socket socket, JTable table) {
			this.socket = socket;
			this.table = table;
		}

		@Override
		public void run() {
			try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
				String clientAddress = socket.getInetAddress().getHostAddress();
				int clientPort = socket.getPort();
				System.out.println("Connected to client: " + clientAddress + ":" + clientPort);

				while (true) {
					String action = (String) ois.readObject();

					switch (action) {
					case "GETALL" -> {
						List<Student> students = database.getAllStudents();
						oos.writeObject(students);
						updateTable();
					}
					case "ADD" -> {
						Student student = (Student) ois.readObject();
						database.addStudent(student);
						updateTable();
						oos.writeObject("Added successfully");
					}
					case "UPDATE" -> {
						Student student = (Student) ois.readObject();
						database.updateStudent(student);
						updateTable();
						oos.writeObject("Updated successfully");
					}
					case "DELETE" -> {
						String id = (String) ois.readObject();
						database.deleteStudent(id);
						updateTable();
						oos.writeObject("Deleted successfully");
					}
					case "FIND" -> {
						String id = (String) ois.readObject();
						Student student = database.findStudent(id);
						oos.writeObject(student);
					}
					case "EXIT" -> {
						oos.writeObject("Disconnected.");
						return;
					}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void updateTable() throws SQLException {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.setRowCount(0);
			List<Student> students = database.getAllStudents();
			for (Student s : students) {
				model.addRow(new Object[] { s.getId(), s.getName(), s.getMajor(), s.getLanguage(), s.getGpa() });
			}
		}
	}
}
