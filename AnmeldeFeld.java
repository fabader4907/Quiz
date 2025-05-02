import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnmeldeFeld {

    // Anmeldefeld Fenster
    private JFrame frame;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JLabel errorMessageLabel;

    // Benutzername und Passwort (für die Demo fest kodiert)
    private final String CORRECT_USERNAME = "quizuser";
    private final String CORRECT_PASSWORD = "1234";

    // Hauptmethode
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                AnmeldeFeld window = new AnmeldeFeld();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Konstruktor
    public AnmeldeFeld() {
        initialize();
    }

    // Initialisiere das Fenster
    private void initialize() {
        frame = new JFrame("Anmeldung zum Quiz");
        frame.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel für Layout
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(46, 139, 87)); // Moderne Hintergrundfarbe
        frame.getContentPane().add(panel);

        // Titel
        JLabel titleLabel = new JLabel("Willkommen zum Quiz!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(550, 100, 500, 60);
        panel.add(titleLabel);

        // Benutzername Label
        JLabel userLabel = new JLabel("Benutzername:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(550, 250, 150, 30);
        panel.add(userLabel);

        // Benutzername Textfeld
        userTextField = new JTextField();
        userTextField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userTextField.setBounds(550, 290, 400, 30);
        panel.add(userTextField);

        // Passwort Label
        JLabel passwordLabel = new JLabel("Passwort:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(550, 330, 100, 30);
        panel.add(passwordLabel);

        // Passwort Textfeld
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordField.setBounds(550, 370, 400, 30);
        panel.add(passwordField);

        // Fehlermeldung (wenn der Benutzername/Passwort falsch ist)
        errorMessageLabel = new JLabel("");
        errorMessageLabel.setForeground(Color.RED);
        errorMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        errorMessageLabel.setBounds(550, 420, 400, 30);
        panel.add(errorMessageLabel);

        // Anmelden Button
        JButton loginButton = new JButton("Anmelden");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setBackground(new Color(30, 144, 255)); // Modernes Blau
        loginButton.setForeground(Color.WHITE);
        loginButton.setBounds(550, 470, 400, 40);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLogin();
            }
        });
        panel.add(loginButton);
    }

    // Überprüfen des Benutzernamens und Passworts
    private void checkLogin() {
        String username = userTextField.getText();
        char[] passwordArray = passwordField.getPassword();
        String password = new String(passwordArray);

        // Wenn Benutzername und Passwort korrekt sind, öffne neues Fenster
        if (CORRECT_USERNAME.equals(username) && CORRECT_PASSWORD.equals(password)) {
            JOptionPane.showMessageDialog(frame, "Willkommen, " + username + "!", "Erfolgreich angemeldet", JOptionPane.INFORMATION_MESSAGE);
            // Hier könnte weiteres Programm folgen (z.B. das Quiz starten)
        } else {
            // Wenn falsche Eingabe, zeige Fehlermeldung
            errorMessageLabel.setText("Falscher Benutzername oder Passwort");
        }
    }
}
