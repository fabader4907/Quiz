import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ModernQuizLogin extends JFrame {

    private Map<String, String> userMap = new HashMap<>(); // Benutzerliste

    public ModernQuizLogin() {
        setTitle("🎓 Quiz Anmeldung");
        setSize(420, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // zentriert

        // Farben
        Color bgColor = new Color(240, 250, 255);
        Color buttonColor = new Color(0, 123, 255);
        Color fieldColor = new Color(230, 240, 250);

        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Willkommen beim Quiz", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(33, 37, 41));

        JTextField usernameField = new JTextField();
        usernameField.setBackground(fieldColor);
        usernameField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(fieldColor);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel msg = new JLabel("", SwingConstants.CENTER);
        msg.setForeground(Color.RED);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton loginButton = new JButton("Anmelden");
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setFocusPainted(false);

        JButton registerButton = new JButton("Registrieren");
        registerButton.setBackground(new Color(100, 180, 100));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Layout setzen
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Benutzername:"), gbc);

        gbc.gridy++;
        panel.add(usernameField, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Passwort:"), gbc);

        gbc.gridy++;
        panel.add(passwordField, gbc);

        gbc.gridy++;
        panel.add(loginButton, gbc);

        gbc.gridy++;
        panel.add(registerButton, gbc);

        gbc.gridy++;
        panel.add(msg, gbc);

        // Aktionen
        loginButton.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (userMap.containsKey(user) && userMap.get(user).equals(pass)) {
                dispose();
                starteFrage1();
            } else {
                msg.setText("❌ Benutzername oder Passwort ist falsch!");
            }
        });

        registerButton.addActionListener(e -> openRegisterWindow());

        add(panel);
        setVisible(true);
    }

    // Quiz-Fenster starten (Frage1)
    private void starteFrage1() {
        new Frage1(); // dein echtes Quiz-Fenster
    }

    // Registrierungsfenster
    private void openRegisterWindow() {
        JFrame registerFrame = new JFrame("📝 Benutzer registrieren");
        registerFrame.setSize(350, 200);
        registerFrame.setLocationRelativeTo(null);
        registerFrame.setLayout(new GridLayout(4, 2, 10, 10));

        JTextField newUserField = new JTextField();
        JPasswordField newPassField = new JPasswordField();
        JLabel info = new JLabel("");

        JButton saveButton = new JButton("Speichern");

        registerFrame.add(new JLabel("Benutzername:"));
        registerFrame.add(newUserField);
        registerFrame.add(new JLabel("Passwort:"));
        registerFrame.add(newPassField);
        registerFrame.add(new JLabel());
        registerFrame.add(saveButton);
        registerFrame.add(info);

        saveButton.addActionListener(e -> {
            String newUser = newUserField.getText();
            String newPass = new String(newPassField.getPassword());

            if (newUser.isEmpty() || newPass.isEmpty()) {
                info.setText("⚠ Bitte alle Felder ausfüllen!");
            } else if (userMap.containsKey(newUser)) {
                info.setText("⚠ Benutzer existiert bereits!");
            } else {
                userMap.put(newUser, newPass);
                info.setText("✅ Registrierung erfolgreich!");
                registerFrame.dispose();
                dispose();
                starteFrage1(); // Direkt ins Quiz nach Registrierung
            }
        });

        registerFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ModernQuizLogin::new);
    }
}
