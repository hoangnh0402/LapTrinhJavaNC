package bai1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentDatabase {
	private static final String FILE_NAME = "Data.dat";
	private List<Student> students;

	public StudentDatabase() {
		this.students = readFromFile();
	}

	public synchronized void addStudent(Student student) {
		students.add(student);
		saveToFile();
	}

	public synchronized void updateStudent(String studentCode, Student updatedStudent) {
		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).getStudentCode().equals(studentCode)) {
				students.set(i, updatedStudent);
				saveToFile();
				return;
			}
		}
	}

	public synchronized void deleteStudent(String studentCode) {
		students.removeIf(student -> student.getStudentCode().equals(studentCode));
		saveToFile();
	}

	public synchronized Student findStudent(String studentCode) {
		for (Student student : students) {
			if (student.getStudentCode().equals(studentCode)) {
				return student;
			}
		}
		return null;
	}

	public synchronized List<Student> getAllStudents() {
		this.students = readFromFile();
		return students;
	}

	private void saveToFile() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
			oos.writeObject(students);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private List<Student> readFromFile() {
		File file = new File(FILE_NAME);
		if (!file.exists()) {
			return new ArrayList<Student>();
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
			return (List<Student>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<Student>();
		}
	}

}
