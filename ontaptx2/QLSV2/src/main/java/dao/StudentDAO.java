package dao;

import model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Thêm sinh viên vào cơ sở dữ liệu
    public void addStudent(Student student) {
        String query = "INSERT INTO students (id, name, major, language, gpa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, student.getId());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getMajor());
            stmt.setString(4, student.getLanguage());
            stmt.setFloat(5, student.getGpa());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Sửa thông tin sinh viên
    public void updateStudent(Student student) {
        String query = "UPDATE students SET name = ?, major = ?, language = ?, gpa = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getMajor());
            stmt.setString(3, student.getLanguage());
            stmt.setFloat(4, student.getGpa());
            stmt.setString(5, student.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa sinh viên khỏi cơ sở dữ liệu
    public void deleteStudent(String studentId) {
        String query = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách tất cả sinh viên
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String major = rs.getString("major");
                String language = rs.getString("language");
                float gpa = rs.getFloat("gpa");
                students.add(new Student(id, name, major, language, gpa));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
}

