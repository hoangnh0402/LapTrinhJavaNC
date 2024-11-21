package server;

import dao.StudentDAO;
import model.Student;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345; // Cổng mà server lắng nghe
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static StudentDAO studentDAO;

    public static void main(String[] args) {
        studentDAO = new StudentDAO();
        
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running...");
            
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Client connected...");
                
                // Tạo luồng xử lý yêu cầu từ client
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lớp xử lý yêu cầu từ client
    static class ClientHandler extends Thread {
        private Socket clientSocket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                inputStream = new DataInputStream(clientSocket.getInputStream());
                outputStream = new DataOutputStream(clientSocket.getOutputStream());

                String request;
                while (true) {
                    try {
                        // Kiểm tra xem có dữ liệu không
                        if (inputStream.available() > 0) {
                            request = inputStream.readUTF();
                            System.out.println("Request from client: " + request);
                            
                            // Chuyển hướng các yêu cầu từ client
                            if (request.startsWith("ADD")) {
                                addStudent(request);
                            } else if (request.startsWith("UPDATE")) {
                                updateStudent(request);
                            } else if (request.startsWith("DELETE")) {
                                deleteStudent(request);
                            } else if (request.equals("GET_ALL")) {
                                getAllStudents();
                            }
                        }
                    } catch (EOFException e) {
                        System.out.println("Client disconnected.");
                        break;  // Client đã ngắt kết nối
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;  // Nếu có lỗi I/O, dừng vòng lặp
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void addStudent(String request) {
            try {
                String[] parts = request.split(";");
                String id = parts[1];
                String name = parts[2];
                String major = parts[3];
                String language = parts[4];
                float gpa = Float.parseFloat(parts[5]);

                Student student = new Student(id, name, major, language, gpa);
                studentDAO.addStudent(student);
                
                outputStream.writeUTF("Student added successfully!");
                outputStream.flush();  // Đảm bảo dữ liệu được gửi đi ngay lập tức
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void updateStudent(String request) {
            try {
                String[] parts = request.split(";");
                String id = parts[1];
                String name = parts[2];
                String major = parts[3];
                String language = parts[4];
                float gpa = Float.parseFloat(parts[5]);

                Student student = new Student(id, name, major, language, gpa);
                studentDAO.updateStudent(student);
                
                outputStream.writeUTF("Student updated successfully!");
                outputStream.flush();  // Đảm bảo dữ liệu được gửi đi ngay lập tức
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void deleteStudent(String request) {
            try {
                String[] parts = request.split(";");
                String id = parts[1];
                studentDAO.deleteStudent(id);
                
                outputStream.writeUTF("Student deleted successfully!");
                outputStream.flush();  // Đảm bảo dữ liệu được gửi đi ngay lập tức
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void getAllStudents() {
            try {
                List<Student> students = studentDAO.getAllStudents();
                StringBuilder response = new StringBuilder();
                
                for (Student student : students) {
                    response.append(student.toString()).append("\n");
                }
                
                outputStream.writeUTF(response.toString());
                outputStream.flush();  // Đảm bảo dữ liệu được gửi đi ngay lập tức
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
