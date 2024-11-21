package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
    private JTextField idField, nameField, gpaField;
    private JRadioButton cnntButton, ktpmButton, khmtButton;
    private JRadioButton anhButton, ngaButton, phapButton;
    private JTextArea resultArea;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    public Client() {
        setTitle("Chương Trình Quản Lý Sinh Viên - Client");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        // Khởi tạo các thành phần
        idField = new JTextField(20);
        nameField = new JTextField(20);
        gpaField = new JTextField(20);

        cnntButton = new JRadioButton("CNTT");
        ktpmButton = new JRadioButton("KTPM");
        khmtButton = new JRadioButton("KHMT");

        anhButton = new JRadioButton("Anh");
        ngaButton = new JRadioButton("Nga");
        phapButton = new JRadioButton("Pháp");

        ButtonGroup majorGroup = new ButtonGroup();
        majorGroup.add(cnntButton);
        majorGroup.add(ktpmButton);
        majorGroup.add(khmtButton);

        ButtonGroup languageGroup = new ButtonGroup();
        languageGroup.add(anhButton);
        languageGroup.add(ngaButton);
        languageGroup.add(phapButton);

        resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);

        // Các nút điều khiển
        JButton addButton = new JButton("Thêm");
        JButton updateButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton searchButton = new JButton("Tìm kiếm");

        // Thêm các thành phần vào giao diện
        add(new JLabel("ID Sinh Viên"));
        add(idField);
        add(new JLabel("Tên Sinh Viên"));
        add(nameField);
        add(new JLabel("Điểm Tổng Kết"));
        add(gpaField);

        add(new JLabel("Ngành Học"));
        add(cnntButton);
        add(ktpmButton);
        add(khmtButton);

        add(new JLabel("Ngoại Ngữ"));
        add(anhButton);
        add(ngaButton);
        add(phapButton);

        add(addButton);
        add(updateButton);
        add(deleteButton);
        add(searchButton);

        add(new JScrollPane(resultArea));

        // Kết nối đến server
        try {
            socket = new Socket("localhost", 12345);
            outputStream = new DataOutputStream(socket.getOutputStream());
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Xử lý sự kiện nút
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        setVisible(true);
    }

    private void addStudent() {
        try {
            String id = idField.getText();
            String name = nameField.getText();
            float gpa = Float.parseFloat(gpaField.getText());

            String major = cnntButton.isSelected() ? "CNTT" : (ktpmButton.isSelected() ? "KTPM" : "KHMT");
            String language = anhButton.isSelected() ? "Anh" : (ngaButton.isSelected() ? "Nga" : "Pháp");

            String request = "ADD;" + id + ";" + name + ";" + major + ";" + language + ";" + gpa;
            
            // Gửi yêu cầu đến server
            outputStream.writeUTF(request);
            outputStream.flush();  // Đảm bảo dữ liệu được gửi ngay lập tức

            // Đọc phản hồi từ server
            String response = inputStream.readUTF();
            
            // Xử lý phản hồi từ server
            JOptionPane.showMessageDialog(this, response);
            updateResultArea(response);  // Cập nhật kết quả sau khi thêm

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error in sending data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateStudent() {
        try {
            String id = idField.getText();
            String name = nameField.getText();
            float gpa = Float.parseFloat(gpaField.getText());

            String major = cnntButton.isSelected() ? "CNTT" : (ktpmButton.isSelected() ? "KTPM" : "KHMT");
            String language = anhButton.isSelected() ? "Anh" : (ngaButton.isSelected() ? "Nga" : "Pháp");

            String request = "UPDATE;" + id + ";" + name + ";" + major + ";" + language + ";" + gpa;
            
            // Gửi yêu cầu đến server
            outputStream.writeUTF(request);
            outputStream.flush();  // Đảm bảo dữ liệu được gửi ngay lập tức

            // Đọc phản hồi từ server
            String response = inputStream.readUTF();
            
            // Xử lý phản hồi từ server
            JOptionPane.showMessageDialog(this, response);
            updateResultArea(response);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error in sending data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteStudent() {
        try {
            String id = idField.getText();
            String request = "DELETE;" + id;
            
            // Gửi yêu cầu đến server
            outputStream.writeUTF(request);
            outputStream.flush();  // Đảm bảo dữ liệu được gửi ngay lập tức

            // Đọc phản hồi từ server
            String response = inputStream.readUTF();
            
            // Xử lý phản hồi từ server
            JOptionPane.showMessageDialog(this, response);
            updateResultArea(response);
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error in sending data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateResultArea(String response) {
        // Cập nhật khu vực kết quả (result area) sau mỗi thao tác
        resultArea.setText("Kết quả:\n" + response);
    }

    public static void main(String[] args) {
        new Client();
    }
}
