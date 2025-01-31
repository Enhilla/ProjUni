package scheduler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ClassSchedulingSystem {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JTextField courseField, instructorField, dayField, timeField;
    private List<String[]> scheduleList;

    public ClassSchedulingSystem() {
        scheduleList = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Class Scheduling System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

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

        JButton addButton = new JButton("Add Schedule");
        addButton.addActionListener(e -> addSchedule());
        panel.add(addButton);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.addActionListener(e -> deleteSelectedRow());
        panel.add(deleteButton);

        frame.add(panel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Course", "Instructor", "Day", "Time"}, 0);
        table = new JTable(model);
        loadSchedule();
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void addSchedule() {
        String course = courseField.getText();
        String instructor = instructorField.getText();
        String day = dayField.getText();
        String time = timeField.getText();

        if (course.isEmpty() || instructor.isEmpty() || day.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled!");
            return;
        }

        scheduleList.add(new String[]{String.valueOf(scheduleList.size() + 1), course, instructor, day, time});
        loadSchedule();
    }

    private void loadSchedule() {
        model.setRowCount(0);
        for (String[] entry : scheduleList) {
            model.addRow(entry);
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a row to delete!");
            return;
        }

        scheduleList.remove(selectedRow);
        loadSchedule();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClassSchedulingSystem::new);
    }
}
