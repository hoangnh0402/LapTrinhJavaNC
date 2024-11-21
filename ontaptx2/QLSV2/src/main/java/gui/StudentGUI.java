package gui;

import dao.StudentDAO;
import model.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentGUI extends JFrame {
    private JTextField idField, nameField, gpaField;
    private JRadioButton cnntButton, ktpmButton, khmtButton;
    private JRadioButton anhButton, ngaButton, phapButton;
    private JTextArea resultArea;
    private StudentDAO studentDAO;

    public StudentGUI() {
        studentDAO = new StudentDAO();

        setTitle("Chương Trình Quản Lý Sinh Viên");
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

    // Thêm sinh viên
    private void addStudent() {
        String id = idField.getText();
        String name = nameField.getText();
        float gpa = Float.parseFloat(gpaField.getText());

        String major = cnntButton.isSelected() ? "CNTT" : (ktpmButton.isSelected() ? "KTPM" : "KHMT");
        String language = anhButton.isSelected() ? "Anh" : (ngaButton.isSelected() ? "Nga" : "Pháp");

        Student student = new Student(id, name, major, language, gpa);
        studentDAO.addStudent(student);

        JOptionPane.showMessageDialog(this, "Sinh viên đã được thêm!");
    }

    // Sửa sinh viên
    private void updateStudent() {
        String id = idField.getText();
        String name = nameField.getText();
        float gpa = Float.parseFloat(gpaField.getText());

        String major = cnntButton.isSelected() ? "CNTT" : (ktpmButton.isSelected() ? "KTPM" : "KHMT");
        String language = anhButton.isSelected() ? "Anh" : (ngaButton.isSelected() ? "Nga" : "Pháp");

        Student student = new Student(id, name, major, language, gpa);
        studentDAO.updateStudent(student);

        JOptionPane.showMessageDialog(this, "Sinh viên đã được sửa!");
    }

    // Xóa sinh viên
    private void deleteStudent() {
        String id = idField.getText();
        studentDAO.deleteStudent(id);

        JOptionPane.showMessageDialog(this, "Sinh viên đã được xóa!");
    }

    public static void main(String[] args) {
        new StudentGUI();
    }
}
