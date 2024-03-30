import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class LoginPanel extends JFrame {
    private JTextField emailField;
    private JTextField usernameField;
    private JButton loginButton;

    public LoginPanel() {
        setTitle("Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the login panel on the screen
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.WHITE); // Set background color

        // Create constraints for left section (image)
        GridBagConstraints leftConstraints = new GridBagConstraints();
        leftConstraints.gridx = 0;
        leftConstraints.gridy = 0;
        leftConstraints.weightx = 0.3; // Adjusted weight for the left section
        leftConstraints.fill = GridBagConstraints.BOTH;

        // Create constraints for right section (login fields)
        GridBagConstraints rightConstraints = new GridBagConstraints();
        rightConstraints.gridx = 1;
        rightConstraints.gridy = 0;
        rightConstraints.weightx = 0.7; // Adjusted weight for the right section
        rightConstraints.fill = GridBagConstraints.BOTH;

        // Create a panel for the image (left section)
        ImageIcon originalIcon = new ImageIcon("src/logo.jpg"); // Adjust path to the image
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Resize the image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel logoLabel = new JLabel(scaledIcon);
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE); // Set background color
        imagePanel.add(logoLabel, BorderLayout.CENTER);
        add(imagePanel, leftConstraints);


        // Create a panel for inputs (right section)
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 10)); // Increased vertical gap for better spacing
        inputPanel.setBackground(Color.WHITE); // Set background color
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding and create an empty border
        Font labelFont = new Font("Arial", Font.BOLD, 14); // Define a font for labels
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14); // Define a font for text fields

// Create labels and text fields
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont); // Set font for label
        emailLabel.setForeground(new Color(66, 139, 202)); // Set text color for label
        emailField = new JTextField();
        emailField.setFont(textFieldFont); // Set font for text field
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(labelFont); // Set font for label
        usernameLabel.setForeground(new Color(66, 139, 202)); // Set text color for label
        usernameField = new JTextField();
        usernameField.setFont(textFieldFont); // Set font for text field
        inputPanel.add(usernameLabel);
        inputPanel.add(usernameField);

        add(inputPanel, rightConstraints);


        // Create a panel for the login button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE); // Set background color
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(66, 139, 202)); // Set button background color
        loginButton.setForeground(Color.WHITE); // Set button text color
        loginButton.addActionListener(e -> login());
        buttonPanel.add(loginButton);
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.gridx = 1;
        buttonConstraints.gridy = 1;
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraints.anchor = GridBagConstraints.PAGE_END;
        buttonConstraints.insets = new Insets(10, 0, 0, 0); // Add some space between fields and button
        add(buttonPanel, buttonConstraints);


        setVisible(true);
    }
    private void login() {
        String email = emailField.getText();
        String username = usernameField.getText();

        if (!email.isEmpty() && !username.isEmpty()) {
            try {
                Socket socket = new Socket("localhost", 12345);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

                outputStream.writeObject("JOIN " + username + " " + email);
                outputStream.flush();

                String response = (String) inputStream.readObject();

                if (response.equals("LOGIN_SUCCESSFUL")) {
                    JOptionPane.showMessageDialog(this, "Login successful");
                    dispose();
                    String serverAddress = "localhost";
                    int port = 12345;
                    new ChatClient(serverAddress, port, username, socket, outputStream, inputStream);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or email", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please enter both email and username.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginPanel::new);
    }
}
