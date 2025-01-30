package scheduler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ClassSchedulingSystem {
    // Declare variables
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JTextField courseField, instructorField, dayField, timeField;
    private Connection conn;

    // Constructor
    public ClassSchedulingSystem() {
        initializeDB();
        initializeUI();
    }

    // Initialize the database
    private void initializeDB() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:database/schedule.db");
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS schedule (id INTEGER PRIMARY KEY AUTOINCREMENT, course TEXT, instructor TEXT, day TEXT, time TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Initialize the user interface
    private void initializeUI() {
        frame = new JFrame("Class Scheduling System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create a panel to hold the input fields and buttons
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Course Name:"));
        courseField = new JTextField();
        panel.add(courseField);

        panel.add(new JLabel("Instructor:"));
        instructorField = new JTextField();
        panel.add(instructorField);

        panel.add(new JLabel("Day:"));
        dayField = new JTextField();
        panel.add(dayField);

        panel.add(new JLabel("Time:"));
        timeField = new JTextField();
        panel.add(timeField);

        // Create a button to add a schedule
        JButton addButton = new JButton("Add Schedule");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSchedule();
            }
        });
        panel.add(addButton);

        // Create a button to delete a selected schedule
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });
        panel.add(deleteButton);

        // Add the panel to the frame
        frame.add(panel, BorderLayout.NORTH);

        // Create a table to display the schedule
        model = new DefaultTableModel(new String[]{"ID", "Course", "Instructor", "Day", "Time"}, 0);
        table = new JTable(model);
        loadSchedule();
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }

    // Add a schedule to the database
    private void addSchedule() {
        String course = courseField.getText();
        String instructor = instructorField.getText();
        String day = dayField.getText();
        String time = timeField.getText();
        
        // Check if all fields are filled
        if (course.isEmpty() || instructor.isEmpty() || day.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled!");
            return;
        }
        
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO schedule (course, instructor, day, time) VALUES (?, ?, ?, ?)");
            stmt.setString(1, course);
            stmt.setString(2, instructor);
            stmt.setString(3, day);
            stmt.setString(4, time);
            stmt.executeUpdate();
            loadSchedule();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSchedule() {
        model.setRowCount(0);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM schedule");
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("course"), rs.getString("instructor"), rs.getString("day"), rs.getString("time")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row to delete!");
            return;
        }
        int id = (int) model.getValueAt(selectedRow, 0);
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM schedule WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            loadSchedule();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClassSchedulingSystem();
    }
}
