/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.Student;

public class Server {
    private static final int PORT = 12345;
    private static ArrayList<Student> students = new ArrayList<>();
    private static final String FILE_NAME = "Data.dat";

    public static void main(String[] args) {
        try {
            loadData(); // Load existing data from file
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New connection from: " + socket.getInetAddress().getHostName());
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load data from file
    private static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            students = (ArrayList<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous data found, starting fresh.");
        }
    }

    // Save data to file
    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(students);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle client requests
    static class ClientHandler extends Thread {
        private Socket socket;
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream = new ObjectOutputStream(socket.getOutputStream());

                String action;
                while ((action = (String) inputStream.readObject()) != null) {
                    switch (action) {
                        case "ADD":
                            handleAdd();
                            break;
                        case "UPDATE":
                            handleUpdate();
                            break;
                        case "DELETE":
                            handleDelete();
                            break;
                        case "SEARCH":
                            handleSearch();
                            break;
                        case "EXIT":
                            socket.close();
                            return;
                        default:
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void handleAdd() throws IOException, ClassNotFoundException {
            Student student = (Student) inputStream.readObject();
            students.add(student);
            saveData();
            outputStream.writeObject("Student added successfully!");
            displayStudentInfo(student);
        }

        private void handleUpdate() throws IOException, ClassNotFoundException {
            String id = (String) inputStream.readObject();
            Student updatedStudent = (Student) inputStream.readObject();
            boolean found = false;

            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getId().equals(id)) {
                    students.set(i, updatedStudent);
                    found = true;
                    break;
                }
            }

            if (found) {
                saveData();
                outputStream.writeObject("Student updated successfully!");
                displayStudentInfo(updatedStudent);
            } else {
                outputStream.writeObject("Student not found!");
            }
        }

        private void handleDelete() throws IOException, ClassNotFoundException {
            String id = (String) inputStream.readObject();
            boolean removed = students.removeIf(student -> student.getId().equals(id));

            if (removed) {
                saveData();
                outputStream.writeObject("Student deleted successfully!");
            } else {
                outputStream.writeObject("Student not found!");
            }
        }

        private void handleSearch() throws IOException, ClassNotFoundException {
            String id = (String) inputStream.readObject();
            for (Student student : students) {
                if (student.getId().equals(id)) {
                    outputStream.writeObject(student);
                    return;
                }
            }
            outputStream.writeObject("Student not found!");
        }

        private void displayStudentInfo(Student student) {
            String hostName = socket.getInetAddress().getHostName();
            String ipAddress = socket.getInetAddress().getHostAddress();
            int port = socket.getPort();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // Hiển thị thông tin sinh viên dưới dạng bảng
            System.out.println("Received student data:");
            System.out.println(String.format("%-15s%-20s%-15s%-15s%-10s", "ID", "Name", "Major", "Language", "GPA"));
            System.out.println(student);
            System.out.println("From host: " + hostName + " (IP: " + ipAddress + ", Port: " + port + ")");
            System.out.println("Time: " + time);
        }
    }
}
