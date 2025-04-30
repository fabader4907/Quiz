import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Basis extends JFrame {
    protected JLabel frageLabel;
    protected JLabel bildLabel;
    protected JButton[] antwortButtons;
    protected ButtonGroup antwortGruppe;
    protected JButton button;
    protected Clip soundClip;

    public Basis(String frage, String[] antworten/*, String bildPfad, String soundPfad*/) {
        // Fenster-Einstellungen
        setTitle("Quiz Frage");
        setSize(2000, 2000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Frage Label
        frageLabel = new JLabel(frage);
        frageLabel.setFont(new Font("Arial", Font.BOLD, 50));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(frageLabel, gbc);

        /* Bild hinzufügen (optional)
        if (bildPfad != null && !bildPfad.isEmpty()) {
            ImageIcon icon = new ImageIcon(bildPfad);
            bildLabel = new JLabel(icon);
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            add(bildLabel, gbc);
        }*/

        // Antwortmöglichkeiten mit Buttons
        antwortGruppe = new ButtonGroup();
        antwortButtons = new JButton[antworten.length];
        for (int i = 0; i < antworten.length; i++) {
            antwortButtons[i] = new JButton(antworten[i]);
            antwortGruppe.add(antwortButtons[i]);
            gbc.gridy = 2 + i;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            add(antwortButtons[i], gbc);
        }


        /* Sound abspielen (optional)
        if (soundPfad != null && !soundPfad.isEmpty()) {
            playSound(soundPfad);
        }
        */


        setVisible(true);
    }

    /*Funktion zum Abspielen eines Sounds
    private void playSound(String soundPfad) {
        try {
            File soundFile = new File(soundPfad);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            soundClip = AudioSystem.getClip();
            soundClip.open(audioIn);
            soundClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Sound konnte nicht abgespielt werden: " + e.getMessage());
        }
    }
    */
}
