

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
	private static final String URL = "jdbc:mysql://localhost:3306/student_db";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "123456";

	private Connection connection;

	public DatabaseHandler() throws SQLException {
		connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

	public List<Student> getAllStudents() throws SQLException {
		List<Student> students = new ArrayList<Student>();
		String query = "SELECT * FROM student";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			students.add(new Student(rs.getString("id"), rs.getString("name"), rs.getString("major"),
					rs.getString("language"), rs.getFloat("gpa")));
		}
		return students;
	}

	public void addStudent(Student student) throws SQLException {
		String query = "INSERT INTO student VALUES (?, ?, ?, ?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(query);
		pstmt.setString(1, student.getId());
		pstmt.setString(2, student.getName());
		pstmt.setString(3, student.getMajor());
		pstmt.setString(4, student.getLanguage());
		pstmt.setFloat(5, student.getGpa());
		pstmt.executeUpdate();
	}

	public void updateStudent(Student student) throws SQLException {
		String query = "UPDATE student SET name = ?, major = ?, language = ?, gpa = ? WHERE id = ?";
		PreparedStatement pstmt = connection.prepareStatement(query);
		pstmt.setString(1, student.getName());
		pstmt.setString(2, student.getMajor());
		pstmt.setString(3, student.getLanguage());
		pstmt.setFloat(4, student.getGpa());
		pstmt.setString(5, student.getId());
		pstmt.executeUpdate();
	}

	public void deleteStudent(String id) throws SQLException {
		String query = "DELETE FROM student WHERE id = ?";
		PreparedStatement pstmt = connection.prepareStatement(query);
		pstmt.setString(1, id);
		pstmt.executeUpdate();
	}

	public Student findStudent(String id) throws SQLException {
		String query = "SELECT * FROM student WHERE id = ?";
		PreparedStatement pstmt = connection.prepareStatement(query);
		pstmt.setString(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return new Student(rs.getString("id"), rs.getString("name"), rs.getString("major"),
					rs.getString("language"), rs.getFloat("gpa"));
		}
		return null;
	}
}
