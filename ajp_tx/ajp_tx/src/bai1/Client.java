package bai1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		String host = "localhost";
		int port = 12345;

		try (Socket socket = new Socket(host, port);
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.println("\t Chương trình quản lý sinh viên 2024");
				System.out.println("1. Thêm sinh viên");
				System.out.println("2. Sửa");
				System.out.println("3. Xóa");
				System.out.println("4. Tìm sinh viên theo mã");
				System.out.println("5. Danh sách sinh viên");
				System.out.println("6. Thoát");
				System.out.print("Chọn chức năng: ");
				int choice = Integer.parseInt(scanner.nextLine());

				switch (choice) {
				case 1 -> {
					System.out.print("Họ tên: ");
					String name = scanner.nextLine();
					System.out.print("Mã sinh viên: ");
					String studentCode = scanner.nextLine();
					System.out.print("Ngành học: ");
					String major = scanner.nextLine();
					System.out.print("Ngoại ngữ: ");
					String language = scanner.nextLine();
					System.out.print("Điểm tổng kết: ");
					float gpa = Float.parseFloat(scanner.nextLine());
					Student student = new Student(name, studentCode, major, language, gpa);

					oos.writeObject("ADD");
					oos.writeObject(student);
					System.out.println(ois.readObject());
				}
				case 2 -> {
					System.out.print("Mã sinh viên cần sửa: ");
					String studentId = scanner.nextLine();
					System.out.print("Họ tên: ");
					String name = scanner.nextLine();
					System.out.print("Ngành học: ");
					String major = scanner.nextLine();
					System.out.print("Ngoại ngữ: ");
					String language = scanner.nextLine();
					System.out.print("Điểm tổng kết: ");
					float gpa = Float.parseFloat(scanner.nextLine());
					Student updatedStudent = new Student(name, studentId, major, language, gpa);

					oos.writeObject("UPDATE");
					oos.writeObject(studentId);
					oos.writeObject(updatedStudent);
					System.out.println(ois.readObject());
				}
				case 3 -> {
					System.out.print("Mã sinh viên cần xóa: ");
					String studentId = scanner.nextLine();
					oos.writeObject("DELETE");
					oos.writeObject(studentId);
					System.out.println(ois.readObject());
				}
				case 4 -> {
					System.out.print("Mã sinh viên cần tìm: ");
					String studentId = scanner.nextLine();
					oos.writeObject("FIND");
					oos.writeObject(studentId);
					Object response = ois.readObject();
					if (response instanceof Student) {
						System.out.println(response);
					} else {
						System.out.println("Sinh viên không tồn tại.");
					}
				}
				case 5 -> {
					oos.writeObject("LIST");
					@SuppressWarnings("unchecked")
					List<Student> students = (List<Student>) ois.readObject();
					System.out.printf("%-20s %-10s %-15s %-10s %-5s%n", "Họ Tên", "MSSV", "Ngành", "Ngôn ngữ", "GPA");
					for (Student student : students) {
						System.out.println(student);
					}
				}
				case 6 -> {
					oos.writeObject("EXIT");
					System.out.println(ois.readObject());
					return;
				}
				default -> System.out.println("Chọn sai chức năng.");
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
