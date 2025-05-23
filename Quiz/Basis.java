package Quiz;

import javax.swing.*;
import java.awt.*;
import javax.sound.sampled.*;

public class Basis extends JFrame {
    protected JLabel frageLabel;
    protected JButton[] antwortButtons;
    protected ButtonGroup antwortGruppe;

    public Basis(String frage, String[] antworten/*, String bildPfad, String soundPfad*/) {
        // Fenster-Einstellungen
        setTitle("Quiz Frage");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fenster maximieren
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;

        // Frage Label
        frageLabel = new JLabel(frage, SwingConstants.CENTER);
        frageLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.weighty = 0.1;
        add(frageLabel, gbc);

        // Antwortmöglichkeiten mit Buttons (2 Spalten)
        antwortGruppe = new ButtonGroup();
        antwortButtons = new JButton[antworten.length];

        for (int i = 0; i < antworten.length; i++) {
            antwortButtons[i] = new JButton(antworten[i]);
            antwortButtons[i].setFont(new Font("Arial", Font.PLAIN, 30));
            antwortButtons[i].setPreferredSize(new Dimension(400, 150));
            antwortGruppe.add(antwortButtons[i]);

            gbc.gridx = i % 2;               // 0 oder 1
            gbc.gridy = 1 + (i / 2);         // Zeile steigt pro 2 Antworten
            gbc.gridwidth = 1;
            gbc.weightx = 0.5;
            gbc.weighty = 0.2;
            add(antwortButtons[i], gbc);
        }

        setVisible(true);
    }
}
