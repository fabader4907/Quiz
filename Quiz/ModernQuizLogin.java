package Quiz;

import javax.swing.*;
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

    private Map<String, String> userMap = new HashMap<>();
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel msg;

    private String aktuellerBenutzer; // Zum Speichern des angemeldeten Benutzernamens

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
        passwordField.setEchoChar('•');

        JCheckBox showPassword = new JCheckBox("Passwort anzeigen");
        showPassword.setBackground(bgColor);
        showPassword.addActionListener(e -> {
            passwordField.setEchoChar(showPassword.isSelected() ? (char) 0 : '•');
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
     * Verarbeitet die Anmeldung des Benutzers.
     * Bei Erfolg wird das Quiz gestartet.
     */
    private void handleLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (userMap.containsKey(user) && userMap.get(user).equals(pass)) {
            aktuellerBenutzer = user; // Speichern des angemeldeten Benutzers
            dispose();
            starteQuizGame();
        } else {
            msg.setText("Benutzername oder Passwort ist falsch!");
        }
    }

    /**
     * Platzhalter zum Start des Quiz-Spiels.
     * Speichert am Ende einen Beispiel-Score.
     */
    private void starteQuizGame() {
        new Hauptmenu(aktuellerBenutzer);  // NEU: Statt direkt Spiel
    }


    /**
     * Öffnet ein Fenster zur Benutzerregistrierung.
     */
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
     * Speichert alle registrierten Benutzer in users.txt.
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
     * Lädt Benutzerdaten aus users.txt.
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
     * Speichert Name und Punktzahl in scores.txt.
     *
     * @param name   Spielername
     * @param punkte Punktzahl
     */
    private void speichereScore(String name, int punkte) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Quiz/scores.txt", true))) {
            writer.println(name + ":" + punkte);
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern der Punktzahl.");
        }
    }

    /**
     * Hauptmethode zum Starten der App.
     */
    public static void main(String[] args) {
        new ModernQuizLogin();
    }
}
