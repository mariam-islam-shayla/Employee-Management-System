import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EmployeeManagementSystem {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField regUsernameField, loginUsernameField;
    private JPasswordField regPasswordField, regConfirmPasswordField, loginPasswordField;

    private JTextField nameField, idField, ageField, emailField, phoneField, designationField, salaryField;
    private JTextField searchField;
    private JComboBox<String> removeIdBox;
    private DefaultTableModel tableModel;

    private java.util.List<Employee> employees = new ArrayList<>();
    private String savedUsername = "";
    private String savedPassword = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeManagementSystem::new);
    }

    public EmployeeManagementSystem() {
        frame = new JFrame("Employee Management System");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createRegistrationPanel(), "register");
        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createMainMenuPanel(), "menu");
        mainPanel.add(createAddEmployeePanel(), "add");
        mainPanel.add(createViewEmployeePanel(), "view");
        mainPanel.add(createRemoveEmployeePanel(), "remove");

        frame.add(mainPanel);
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        cardLayout.show(mainPanel, "register");
    }

    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        regUsernameField = new JTextField();
        regPasswordField = new JPasswordField();
        regConfirmPasswordField = new JPasswordField();

        JButton registerBtn = new JButton("Register");
        registerBtn.addActionListener(e -> {
            String username = regUsernameField.getText();
            String password = new String(regPasswordField.getPassword());
            String confirmPassword = new String(regConfirmPasswordField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match.");
                return;
            }

            savedUsername = username;
            savedPassword = password;
            JOptionPane.showMessageDialog(frame, "Registration Successful!");
            cardLayout.show(mainPanel, "login");
        });

        panel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        panel.add(new JLabel("Username:"));
        panel.add(regUsernameField);
        panel.add(new JLabel("Password:"));
        panel.add(regPasswordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(regConfirmPasswordField);
        panel.add(new JLabel());
        panel.add(registerBtn);

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        loginUsernameField = new JTextField();
        loginPasswordField = new JPasswordField();

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> {
            String user = loginUsernameField.getText();
            String pass = new String(loginPasswordField.getPassword());
            if (user.equals(savedUsername) && pass.equals(savedPassword)) {
                cardLayout.show(mainPanel, "menu");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.");
            }
        });

        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));
        panel.add(new JLabel("Username:"));
        panel.add(loginUsernameField);
        panel.add(new JLabel("Password:"));
        panel.add(loginPasswordField);
        panel.add(new JLabel());
        panel.add(loginBtn);

        return panel;
    }

    private JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JButton addBtn = new JButton("Add Employee");
        JButton viewBtn = new JButton("View Employee");
        JButton removeBtn = new JButton("Remove Employee");

        addBtn.addActionListener(e -> cardLayout.show(mainPanel, "add"));
        viewBtn.addActionListener(e -> {
            updateTable();
            cardLayout.show(mainPanel, "view");
        });
        removeBtn.addActionListener(e -> {
            updateRemoveBox();
            cardLayout.show(mainPanel, "remove");
        });

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(removeBtn);

        panel.add(buttonPanel);
        return panel;
    }

    private JPanel createAddEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Add Employee Details", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        nameField = new JTextField(); 
        idField = new JTextField(); 
        ageField = new JTextField();
        emailField = new JTextField(); 
        phoneField = new JTextField(); 
        designationField = new JTextField();
        salaryField = new JTextField();

        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        formPanel.add(new JLabel("Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("ID:")); formPanel.add(idField);
        formPanel.add(new JLabel("Age:")); formPanel.add(ageField);
        formPanel.add(new JLabel("Email:")); formPanel.add(emailField);
        formPanel.add(new JLabel("Phone Number:")); formPanel.add(phoneField);
        formPanel.add(new JLabel("Designation:")); formPanel.add(designationField);
        formPanel.add(new JLabel("Salary:")); formPanel.add(salaryField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addEmpBtn = new JButton("Add");
        JButton backBtn = new JButton("Back");

        addEmpBtn.addActionListener(e -> {
            Employee emp = new Employee(
                idField.getText(), nameField.getText(), ageField.getText(),
                emailField.getText(), phoneField.getText(),
                designationField.getText(), salaryField.getText()
            );
            employees.add(emp);
            JOptionPane.showMessageDialog(frame, "Employee Added!");
            clearFields();
            cardLayout.show(mainPanel, "menu");
        });

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        buttonPanel.add(addEmpBtn);
        buttonPanel.add(backBtn);

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createViewEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Email", "Phone", "Designation", "Salary"}, 0);
        JTable table = new JTable(tableModel);

        JPanel topPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        JButton backBtn = new JButton("Back");

        searchBtn.addActionListener(e -> searchEmployee());
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        topPanel.add(new JLabel("Search by ID: "));
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(backBtn);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createRemoveEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Remove Employee", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        removeIdBox = new JComboBox<>();
        JLabel idLabel = new JLabel(); 
        JLabel nameLabel = new JLabel();
        JLabel emailLabel = new JLabel(); 
        JLabel phoneLabel = new JLabel(); 
        JLabel salaryLabel = new JLabel();

        removeIdBox.addActionListener(e -> {
            String selectedId = (String) removeIdBox.getSelectedItem();
            Employee emp = findEmployeeById(selectedId);
            if (emp != null) {
                idLabel.setText(emp.id); 
                nameLabel.setText(emp.name);
                emailLabel.setText(emp.email); 
                phoneLabel.setText(emp.phone); 
                salaryLabel.setText(emp.salary);
            }
        });

        centerPanel.add(new JLabel("Select Employee ID:")); 
        centerPanel.add(removeIdBox);
        centerPanel.add(new JLabel("ID:")); 
        centerPanel.add(idLabel);
        centerPanel.add(new JLabel("Name:")); 
        centerPanel.add(nameLabel);
        centerPanel.add(new JLabel("Email:")); 
        centerPanel.add(emailLabel);
        centerPanel.add(new JLabel("Phone:")); 
        centerPanel.add(phoneLabel);
        centerPanel.add(new JLabel("Salary:")); 
        centerPanel.add(salaryLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton removeBtn = new JButton("Remove Employee");
        JButton backBtn = new JButton("Back");

        removeBtn.addActionListener(e -> {
            String selectedId = (String) removeIdBox.getSelectedItem();
            employees.removeIf(emp -> emp.id.equals(selectedId));
            JOptionPane.showMessageDialog(frame, "Employee Removed.");
            updateRemoveBox();
            cardLayout.show(mainPanel, "menu");
        });

        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        buttonPanel.add(removeBtn);
        buttonPanel.add(backBtn);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateRemoveBox() {
        removeIdBox.removeAllItems();
        for (Employee emp : employees) {
            removeIdBox.addItem(emp.id);
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{emp.id, emp.name, emp.age, emp.email, emp.phone, emp.designation, emp.salary});
        }
    }

    private void searchEmployee() {
        String id = searchField.getText();
        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            if (emp.id.equals(id)) {
                tableModel.addRow(new Object[]{emp.id, emp.name, emp.age, emp.email, emp.phone, emp.designation, emp.salary});
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Employee not found.");
    }

    private void clearFields() {
        nameField.setText(""); 
        idField.setText(""); 
        ageField.setText("");
        emailField.setText(""); 
        phoneField.setText(""); 
        designationField.setText(""); 
        salaryField.setText("");
    }

    private Employee findEmployeeById(String id) {
        for (Employee emp : employees) {
            if (emp.id.equals(id)) return emp;
        }
        return null;
    }

    static class Employee {
        String id, name, age, email, phone, designation, salary;
        Employee(String id, String name, String age, String email, String phone, String designation, String salary) {
            this.id = id; 
            this.name = name; 
            this.age = age;
            this.email = email; 
            this.phone = phone;
            this.designation = designation; 
            this.salary = salary;
        }
    }
}

