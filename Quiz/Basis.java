package Quiz;

import javax.swing.*;
import java.awt.*;

/**
 * Die Klasse {@code Basis} stellt ein grundlegendes Fenster für eine Quizfrage dar.
 * Es enthält ein Label für die Frage sowie eine dynamische Anzahl an Antwortbuttons.
 * Dieses Grundlayout kann für verschiedene Quizfragen wiederverwendet werden.
 */
public class Basis extends JFrame {

    /** Label zur Anzeige der Quizfrage */
    protected JLabel frageLabel;

    /** Array von Buttons für die Antwortmöglichkeiten */
    protected JButton[] antwortButtons;

    /** ButtonGroup zur Gruppierung der Antwortbuttons (für spätere Erweiterung) */
    protected ButtonGroup antwortGruppe;

    /**
     * Konstruktor zur Erstellung des Quiz-Fensters mit Frage und Antwortmöglichkeiten.
     *
     * @param frage     Der Fragetext, der angezeigt werden soll.
     * @param antworten Ein Array von Strings mit den möglichen Antworten.
     */
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

            gbc.gridx = i % 2;               // 0 oder 1 (Spalte)
            gbc.gridy = 1 + (i / 2);         // Zeile steigt bei jeder 2. Antwort
            gbc.gridwidth = 1;
            gbc.weightx = 0.5;
            gbc.weighty = 0.2;
            add(antwortButtons[i], gbc);
        }

        setVisible(true);
    }
}
