package bai2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String HOST = "localhost";
	private static final int PORT = 12345;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JPanel contentPane;
	private JTextField txtName, txtId, txtGpa;
	private JRadioButton rdbCNTT, rdbKTPM, rdbKHMT;
	private JCheckBox chkEnglish, chkFrench, chkRussian;
	private JTextArea txtResult;
	private JButton btnAdd, btnEdit, btnDelete, btnSearch, btnExit;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Client() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("CHƯƠNG TRÌNH QUẢN LÝ SINH VIÊN - 2024", SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(0, 0, 255));
		lblNewLabel.setFont(lblNewLabel.getFont().deriveFont(lblNewLabel.getFont().getStyle() | Font.BOLD, 16f));
		lblNewLabel.setBounds(10, 0, 684, 43);
		contentPane.add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBounds(10, 33, 684, 337);
		contentPane.add(panel);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 11, 664, 256);
		panel.add(panel_1);
		panel_1.setLayout(new GridLayout(1, 2, 0, 0));

		JPanel inputPanel = new JPanel();
		panel_1.add(inputPanel);
		inputPanel.setBorder(BorderFactory.createTitledBorder("NHAP"));
		inputPanel.setLayout(null);
		JLabel label = new JLabel("Ho va ten: ", SwingConstants.RIGHT);
		label.setBounds(6, 18, 85, 34);
		inputPanel.add(label);
		txtName = new JTextField();
		txtName.setBounds(101, 18, 194, 34);
		inputPanel.add(txtName);

		JLabel label_1 = new JLabel("Ma sinh vien: ", SwingConstants.RIGHT);
		label_1.setBounds(6, 57, 85, 34);
		inputPanel.add(label_1);
		txtId = new JTextField();
		txtId.setBounds(101, 57, 157, 34);
		inputPanel.add(txtId);

		JLabel label_2 = new JLabel("Nganh hoc: ", SwingConstants.RIGHT);
		label_2.setBounds(6, 96, 85, 34);
		inputPanel.add(label_2);
		JPanel majorPanel = new JPanel();
		majorPanel.setBounds(88, 96, 194, 34);
		rdbCNTT = new JRadioButton("CNTT");
		rdbKTPM = new JRadioButton("KTPM");
		rdbKHMT = new JRadioButton("KHMT");
		ButtonGroup bgMajor = new ButtonGroup();
		bgMajor.add(rdbCNTT);
		bgMajor.add(rdbKTPM);
		bgMajor.add(rdbKHMT);
		majorPanel.add(rdbCNTT);
		majorPanel.add(rdbKTPM);
		majorPanel.add(rdbKHMT);
		inputPanel.add(majorPanel);

		JLabel label_3 = new JLabel("Ngoai ngu: ", SwingConstants.RIGHT);
		label_3.setBounds(6, 135, 85, 34);
		inputPanel.add(label_3);
		JPanel panelLanguage = new JPanel();
		panelLanguage.setBounds(88, 135, 194, 34);
		chkEnglish = new JCheckBox("Anh");
		chkFrench = new JCheckBox("Phap");
		chkRussian = new JCheckBox("Nga");
		panelLanguage.add(chkEnglish);
		panelLanguage.add(chkFrench);
		panelLanguage.add(chkRussian);
		inputPanel.add(panelLanguage);

		JLabel label_4 = new JLabel("Diem tong ket: ", SwingConstants.RIGHT);
		label_4.setBounds(6, 174, 85, 34);
		inputPanel.add(label_4);
		txtGpa = new JTextField();
		txtGpa.setBounds(101, 174, 62, 34);
		inputPanel.add(txtGpa);

		JPanel resultPanel = new JPanel(new BorderLayout());
		resultPanel.setBorder(BorderFactory.createTitledBorder("KET QUA"));
		panel_1.add(resultPanel);

		txtResult = new JTextArea();
		txtResult.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(txtResult);
		resultPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel actionsPanel = new JPanel(new GridLayout(1, 5, 10, 10));
		actionsPanel.setBorder(BorderFactory.createTitledBorder("THAO TAC"));
		actionsPanel.setBounds(10, 278, 664, 48);
		btnAdd = new JButton("Them");
		btnEdit = new JButton("Sua");
		btnDelete = new JButton("Xoa");
		btnSearch = new JButton("Tim kiem");
		btnExit = new JButton("Thoat");
		actionsPanel.add(btnAdd);
		actionsPanel.add(btnEdit);
		actionsPanel.add(btnDelete);
		actionsPanel.add(btnSearch);
		actionsPanel.add(btnExit);
		panel.add(actionsPanel);
		setLocationRelativeTo(null);

		connectToServer();
		loadData();

		addActionListeners();
	}

	public void addActionListeners() {
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleAdd();
			}
		});

		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleEdit();
			}
		});

		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleDelete();
			}
		});

		btnSearch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleSearch();
			}
		});

		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleExit();
			}
		});
	}

	private void handleAdd() {
		try {
			String name = txtName.getText();
			String id = txtId.getText();
			String major = getSelectedMajor();
			String languages = getSelectedLanguages();
			String gpa = txtGpa.getText();

			if (name.isEmpty() || id.isEmpty() || major.isEmpty() || languages.isEmpty() || gpa.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin.", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			Student student = new Student(id, name, major, languages, Float.parseFloat(gpa));
			oos.writeObject("ADD");
			oos.writeObject(student);
			String response = (String) ois.readObject();
			JOptionPane.showMessageDialog(null, response, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			loadData();
			clearInput();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void handleEdit() {
		if (btnEdit.getText() == "Sua") {
			String id = JOptionPane.showInputDialog("Nhap ma sinh vien: ");
			Student student = getStudent(id);
			if (student != null) {
				txtId.setText(student.getId());
				txtName.setText(student.getName());
				txtGpa.setText(String.valueOf(student.getGpa()));
				String major = student.getMajor();
				if (major == "CNTT") {
					rdbCNTT.setSelected(true);
				} else if (major == "KHMT") {
					rdbKHMT.setSelected(true);
				} else {
					rdbKTPM.setSelected(true);
				}
				String languages = student.getLanguage();
				if (languages.contains(chkEnglish.getText()))
					chkEnglish.setSelected(true);
				if (languages.contains(chkFrench.getText()))
					chkFrench.setSelected(true);
				if (languages.contains(chkRussian.getText()))
					chkRussian.setSelected(true);
			}
			btnEdit.setText("Cap nhat");
			return;
		}

		try {
			String name = txtName.getText();
			String id = txtId.getText();
			String major = getSelectedMajor();
			String languages = getSelectedLanguages();
			String gpa = txtGpa.getText();

			if (name.isEmpty() || id.isEmpty() || major.isEmpty() || languages.isEmpty() || gpa.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin.", "Cảnh báo",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			int cf = JOptionPane.showConfirmDialog(null, "Bạn có muốn cập nhật sửa đổi?");

			if (cf == JOptionPane.YES_OPTION) {
				Student student = new Student(id, name, major, languages, Float.parseFloat(gpa));
				oos.writeObject("UPDATE");
				oos.writeObject(student);
				String response = (String) ois.readObject();
				JOptionPane.showMessageDialog(null, response, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
				loadData();
				clearInput();
				btnEdit.setText("Sua");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void handleDelete() {
		try {
			String id = JOptionPane.showInputDialog("Nhap ma sinh vien: ");
			Student student = getStudent(id);
			txtResult.setText(student + "");
			int cf = JOptionPane.showConfirmDialog(null, "Ban co chac chan muon xoa?");
			if (cf == JOptionPane.OK_OPTION) {
				oos.writeObject("DELETE");
				oos.writeObject(id);
				String response = (String) ois.readObject();
				JOptionPane.showMessageDialog(null, response);
			}
			loadData();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void handleSearch() {
		String id = JOptionPane.showInputDialog("Nhap ma sinh vien: ");
		Student student = getStudent(id);
		if (student != null) {
			txtResult.setText(student + "");
		} else {
			txtResult.setText("Bản ghi " + id + " không tìm thấy");
		}
	}

	private void handleExit() {
		int cf = JOptionPane.showConfirmDialog(null, "Bạn muốn kết thúc chương trình?");
		if (cf == JOptionPane.OK_OPTION) {
			System.exit(0);
		}
	}

	private String getSelectedMajor() {
		if (rdbCNTT.isSelected())
			return "CNTT";
		if (rdbKHMT.isSelected())
			return "KHMT";
		if (rdbKTPM.isSelected())
			return "KTPM";
		return null;
	}

	private String getSelectedLanguages() {
		StringBuilder languages = new StringBuilder();
		if (chkEnglish.isSelected())
			languages.append("Anh ");
		if (chkFrench.isSelected())
			languages.append("Phap ");
		if (chkRussian.isSelected())
			languages.append("Nga ");
		return languages.toString().trim();
	}

	private void clearInput() {
		txtId.setText("");
		txtName.setText("");
		txtGpa.setText("");
		rdbCNTT.setSelected(false);
		rdbKHMT.setSelected(false);
		rdbKTPM.setSelected(false);
		chkEnglish.setSelected(false);
		chkFrench.setSelected(false);
		chkRussian.setSelected(false);
	}

	private void connectToServer() {
		try {
			socket = new Socket(HOST, PORT);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			JOptionPane.showMessageDialog(null, "Kết nối đến máy chủ thành công.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Kết nối đến máy chủ thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		try {
			oos.writeObject("GETALL");
			List<Student> students = (List<Student>) ois.readObject();
			txtResult.setText("");
			txtResult.append(
					String.format("%-10s %-20s %-15s %-10s %-5s%n", "MSV", "Ho ten", "Nganh", "Ngon ngu", "GPA"));
			for (Student s : students) {
				txtResult.append(s + "");
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Student getStudent(String id) {
		try {
			oos.writeObject("FIND");
			oos.writeObject(id);
			return (Student) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
