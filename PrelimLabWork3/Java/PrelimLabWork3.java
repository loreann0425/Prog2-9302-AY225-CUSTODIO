package Java;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class PrelimLabWork3 extends JFrame {
    // Input fields
    private JTextField attendanceField;
    private JTextField lab1Field;
    private JTextField lab2Field;
    private JTextField lab3Field;
    
    // Result labels
    private JLabel attendanceScoreLabel;
    private JLabel lab1DisplayLabel;
    private JLabel lab2DisplayLabel;
    private JLabel lab3DisplayLabel;
    private JLabel labAvgLabel;
    private JLabel classStandingLabel;
    private JLabel passScoreLabel;
    private JLabel excellentScoreLabel;
    private JTextArea remarksArea;
    
    public PrelimLabWork3() {
        setTitle("Prelim Grade Calculator");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(106, 90, 205);
                Color color2 = new Color(72, 61, 139);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("📊 PRELIM GRADE CALCULATOR");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Input Panel
        JPanel inputPanel = createStyledPanel("INPUT SECTION");
        inputPanel.setLayout(new GridLayout(4, 2, 15, 15));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Add input fields
        attendanceField = createStyledTextField();
        lab1Field = createStyledTextField();
        lab2Field = createStyledTextField();
        lab3Field = createStyledTextField();
        
        inputPanel.add(createStyledLabel("Number of Attendances (max 5):"));
        inputPanel.add(attendanceField);
        inputPanel.add(createStyledLabel("Lab Work 1 Grade (0-100):"));
        inputPanel.add(lab1Field);
        inputPanel.add(createStyledLabel("Lab Work 2 Grade (0-100):"));
        inputPanel.add(lab2Field);
        inputPanel.add(createStyledLabel("Lab Work 3 Grade (0-100):"));
        inputPanel.add(lab3Field);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton calculateBtn = createStyledButton("CALCULATE", new Color(34, 139, 34));
        JButton clearBtn = createStyledButton("CLEAR", new Color(220, 20, 60));
        
        buttonPanel.add(calculateBtn);
        buttonPanel.add(clearBtn);
        
        // Results Panel
        JPanel resultsPanel = createStyledPanel("COMPUTED VALUES");
        resultsPanel.setLayout(new GridLayout(5, 2, 10, 10));
        resultsPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 2, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        attendanceScoreLabel = createResultLabel();
        lab1DisplayLabel = createResultLabel();
        lab2DisplayLabel = createResultLabel();
        lab3DisplayLabel = createResultLabel();
        labAvgLabel = createResultLabel();
        classStandingLabel = createResultLabel();
        passScoreLabel = createResultLabel();
        excellentScoreLabel = createResultLabel();
        
        resultsPanel.add(createStyledLabel("Attendance Score:"));
        resultsPanel.add(attendanceScoreLabel);
        resultsPanel.add(createStyledLabel("Lab Work Average:"));
        resultsPanel.add(labAvgLabel);
        resultsPanel.add(createStyledLabel("Class Standing (70%):"));
        resultsPanel.add(classStandingLabel);
        resultsPanel.add(createStyledLabel("To Pass (75):"));
        resultsPanel.add(passScoreLabel);
        resultsPanel.add(createStyledLabel("For Excellent (100):"));
        resultsPanel.add(excellentScoreLabel);
        
        // Remarks Panel
        JPanel remarksPanel = createStyledPanel("REMARKS");
        remarksPanel.setLayout(new BorderLayout());
        remarksPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 2, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        remarksArea = new JTextArea(4, 30);
        remarksArea.setEditable(false);
        remarksArea.setFont(new Font("Arial", Font.BOLD, 13));
        remarksArea.setBackground(new Color(255, 240, 245));
        remarksArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        remarksArea.setLineWrap(true);
        remarksArea.setWrapStyleWord(true);
        remarksPanel.add(new JScrollPane(remarksArea), BorderLayout.CENTER);
        
        // Add all panels to content
        contentPanel.add(inputPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(resultsPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(remarksPanel);
        
        // Add to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Button Actions
        calculateBtn.addActionListener(e -> calculateGrades());
        clearBtn.addActionListener(e -> clearFields());
    }
    
    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255, 230));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(106, 90, 205), 2),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(106, 90, 205)
        ));
        return panel;
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(47, 79, 79));
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(106, 90, 205), 2, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private JLabel createResultLabel() {
        JLabel label = new JLabel("0.00");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(0, 100, 0));
        return label;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 30, 10, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void calculateGrades() {
        try {
            // Get inputs
            int attendance = Integer.parseInt(attendanceField.getText().trim());
            int lab1 = Integer.parseInt(lab1Field.getText().trim());
            int lab2 = Integer.parseInt(lab2Field.getText().trim());
            int lab3 = Integer.parseInt(lab3Field.getText().trim());
            
            // Validate inputs
            if (attendance < 0 || attendance > 5) {
                JOptionPane.showMessageDialog(this, 
                    "Attendance must be between 0 and 5!", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (lab1 < 0 || lab1 > 100 || lab2 < 0 || lab2 > 100 || lab3 < 0 || lab3 > 100) {
                JOptionPane.showMessageDialog(this, 
                    "Lab grades must be between 0 and 100!", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Calculate attendance score (each attendance = 20%)
            double attendanceScore = attendance * 20.0;
            
            // Calculate absences
            int absences = 5 - attendance;
            
            // Check if failed due to absences (4 or more absences)
            if (absences >= 4) {
                attendanceScoreLabel.setText(String.format("%.2f", attendanceScore));
                labAvgLabel.setText("0.00");
                classStandingLabel.setText("0.00");
                passScoreLabel.setText("N/A");
                excellentScoreLabel.setText("N/A");
                
                remarksArea.setText("❌ FAILED.\n\n" +
                    "You have " + absences + " absence(s). Having 4 or more absences results in automatic failure.\n\n" +
                    "⚠️ Passing the Prelim period is IMPOSSIBLE due to excessive absences.");
                remarksArea.setForeground(new Color(139, 0, 0));
                return;
            }
            
            // Calculate Lab Work Average
            double labAvg = (lab1 + lab2 + lab3) / 3.0;
            
            // Calculate Class Standing (40% Attendance + 60% Lab Work Average)
            double classStanding = (attendanceScore * 0.40) + (labAvg * 0.60);
            
            // Calculate Required Prelim Exam Scores
            // Formula: Prelim Grade = (Prelim Exam × 0.30) + (Class Standing × 0.70)
            // To Pass (75): 75 = (PE × 0.30) + (CS × 0.70)
            // PE = (75 - CS × 0.70) / 0.30
            
            double requiredToPass = (75.0 - (classStanding * 0.70)) / 0.30;
            double requiredForExcellent = (100.0 - (classStanding * 0.70)) / 0.30;
            
            // Display results
            attendanceScoreLabel.setText(String.format("%.2f", attendanceScore));
            labAvgLabel.setText(String.format("%.2f", labAvg));
            classStandingLabel.setText(String.format("%.2f", classStanding));
            passScoreLabel.setText(String.format("%.2f", requiredToPass));
            excellentScoreLabel.setText(String.format("%.2f", requiredForExcellent));
            
            // Generate remarks
            StringBuilder remarks = new StringBuilder();
            
            if (requiredToPass > 100) {
                remarks.append("❌ FAILED.\n\n");
                remarks.append(String.format("Passing the Prelim period is IMPOSSIBLE because the required score (%.2f) exceeds 100.\n\n", requiredToPass));
                remarks.append("⚠️ Achieving an Excellent grade (100) is NOT possible.\n");
                remarks.append(String.format("The required score (%.2f) exceeds 100.", requiredForExcellent));
                remarksArea.setForeground(new Color(139, 0, 0));
            } else if (requiredForExcellent > 100) {
                remarks.append("✓ Passing is POSSIBLE!\n\n");
                remarks.append(String.format("You need to score %.2f on the Prelim Exam to pass (75).\n\n", requiredToPass));
                remarks.append("⚠️ Achieving an Excellent grade (100) is NOT possible.\n");
                remarks.append(String.format("The required score (%.2f) exceeds 100.", requiredForExcellent));
                remarksArea.setForeground(new Color(184, 134, 11));
            } else {
                remarks.append("✓ Passing is POSSIBLE!\n\n");
                remarks.append(String.format("You need to score %.2f on the Prelim Exam to pass (75).\n\n", requiredToPass));
                remarks.append("🌟 Achieving an Excellent grade (100) is POSSIBLE!\n");
                remarks.append(String.format("You need to score %.2f on the Prelim Exam.", requiredForExcellent));
                remarksArea.setForeground(new Color(0, 100, 0));
            }
            
            remarksArea.setText(remarks.toString());
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers in all fields!", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        attendanceField.setText("");
        lab1Field.setText("");
        lab2Field.setText("");
        lab3Field.setText("");
        attendanceScoreLabel.setText("0.00");
        labAvgLabel.setText("0.00");
        classStandingLabel.setText("0.00");
        passScoreLabel.setText("0.00");
        excellentScoreLabel.setText("0.00");
        remarksArea.setText("");
        attendanceField.requestFocus();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new PrelimLabWork3().setVisible(true);
        });
    }
}