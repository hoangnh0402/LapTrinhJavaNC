package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import model.Student;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;

    public static void main(String[] args) throws ClassNotFoundException {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            while (true) {
                showMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addStudent(scanner);
                        break;
                    case 2:
                        updateStudent(scanner);
                        break;
                    case 3:
                        deleteStudent(scanner);
                        break;
                    case 4:
                        searchStudent(scanner);
                        break;
                    case 5:
                        outputStream.writeObject("EXIT");
                        outputStream.flush();
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showMenu() {
        System.out.println("CHƯƠNG TRÌNH QLSV-2024");
        System.out.println("------------------------------------------");
        System.out.println("1. Thêm sinh viên");
        System.out.println("2. Sửa sinh viên");
        System.out.println("3. Xóa sinh viên");
        System.out.println("4. Tìm sinh viên theo mã");
        System.out.println("5. Thoát");
        System.out.print("Chọn chức năng: ");
    }

    private static void addStudent(Scanner scanner) throws IOException, ClassNotFoundException {
        System.out.print("Nhập mã sinh viên: ");
        String id = scanner.nextLine();
        System.out.print("Nhập tên sinh viên: ");
        String name = scanner.nextLine();
        System.out.print("Nhập ngành học: ");
        String major = scanner.nextLine();
        System.out.print("Nhập ngoại ngữ: ");
        String language = scanner.nextLine();
        System.out.print("Nhập điểm tổng kết: ");
        float gpa = scanner.nextFloat();
        scanner.nextLine(); // Consume newline

        Student student = new Student(id, name, major, language, gpa);
        outputStream.writeObject("ADD");
        outputStream.writeObject(student);
        outputStream.flush();
        String response = (String) inputStream.readObject();
        System.out.println(response);
    }

    private static void updateStudent(Scanner scanner) throws IOException, ClassNotFoundException {
        System.out.print("Nhập mã sinh viên cần sửa: ");
        String id = scanner.nextLine();
        System.out.print("Nhập tên sinh viên mới: ");
        String name = scanner.nextLine();
        System.out.print("Nhập ngành học mới: ");
        String major = scanner.nextLine();
        System.out.print("Nhập ngoại ngữ mới: ");
        String language = scanner.nextLine();
        System.out.print("Nhập điểm tổng kết mới: ");
        float gpa = scanner.nextFloat();
        scanner.nextLine(); // Consume newline

        Student updatedStudent = new Student(id, name, major, language, gpa);
        outputStream.writeObject("UPDATE");
        outputStream.writeObject(id);
        outputStream.writeObject(updatedStudent);
        outputStream.flush();
        String response = (String) inputStream.readObject();
        System.out.println(response);
    }

    private static void deleteStudent(Scanner scanner) throws IOException, ClassNotFoundException {
        System.out.print("Nhập mã sinh viên cần xóa: ");
        String id = scanner.nextLine();
        outputStream.writeObject("DELETE");
        outputStream.writeObject(id);
        outputStream.flush();
        String response = (String) inputStream.readObject();
        System.out.println(response);
    }

    private static void searchStudent(Scanner scanner) throws IOException, ClassNotFoundException {
        System.out.print("Nhập mã sinh viên cần tìm: ");
        String id = scanner.nextLine();
        outputStream.writeObject("SEARCH");
        outputStream.writeObject(id);
        outputStream.flush();
        Object response = inputStream.readObject();
        if (response instanceof Student) {
            System.out.println("Thông tin sinh viên: ");
            System.out.println(response);
        } else {
            System.out.println(response);
        }
    }
}

