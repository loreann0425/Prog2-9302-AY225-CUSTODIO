import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * AttendanceTracker - A Java Swing application for tracking attendance
 * This application displays a form to record attendance information
 * including name, course/year, time in, and generates an e-signature
 * 
 * @author Your Name
 * @version 1.0
 */
public class AttendanceTracker {
    
    // Declare GUI components
    private JFrame frame;
    private JTextField nameField;
    private JTextField courseYearField;
    private JTextField timeInField;
    private JTextField eSignatureField;
    private JButton submitButton;
    private JButton clearButton;
    
    /**
     * Constructor - initializes the GUI components
     */
    public AttendanceTracker() {
        initializeGUI();
    }
    
    /**
     * Initialize and setup all GUI components
     */
    private void initializeGUI() {
        // Create the main frame
        frame = new JFrame("Attendance Tracker");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add title label
        JLabel titleLabel = new JLabel("Attendance Tracking System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Create form panel for input fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Attendance Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel nameLabel = new JLabel("Attendance Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        // Course/Year field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel courseYearLabel = new JLabel("Course/Year:");
        courseYearLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(courseYearLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        courseYearField = new JTextField(20);
        formPanel.add(courseYearField, gbc);
        
        // Time In field (auto-populated)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel timeInLabel = new JLabel("Time In:");
        timeInLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(timeInLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        timeInField = new JTextField(20);
        timeInField.setEditable(false); // Make it read-only
        timeInField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(timeInField, gbc);
        
        // E-Signature field (auto-generated)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel eSignatureLabel = new JLabel("E-Signature:");
        eSignatureLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(eSignatureLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        eSignatureField = new JTextField(20);
        eSignatureField.setEditable(false); // Make it read-only
        eSignatureField.setBackground(Color.LIGHT_GRAY);
        formPanel.add(eSignatureField, gbc);
        
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        // Submit button
        submitButton = new JButton("Submit Attendance");
        submitButton.setFont(new Font("Arial", Font.BOLD, 12));
        submitButton.addActionListener(e -> submitAttendance());
        buttonPanel.add(submitButton);
        
        // Clear button
        clearButton = new JButton("Clear Fields");
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.addActionListener(e -> clearFields());
        buttonPanel.add(clearButton);
        
        mainPanel.add(buttonPanel);
        
        // Add main panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);
        
        // Auto-populate Time In and E-Signature on startup
        generateTimeAndSignature();
        
        // Center the frame on screen
        frame.setLocationRelativeTo(null);
        
        // Make the frame visible
        frame.setVisible(true);
    }
    
    /**
     * Generate current date/time and e-signature
     */
    private void generateTimeAndSignature() {
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        
        // Format the date and time for better readability
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        
        // Set the time in field
        timeInField.setText(formattedDateTime);
        
        // Generate unique e-signature using UUID
        String eSignature = UUID.randomUUID().toString();
        
        // Set the e-signature field
        eSignatureField.setText(eSignature);
    }
    
    /**
     * Submit attendance - validate and display confirmation
     */
    private void submitAttendance() {
        // Get values from fields
        String name = nameField.getText().trim();
        String courseYear = courseYearField.getText().trim();
        String timeIn = timeInField.getText();
        String eSignature = eSignatureField.getText();
        
        // Validate input - check if fields are empty
        if (name.isEmpty() || courseYear.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                "Please fill in all required fields (Name and Course/Year)",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate Course/Year - only accept valid courses
        if (!isValidCourse(courseYear)) {
            JOptionPane.showMessageDialog(frame,
                "Invalid Course/Year!\n\n" +
                "Valid courses include:\n" +
                "• BSIT (Bachelor of Science in Information Technology)\n" +
                "• BSCS (Bachelor of Science in Computer Science)\n" +
                "• BSCpE (Bachelor of Science in Computer Engineering)\n" +
                "• BSECE (Bachelor of Science in Electronics and Communications Engineering)\n" +
                "• ACT (Associate in Computer Technology)\n\n" +
                "Format: COURSE-YEAR (e.g., BSIT-3, BSCS-1)",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Display confirmation message
        String message = "Attendance Submitted Successfully!\n\n" +
                        "Name: " + name + "\n" +
                        "Course/Year: " + courseYear + "\n" +
                        "Time In: " + timeIn + "\n" +
                        "E-Signature: " + eSignature;
        
        JOptionPane.showMessageDialog(frame,
            message,
            "Attendance Confirmation",
            JOptionPane.INFORMATION_MESSAGE);
        
        // Clear fields after successful submission
        clearFields();
    }
    
    /**
     * Validate if the entered course/year is valid
     * @param courseYear the course/year string to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidCourse(String courseYear) {
        // Convert to uppercase for case-insensitive comparison
        String input = courseYear.toUpperCase().trim();
        
        // List of valid course codes
        String[] validCourses = {
            "BSIT",   // Bachelor of Science in Information Technology
            "BSCS",   // Bachelor of Science in Computer Science
            "BSCPE",  // Bachelor of Science in Computer Engineering
            "BSECE",  // Bachelor of Science in Electronics and Communications Engineering
            "ACT",    // Associate in Computer Technology
            "BSCE",   // Bachelor of Science in Civil Engineering
            "BSEE",   // Bachelor of Science in Electrical Engineering
            "BSME",   // Bachelor of Science in Mechanical Engineering
            "BSBA",   // Bachelor of Science in Business Administration
            "BSED",   // Bachelor of Secondary Education
            "BEED",   // Bachelor of Elementary Education
            "BSN",    // Bachelor of Science in Nursing
            "BSHRM"   // Bachelor of Science in Hotel and Restaurant Management
        };
        
        // Check if input matches pattern: COURSE-YEAR (e.g., BSIT-3, BSCS-1)
        // Also accept just the course code without year (e.g., BSIT, BSCS)
        for (String course : validCourses) {
            // Check if it matches exactly (just course code)
            if (input.equals(course)) {
                return true;
            }
            
            // Check if it matches with year format (COURSE-YEAR)
            if (input.startsWith(course + "-")) {
                // Extract the year part
                String yearPart = input.substring(course.length() + 1);
                
                // Check if year is a valid number (1-5)
                try {
                    int year = Integer.parseInt(yearPart);
                    if (year >= 1 && year <= 5) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    // Invalid year format
                    continue;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Clear all input fields and regenerate time and signature
     */
    private void clearFields() {
        // Clear user input fields
        nameField.setText("");
        courseYearField.setText("");
        
        // Regenerate time and e-signature
        generateTimeAndSignature();
        
        // Set focus back to name field
        nameField.requestFocus();
    }
    
    /**
     * Main method - entry point of the application
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure GUI is created on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new AttendanceTracker();
        });
    }
}