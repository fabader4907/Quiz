package Quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Die Klasse {@code AnmeldeFeld} stellt ein GUI-Fenster dar, über das sich
 * Benutzer mit einem Benutzernamen und Passwort anmelden können.
 * Für Demonstrationszwecke sind Benutzername und Passwort fest im Code hinterlegt.
 */
public class AnmeldeFeld {

    // GUI-Komponenten
    private JFrame frame;
    private JTextField userTextField;
    private JPasswordField passwordField;
    private JLabel errorMessageLabel;

    // Feste Zugangsdaten zur Demonstration
    private final String CORRECT_USERNAME = "quizuser";
    private final String CORRECT_PASSWORD = "1234";

    /**
     * Einstiegspunkt der Anwendung.
     * Initialisiert das Anmeldefenster auf dem Event-Dispatch-Thread.
     *
     * @param args Kommandozeilenargumente (nicht verwendet)
     */
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

    /**
     * Konstruktor der Klasse.
     * Initialisiert das GUI-Fenster.
     */
    public AnmeldeFeld() {
        initialize();
    }

    /**
     * Initialisiert alle GUI-Komponenten des Anmeldefensters.
     */
    private void initialize() {
        frame = new JFrame("Anmeldung zum Quiz");
        frame.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(46, 139, 87));
        frame.getContentPane().add(panel);

        JLabel titleLabel = new JLabel("Willkommen zum Quiz!");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(550, 100, 500, 60);
        panel.add(titleLabel);

        JLabel userLabel = new JLabel("Benutzername:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(550, 250, 150, 30);
        panel.add(userLabel);

        userTextField = new JTextField();
        userTextField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userTextField.setBounds(550, 290, 400, 30);
        panel.add(userTextField);

        JLabel passwordLabel = new JLabel("Passwort:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(550, 330, 100, 30);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordField.setBounds(550, 370, 400, 30);
        panel.add(passwordField);

        errorMessageLabel = new JLabel("");
        errorMessageLabel.setForeground(Color.RED);
        errorMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        errorMessageLabel.setBounds(550, 420, 400, 30);
        panel.add(errorMessageLabel);

        JButton loginButton = new JButton("Anmelden");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginButton.setBackground(new Color(30, 144, 255));
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

    /**
     * Überprüft die eingegebenen Zugangsdaten und zeigt bei Erfolg eine Begrüßung,
     * bei Fehler eine Fehlermeldung an.
     */
    private void checkLogin() {
        String username = userTextField.getText();
        char[] passwordArray = passwordField.getPassword();
        String password = new String(passwordArray);

        if (CORRECT_USERNAME.equals(username) && CORRECT_PASSWORD.equals(password)) {
            JOptionPane.showMessageDialog(frame, "Willkommen, " + username + "!", "Erfolgreich angemeldet", JOptionPane.INFORMATION_MESSAGE);
            // Hier kann das Hauptprogramm gestartet werden
        } else {
            errorMessageLabel.setText("Falscher Benutzername oder Passwort");
        }
    }
}
