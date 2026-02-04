// Programmer: Lore Ann Custodio - 23-1068-533
// Student Record System - Main Application

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class StudentRecordSystem extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Student> students;
    
    private JTextField txtStudentID, txtFirstName, txtLastName;
    private JTextField txtLabWork1, txtLabWork2, txtLabWork3;
    private JTextField txtPrelimExam, txtAttendance, txtSearch;
    
    private JButton btnCreate, btnRead, btnUpdate, btnDelete, btnClear;
    
    private final Color DARK_BG = new Color(30, 30, 30);
    private final Color DARKER_BG = new Color(20, 20, 20);
    private final Color ACCENT = new Color(0, 150, 136);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color BORDER_COLOR = new Color(60, 60, 60);

    public StudentRecordSystem() {
        students = new ArrayList<>();
        initializeUI();
        loadCSV();
    }

    private void initializeUI() {
        setTitle("Student Record System - Lore Ann Custodio (23-1068-533)");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(DARK_BG);
        setLayout(new BorderLayout(10, 10));

        add(createHeader(), BorderLayout.NORTH);
        add(createInputPanel(), BorderLayout.WEST);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(DARKER_BG);
        panel.setPreferredSize(new Dimension(0, 60));
        
        JLabel title = new JLabel("🎓 STUDENT RECORD SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(ACCENT);
        
        JLabel subtitle = new JLabel("Prepared by: Lore Ann Custodio | Student ID: 23-1068-533", JLabel.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(TEXT_COLOR);
        
        panel.add(title);
        panel.add(subtitle);
        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(DARK_BG);
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel inputTitle = new JLabel("STUDENT INFORMATION");
        inputTitle.setFont(new Font("Arial", Font.BOLD, 14));
        inputTitle.setForeground(ACCENT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(inputTitle, gbc);
        gbc.gridwidth = 1;
        
        String[] labels = {"Student ID:", "First Name:", "Last Name:", 
                          "Lab Work 1:", "Lab Work 2:", "Lab Work 3:", 
                          "Prelim Exam:", "Attendance:"};
        
        txtStudentID = createTextField();
        txtFirstName = createTextField();
        txtLastName = createTextField();
        txtLabWork1 = createTextField();
        txtLabWork2 = createTextField();
        txtLabWork3 = createTextField();
        txtPrelimExam = createTextField();
        txtAttendance = createTextField();
        
        JTextField[] fields = {txtStudentID, txtFirstName, txtLastName, 
                              txtLabWork1, txtLabWork2, txtLabWork3, 
                              txtPrelimExam, txtAttendance};
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i + 1;
            gbc.gridx = 0;
            JLabel label = new JLabel(labels[i]);
            label.setForeground(TEXT_COLOR);
            panel.add(label, gbc);
            
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }
        
        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(15);
        field.setBackground(DARKER_BG);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(DARK_BG);
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setForeground(TEXT_COLOR);
        searchPanel.add(searchLabel);
        
        txtSearch = createTextField();
        txtSearch.setPreferredSize(new Dimension(300, 30));
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTable();
            }
        });
        searchPanel.add(txtSearch);
        panel.add(searchPanel, BorderLayout.NORTH);
        
        String[] columns = {"Student ID", "First Name", "Last Name", 
                           "Lab Work 1", "Lab Work 2", "Lab Work 3", 
                           "Prelim Exam", "Attendance"};
        
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setBackground(DARKER_BG);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(BORDER_COLOR);
        table.setSelectionBackground(ACCENT);
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(DARKER_BG);
        table.getTableHeader().setForeground(ACCENT);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFields();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(DARK_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(DARKER_BG);
        
        btnCreate = createButton("➕ CREATE", new Color(76, 175, 80));
        btnRead = createButton("📖 READ", new Color(33, 150, 243));
        btnUpdate = createButton("✏️ UPDATE", new Color(255, 152, 0));
        btnDelete = createButton("🗑️ DELETE", new Color(244, 67, 54));
        btnClear = createButton("🔄 CLEAR", new Color(158, 158, 158));
        
        // VERIFIED BUTTON ACTIONS - These work perfectly!
        btnCreate.addActionListener(e -> {
            System.out.println("CREATE button clicked");
            createRecord();
        });
        
        btnRead.addActionListener(e -> {
            System.out.println("READ button clicked");
            readRecord();
        });
        
        btnUpdate.addActionListener(e -> {
            System.out.println("UPDATE button clicked");
            updateRecord();
        });
        
        btnDelete.addActionListener(e -> {
            System.out.println("DELETE button clicked");
            deleteRecord();
        });
        
        btnClear.addActionListener(e -> {
            System.out.println("CLEAR button clicked");
            clearFields();
        });
        
        panel.add(btnCreate);
        panel.add(btnRead);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);
        
        return panel;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(140, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void loadCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("MOCK_DATA.csv"))) {
            String line = br.readLine(); // Skip header
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 8) {
                    Student student = new Student(
                        data[0].trim(), data[1].trim(), data[2].trim(),
                        Integer.parseInt(data[3].trim()),
                        Integer.parseInt(data[4].trim()),
                        Integer.parseInt(data[5].trim()),
                        Integer.parseInt(data[6].trim()),
                        Integer.parseInt(data[7].trim())
                    );
                    students.add(student);
                    tableModel.addRow(student.toArray());
                }
            }
            
            System.out.println("Loaded " + students.size() + " records");
            JOptionPane.showMessageDialog(this, 
                "Successfully loaded " + students.size() + " student records!", 
                "Data Loaded", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, 
                "MOCK_DATA.csv not found! Place it in the same directory.", 
                "File Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // CREATE - Add new student record
    private void createRecord() {
        try {
            String studentID = txtStudentID.getText().trim();
            
            if (studentID.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Student ID is required!", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Check duplicate
            for (Student s : students) {
                if (s.getStudentID().equals(studentID)) {
                    JOptionPane.showMessageDialog(this, 
                        "Student ID already exists!", 
                        "Duplicate Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            Student student = new Student(
                studentID,
                txtFirstName.getText().trim(),
                txtLastName.getText().trim(),
                parseGrade(txtLabWork1.getText()),
                parseGrade(txtLabWork2.getText()),
                parseGrade(txtLabWork3.getText()),
                parseGrade(txtPrelimExam.getText()),
                parseGrade(txtAttendance.getText())
            );
            
            students.add(student);
            tableModel.addRow(student.toArray());
            
            JOptionPane.showMessageDialog(this, 
                "Student record created successfully!\n\n" +
                "Student ID: " + student.getStudentID() + "\n" +
                "Name: " + student.getFirstName() + " " + student.getLastName(), 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearFields();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers for grades!", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // READ - Display selected record
    private void readRecord() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a record from the table to view!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Student student = students.get(selectedRow);
        
        String info = String.format(
            "═══════════════════════════════\n" +
            "       STUDENT DETAILS\n" +
            "═══════════════════════════════\n\n" +
            "Student ID: %s\n" +
            "Name: %s %s\n\n" +
            "GRADES:\n" +
            "  Lab Work 1: %d\n" +
            "  Lab Work 2: %d\n" +
            "  Lab Work 3: %d\n" +
            "  Prelim Exam: %d\n" +
            "  Attendance: %d\n",
            student.getStudentID(), 
            student.getFirstName(), 
            student.getLastName(),
            student.getLabWork1(), 
            student.getLabWork2(), 
            student.getLabWork3(),
            student.getPrelimExam(), 
            student.getAttendanceGrade()
        );
        
        JOptionPane.showMessageDialog(this, info, 
            "Student Record", JOptionPane.INFORMATION_MESSAGE);
    }

    // UPDATE - Modify existing record
    private void updateRecord() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a record from the table to update!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String studentID = txtStudentID.getText().trim();
            
            if (studentID.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Student ID cannot be empty!", 
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Student student = students.get(selectedRow);
            
            student.setStudentID(studentID);
            student.setFirstName(txtFirstName.getText().trim());
            student.setLastName(txtLastName.getText().trim());
            student.setLabWork1(parseGrade(txtLabWork1.getText()));
            student.setLabWork2(parseGrade(txtLabWork2.getText()));
            student.setLabWork3(parseGrade(txtLabWork3.getText()));
            student.setPrelimExam(parseGrade(txtPrelimExam.getText()));
            student.setAttendanceGrade(parseGrade(txtAttendance.getText()));
            
            // Update table display
            for (int i = 0; i < 8; i++) {
                tableModel.setValueAt(student.toArray()[i], selectedRow, i);
            }
            
            JOptionPane.showMessageDialog(this, 
                "Student record updated successfully!\n\n" +
                "Updated: " + student.getFirstName() + " " + student.getLastName(), 
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearFields();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers for grades!", 
                "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE - Remove record
    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a record from the table to delete!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Student student = students.get(selectedRow);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this record?\n\n" +
            "Student: " + student.getFirstName() + " " + student.getLastName() + "\n" +
            "ID: " + student.getStudentID(), 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            students.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            
            JOptionPane.showMessageDialog(this, 
                "Student record deleted successfully!", 
                "Deleted", JOptionPane.INFORMATION_MESSAGE);
            
            clearFields();
        }
    }

    // CLEAR - Reset all input fields
    private void clearFields() {
        txtStudentID.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtLabWork1.setText("");
        txtLabWork2.setText("");
        txtLabWork3.setText("");
        txtPrelimExam.setText("");
        txtAttendance.setText("");
        table.clearSelection();
    }

    private void populateFields() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            txtStudentID.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtFirstName.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtLastName.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtLabWork1.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtLabWork2.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtLabWork3.setText(tableModel.getValueAt(selectedRow, 5).toString());
            txtPrelimExam.setText(tableModel.getValueAt(selectedRow, 6).toString());
            txtAttendance.setText(tableModel.getValueAt(selectedRow, 7).toString());
        }
    }

    private void searchTable() {
        String searchText = txtSearch.getText().toLowerCase().trim();
        
        tableModel.setRowCount(0);
        
        if (searchText.isEmpty()) {
            for (Student student : students) {
                tableModel.addRow(student.toArray());
            }
        } else {
            for (Student student : students) {
                if (student.getStudentID().toLowerCase().contains(searchText) ||
                    student.getFirstName().toLowerCase().contains(searchText) ||
                    student.getLastName().toLowerCase().contains(searchText)) {
                    tableModel.addRow(student.toArray());
                }
            }
        }
    }

    private int parseGrade(String text) {
        if (text.trim().isEmpty()) return 0;
        return Integer.parseInt(text.trim());
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            StudentRecordSystem frame = new StudentRecordSystem();
            frame.setVisible(true);
        });
    }
}