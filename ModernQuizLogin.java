// Importiere notwendige Klassen für GUI und Datenstruktur
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

// Die Klasse "ModernQuizLogin" erweitert JFrame, um ein GUI-Fenster zu erstellen
public class ModernQuizLogin extends JFrame {

    // Map zur Speicherung von Benutzernamen und Passwörtern
    private Map<String, String> userMap = new HashMap<>();

    // GUI-Komponenten für Benutzereingabe und Anzeige von Nachrichten
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel msg;

    // Konstruktor – baut das Anmeldefenster auf
    public ModernQuizLogin() {
        loadUsers(); // Lade Benutzerdaten aus Datei

        setTitle("Quiz Anmeldung");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

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

        usernameField = new JTextField();
        usernameField.setBackground(fieldColor);
        usernameField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        passwordField = new JPasswordField();
        passwordField.setBackground(fieldColor);
        passwordField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        passwordField.setEchoChar('•'); // Bullet-Zeichen

        // Checkbox zum Anzeigen des Passworts
        JCheckBox showPassword = new JCheckBox("Passwort anzeigen");
        showPassword.setBackground(bgColor);
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0); // Passwort sichtbar
            } else {
                passwordField.setEchoChar('•'); // Passwort versteckt
            }
        });

        msg = new JLabel("", SwingConstants.CENTER);
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

        // Komponenten hinzufügen
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
        panel.add(showPassword, gbc);

        gbc.gridy++;
        panel.add(loginButton, gbc);

        gbc.gridy++;
        panel.add(registerButton, gbc);

        gbc.gridy++;
        panel.add(msg, gbc);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> openRegisterWindow());

        add(panel);
        setVisible(true);
    }

    // Login-Verarbeitung
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (userMap.containsKey(user) && userMap.get(user).equals(pass)) {
            dispose();
            starteFrage1();
        } else {
            msg.setText("Benutzername oder Passwort ist falsch!");
        }
    }

    // Starte das Quiz (extern zu implementieren)
    private void starteFrage1() {
        new Frage1(); // Platzhalter
    }

    // Registrierungsfenster
    private void openRegisterWindow() {
        JDialog registerDialog = new JDialog(this, "Benutzer registrieren", true);
        registerDialog.setSize(350, 200);
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setLayout(new GridLayout(4, 2, 10, 10));

        JTextField newUserField = new JTextField();
        JPasswordField newPassField = new JPasswordField();
        JLabel info = new JLabel("");

        JButton saveButton = new JButton("Speichern");

        registerDialog.add(new JLabel("Benutzername:"));
        registerDialog.add(newUserField);
        registerDialog.add(new JLabel("Passwort:"));
        registerDialog.add(newPassField);
        registerDialog.add(new JLabel());
        registerDialog.add(saveButton);
        registerDialog.add(info);

        saveButton.addActionListener(e -> {
            String newUser = newUserField.getText();
            String newPass = new String(newPassField.getPassword());

            if (newUser.isEmpty() || newPass.isEmpty()) {
                info.setText("Bitte alle Felder ausfüllen!");
            } else if (userMap.containsKey(newUser)) {
                info.setText("Benutzer existiert bereits!");
            } else {
                userMap.put(newUser, newPass);
                saveUsers(); // Daten dauerhaft speichern
                info.setText("Registrierung erfolgreich!");
                registerDialog.dispose();
                msg.setText("Bitte melde dich nun an.");
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        registerDialog.setVisible(true);
    }

    // Speichert alle Benutzerdaten in eine Datei
    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.txt"))) {
            for (Map.Entry<String, String> entry : userMap.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Benutzerdaten.");
        }
    }

    // Lädt Benutzerdaten aus Datei
    private void loadUsers() {
        File file = new File("users.txt");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] teile = zeile.split(":");
                if (teile.length == 2) {
                    userMap.put(teile[0], teile[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Benutzerdaten.");
        }
    }

    // Main-Methode zum Starten der Anwendung
    public static void main(String[] args) {
        new ModernQuizLogin();
    }
}