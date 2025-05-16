package Quiz;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FragenerstellenStart extends JFrame {

    private JTextField frageField;
    private JTextField[] antwortFields = new JTextField[4];
    private JComboBox<String> richtigeAntwortBox;
    private String dateiname; // z.B. "Quiz/allgemein.txt"

    public FragenerstellenStart(String dateiname) {
        this.dateiname = dateiname;

        setTitle("Frage erstellen");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel titel = new JLabel("Neue Quizfrage erstellen", SwingConstants.CENTER);
        titel.setFont(new Font("Arial", Font.BOLD, 32));
        titel.setForeground(new Color(200, 200, 200));
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        frageField = new JTextField();
        frageField.setPreferredSize(new Dimension(600, 40));
        JLabel frageLabel = createLabel("Frage:");
        add(frageLabel, gbc);
        gbc.gridx = 1;
        add(frageField, gbc);
        gbc.gridx = 0;

        // Antwortfelder
        String[] buchstaben = {"A", "B", "C", "D"};
        for (int i = 0; i < 4; i++) {
            gbc.gridy++;
            JLabel lbl = createLabel("Antwort " + buchstaben[i] + ":");
            antwortFields[i] = new JTextField();
            antwortFields[i].setPreferredSize(new Dimension(600, 40));
            add(lbl, gbc);
            gbc.gridx = 1;
            add(antwortFields[i], gbc);
            gbc.gridx = 0;
        }

        gbc.gridy++;
        JLabel richtigLbl = createLabel("Richtige Antwort (A–D):");
        richtigeAntwortBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        add(richtigLbl, gbc);
        gbc.gridx = 1;
        add(richtigeAntwortBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JButton speichernBtn = new JButton("Frage speichern");
        speichernBtn.setBackground(new Color(100, 149, 237));
        speichernBtn.setForeground(Color.WHITE);
        speichernBtn.setFont(new Font("Arial", Font.BOLD, 22));
        speichernBtn.setPreferredSize(new Dimension(300, 60));
        add(speichernBtn, gbc);
        speichernBtn.addActionListener(e -> frageSpeichern());

// Zurück-Button
        gbc.gridy++;
        gbc.gridx = 1;
        JButton zurueckBtn = new JButton("Zurück");
        zurueckBtn.setBackground(new Color(220, 70, 70));
        zurueckBtn.setForeground(Color.WHITE);
        zurueckBtn.setFont(new Font("Arial", Font.BOLD, 22));
        zurueckBtn.setPreferredSize(new Dimension(300, 60));
        add(zurueckBtn, gbc);

        zurueckBtn.addActionListener(e -> {
            dispose(); // Fenster schließen
            new AdminThemenauswahl(); // Zurück zum Admin-Menü
        });

        setVisible(true);

    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(220, 220, 220));
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        return label;
    }

    private void frageSpeichern() {
        String frage = frageField.getText().trim();
        String[] antworten = new String[4];
        for (int i = 0; i < 4; i++) {
            antworten[i] = antwortFields[i].getText().trim();
        }
        String richtig = (String) richtigeAntwortBox.getSelectedItem();

        if (frage.isEmpty() || antworten[0].isEmpty() || antworten[1].isEmpty() ||
                antworten[2].isEmpty() || antworten[3].isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte alle Felder ausfüllen.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Format:
        // Frage: ...
        // A: ...
        // B: ...
        // C: ...
        // D: ...
        // Richtig: ...
        try (PrintWriter out = new PrintWriter(new FileWriter(dateiname, true))) {
            out.println("Frage: " + frage);
            out.println("A: " + antworten[0]);
            out.println("B: " + antworten[1]);
            out.println("C: " + antworten[2]);
            out.println("D: " + antworten[3]);
            out.println("Richtig: " + richtig);
            out.println(); // Leere Zeile als Trenner
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Speichern.", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Frage gespeichert!", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        frageField.setText("");
        for (JTextField tf : antwortFields) tf.setText("");
    }
}
    