import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentResultSystemUI {
    private StudentManager sm;
    private final Color PRIMARY_COLOR = new Color(59, 89, 182); // Blue color
    private final Color SECONDARY_COLOR = new Color(236, 240, 241);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // 🎨 Modern styled button
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setPreferredSize(new Dimension(90, 28));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ✅ Force background to render
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    // 🖌️ Style text field
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(TEXT_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    public StudentResultSystemUI() {
        sm = new StudentManager();
        JFrame frame = new JFrame("Student Result Management System");
        frame.setSize(650, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Add Student Tab
        JPanel addStudentPanel = new JPanel(new GridBagLayout());
        addStudentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        addStudentPanel.setBackground(SECONDARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel addStudentTitle = new JLabel("Add New Student");
        addStudentTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        addStudentPanel.add(addStudentTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(LABEL_FONT);
        addStudentPanel.add(idLabel, gbc);

        gbc.gridx++;
        JTextField idField = createStyledTextField();
        addStudentPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(LABEL_FONT);
        addStudentPanel.add(nameLabel, gbc);

        gbc.gridx++;
        JTextField nameField = createStyledTextField();
        addStudentPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel classLabel = new JLabel("Class:");
        classLabel.setFont(LABEL_FONT);
        addStudentPanel.add(classLabel, gbc);

        gbc.gridx++;
        JTextField classField = createStyledTextField();
        addStudentPanel.add(classField, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        JButton addBtn = createStyledButton("Add Student", PRIMARY_COLOR);
        addStudentPanel.add(addBtn, gbc);

        addBtn.addActionListener(e -> {
            sm.addStudent(idField.getText().trim(), nameField.getText().trim(), classField.getText().trim());
            JOptionPane.showMessageDialog(frame, "Student Added Successfully!");
            idField.setText(""); nameField.setText(""); classField.setText("");
        });

        // View Results Tab
        JPanel viewResultPanel = new JPanel(new BorderLayout(10, 10));
        viewResultPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        viewResultPanel.setBackground(SECONDARY_COLOR);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(SECONDARY_COLOR);

        JLabel searchLabel = new JLabel("Search Student Results:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        topPanel.add(searchLabel);

        JTextField resultIdField = createStyledTextField();
        resultIdField.setPreferredSize(new Dimension(120, 30));
        topPanel.add(resultIdField);

        JButton viewBtn = createStyledButton("View Results", PRIMARY_COLOR);
        topPanel.add(viewBtn);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resultArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        viewResultPanel.add(topPanel, BorderLayout.NORTH);
        viewResultPanel.add(scrollPane, BorderLayout.CENTER);

        viewBtn.addActionListener(e -> {
            String sid = resultIdField.getText().trim();
            resultArea.setText("");

            if (sid.isEmpty()) {
                resultArea.setText("Please enter a student ID.");
                return;
            }

            try (Connection con = DBConnect.connect()) {
                if (con == null) {
                    resultArea.setText("Error: Could not connect to the database.");
                    return;
                }

                String query = """
                        SELECT s.name AS subject, m.marks
                        FROM marks m
                        JOIN subject s ON m.subject_id = s.subject_id
                        WHERE m.student_id = ?
                    """;

                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, sid);
                ResultSet rs = ps.executeQuery();

                boolean found = false;
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("%-20s %s\n", "Subject", "Marks"));
                sb.append("-------------------- -----\n");

                while (rs.next()) {
                    found = true;
                    sb.append(String.format("%-20s %5d\n",
                            rs.getString("subject"),
                            rs.getInt("marks")));
                }

                if (found) {
                    resultArea.setText(sb.toString());
                } else {
                    resultArea.setText("No results found for Student ID: " + sid);
                }

            } catch (Exception ex) {
                resultArea.setText("Error fetching results: " + ex.getMessage());
            }
        });

        // Add Marks Tab
        JPanel addMarksPanel = new JPanel(new GridBagLayout());
        addMarksPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        addMarksPanel.setBackground(SECONDARY_COLOR);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel addMarksTitle = new JLabel("Add Student Marks");
        addMarksTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        addMarksPanel.add(addMarksTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setFont(LABEL_FONT);
        addMarksPanel.add(studentIdLabel, gbc);

        gbc.gridx++;
        JTextField studentIdField = createStyledTextField();
        addMarksPanel.add(studentIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel subjectIdLabel = new JLabel("Subject ID:");
        subjectIdLabel.setFont(LABEL_FONT);
        addMarksPanel.add(subjectIdLabel, gbc);

        gbc.gridx++;
        JTextField subjectIdField = createStyledTextField();
        addMarksPanel.add(subjectIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel marksLabel = new JLabel("Marks:");
        marksLabel.setFont(LABEL_FONT);
        addMarksPanel.add(marksLabel, gbc);

        gbc.gridx++;
        JTextField marksField = createStyledTextField();
        addMarksPanel.add(marksField, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        JButton addMarksBtn = createStyledButton("Add Marks", PRIMARY_COLOR);
        addMarksPanel.add(addMarksBtn, gbc);

        addMarksBtn.addActionListener(e -> {
            try {
                int marks = Integer.parseInt(marksField.getText().trim());
                sm.addMarks(studentIdField.getText().trim(), subjectIdField.getText().trim(), marks);
                JOptionPane.showMessageDialog(frame, "Marks Added Successfully!");
                studentIdField.setText(""); subjectIdField.setText(""); marksField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid integer marks.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add Subject Tab
        JPanel addSubjectPanel = new JPanel(new GridBagLayout());
        addSubjectPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        addSubjectPanel.setBackground(SECONDARY_COLOR);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel addSubjectTitle = new JLabel("Add New Subject");
        addSubjectTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        addSubjectPanel.add(addSubjectTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel subIdLabel = new JLabel("Subject ID:");
        subIdLabel.setFont(LABEL_FONT);
        addSubjectPanel.add(subIdLabel, gbc);

        gbc.gridx++;
        JTextField subIdField = createStyledTextField();
        addSubjectPanel.add(subIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel subNameLabel = new JLabel("Subject Name:");
        subNameLabel.setFont(LABEL_FONT);
        addSubjectPanel.add(subNameLabel, gbc);

        gbc.gridx++;
        JTextField subNameField = createStyledTextField();
        addSubjectPanel.add(subNameField, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        JButton addSubBtn = createStyledButton("Add Subject", PRIMARY_COLOR);
        addSubjectPanel.add(addSubBtn, gbc);

        addSubBtn.addActionListener(e -> {
            sm.addSubject(subIdField.getText().trim(), subNameField.getText().trim());
            JOptionPane.showMessageDialog(frame, "Subject Added Successfully!");
            subIdField.setText(""); subNameField.setText("");
        });

        // Final Setup
        tabbedPane.addTab("Add Student", addStudentPanel);
        tabbedPane.addTab("View Results", viewResultPanel);
        tabbedPane.addTab("Add Marks", addMarksPanel);
        tabbedPane.addTab("Add Subject", addSubjectPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentResultSystemUI());
    }
}
