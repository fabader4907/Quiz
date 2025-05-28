package Quiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * ModernQuizLogin ist das Haupt-Anmeldefenster für das Quizspiel.
 * Benutzer können sich anmelden oder registrieren.
 * Nach dem Spiel wird der Score gespeichert.
 */
public class ModernQuizLogin extends JFrame {

    /** Map zur Speicherung von Benutzernamen und Passwörtern */
    private Map<String, String> userMap = new HashMap<>();

    /** Textfeld für den Benutzernamen */
    private JTextField usernameField;

    /** Passwortfeld für das Passwort */
    private JPasswordField passwordField;

    /** Label zur Anzeige von Fehler- oder Erfolgsmeldungen */
    private JLabel msg;

    /** Der aktuell angemeldete Benutzer */
    private String aktuellerBenutzer;

    /**
     * Konstruktor zum Initialisieren des Login-Fensters.
     */
    public ModernQuizLogin() {
        loadUsers();

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
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Willkommen beim Quiz", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(33, 37, 41));

        usernameField = new JTextField();
        usernameField.setBackground(fieldColor);
        usernameField.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        usernameField.setPreferredSize(new Dimension(300, 50));

        passwordField = new JPasswordField();
        passwordField.setBackground(fieldColor);
        passwordField.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        passwordField.setPreferredSize(new Dimension(300, 50));
        passwordField.setEchoChar('•');

        JCheckBox showPassword = new JCheckBox("Passwort anzeigen");
        showPassword.setBackground(bgColor);
        showPassword.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•');
        });

        msg = new JLabel("", SwingConstants.CENTER);
        msg.setForeground(Color.RED);
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JButton loginButton = new JButton("Anmelden");
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(200, 60));

        JButton registerButton = new JButton("Registrieren");
        registerButton.setBackground(new Color(100, 180, 100));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        registerButton.setPreferredSize(new Dimension(200, 60));

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

    /**
     * Behandelt den Login-Vorgang und öffnet bei Erfolg das Hauptmenü oder das Admin-Panel.
     */
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (userMap.containsKey(user) && userMap.get(user).equals(pass)) {
            aktuellerBenutzer = user;
            dispose();

            if (user.equalsIgnoreCase("Admin") && pass.equals("1")) {
                new AdminThemenauswahl();
            } else {
                String avatarPfad = AvatarAuswahl.loadAvatarForUser(aktuellerBenutzer);
                new Hauptmenu(aktuellerBenutzer, avatarPfad);
            }
        } else {
            msg.setText("Benutzername oder Passwort ist falsch!");
        }
    }

    /**
     * Startet das Quizspiel und lädt den Avatar für den aktuell angemeldeten Benutzer.
     */
    private void starteQuizGame() {
        String avatarPfad = AvatarAuswahl.loadAvatarForUser(aktuellerBenutzer);
        new Hauptmenu(aktuellerBenutzer, avatarPfad);
    }

    /**
     * Öffnet das Registrierungsfenster, in dem neue Benutzer erstellt werden können.
     */
    private void openRegisterWindow() {
        JDialog registerDialog = new JDialog(this, "Benutzer registrieren", true);
        registerDialog.setSize(500, 300);
        registerDialog.setLocationRelativeTo(this);
        registerDialog.setLayout(new GridLayout(4, 2, 15, 15));

        JTextField newUserField = new JTextField();
        newUserField.setPreferredSize(new Dimension(250, 40));
        JPasswordField newPassField = new JPasswordField();
        newPassField.setPreferredSize(new Dimension(250, 40));
        JLabel info = new JLabel("");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton saveButton = new JButton("Speichern");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        saveButton.setPreferredSize(new Dimension(150, 50));

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
                saveUsers();
                info.setText("Registrierung erfolgreich!");
                registerDialog.dispose();
                msg.setText("Bitte melde dich nun an.");
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        registerDialog.setVisible(true);
    }

    /**
     * Speichert alle Benutzerinformationen dauerhaft in einer Datei.
     */
    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Quiz/users.txt"))) {
            for (Map.Entry<String, String> entry : userMap.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Benutzerdaten.");
        }
    }

    /**
     * Lädt die gespeicherten Benutzerdaten aus der Datei beim Start der Anwendung.
     */
    private void loadUsers() {
        File file = new File("Quiz/users.txt");
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

    /**
     * Speichert den Punktestand eines Benutzers in die Score-Datei.
     *
     * @param name   Der Name des Benutzers
     * @param punkte Die erreichte Punktzahl
     */
    private void speichereScore(String name, int punkte) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Quiz/scores.txt", true))) {
            writer.println(name + ":" + punkte);
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Punktzahl.");
        }
    }
}
