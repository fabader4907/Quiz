package Quiz;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Die Klasse {@code AvatarAuswahl} stellt eine grafische Benutzeroberfläche dar,
 * über die ein Benutzer einen Avatar aus einer Auswahl auswählen kann.
 * Die Auswahl wird benutzerspezifisch gespeichert und beim nächsten Start geladen.
 */
public class AvatarAuswahl extends JFrame {

    private static final String AVATAR_FILE = "Quiz/user_avatars.txt";
    private Clip clip;

    /**
     * Konstruktor zur Initialisierung der Avatar-Auswahloberfläche.
     *
     * @param benutzername Der aktuell angemeldete Benutzername.
     */
    public AvatarAuswahl(String benutzername) {
        starteMusik("Quiz/music/quiz-music.wav");
        setTitle("Wähle deinen Avatar");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(20, 20, 30));

        JLabel header = new JLabel("Wähle deinen Avatar", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 36));
        header.setForeground(new Color(255, 215, 0));
        header.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(header, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 40, 40));
        gridPanel.setBackground(new Color(20, 20, 30));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 60, 80));

        String[] avatarPfad = {
                "Quiz/avatars/avatar1.jpg",
                "Quiz/avatars/avatar2.jpg",
                "Quiz/avatars/avatar3.jpg",
                "Quiz/avatars/avatar4.png",
                "Quiz/avatars/avatar5.png",
                "Quiz/avatars/avatar6.png"
        };

        for (String pfad : avatarPfad) {
            ImageIcon icon = new ImageIcon(pfad);
            Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            JButton avatarButton = new JButton(new ImageIcon(image));
            avatarButton.setBackground(Color.DARK_GRAY);
            avatarButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
            avatarButton.setFocusPainted(false);
            avatarButton.setToolTipText("Wähle diesen Avatar");
            avatarButton.addActionListener(e -> {
                saveAvatarForUser(benutzername, pfad);
                dispose();
                new Hauptmenu(benutzername, pfad);
            });
            gridPanel.add(avatarButton);
        }

        add(gridPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Speichert den ausgewählten Avatarpfad für den angegebenen Benutzer.
     *
     * @param benutzername Der Benutzername, dem der Avatar zugeordnet werden soll.
     * @param pfad         Der Pfad zum ausgewählten Avatarbild.
     */
    public static void saveAvatarForUser(String benutzername, String pfad) {
        Map<String, String> map = loadAvatarMap();
        map.put(benutzername, pfad);
        try (PrintWriter pw = new PrintWriter(new FileWriter(AVATAR_FILE))) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                pw.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern des Avatars: " + e.getMessage());
        }
    }

    /**
     * Lädt den Avatarpfad für den angegebenen Benutzer.
     * Falls kein Avatar vorhanden ist, wird ein Standardavatar zurückgegeben.
     *
     * @param benutzername Der Benutzername, dessen Avatar geladen werden soll.
     * @return Pfad zum Avatarbild.
     */
    public static String loadAvatarForUser(String benutzername) {
        Map<String, String> map = loadAvatarMap();
        return map.getOrDefault(benutzername, "Quiz/avatars/avatar1.jpg");
    }

    /**
     * Lädt alle Benutzer-Avatar-Zuordnungen aus der Datei.
     *
     * @return Eine Map mit Benutzernamen als Schlüssel und Avatarpfaden als Werte.
     */
    private static Map<String, String> loadAvatarMap() {
        Map<String, String> map = new HashMap<>();
        File file = new File(AVATAR_FILE);
        if (!file.exists()) return map;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    map.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Avatar-Datei: " + e.getMessage());
        }
        return map;
    }

    /**
     * Startet die Hintergrundmusik aus einer angegebenen WAV-Datei.
     * <p>
     * Die Musik wird geladen und in einer Endlosschleife abgespielt (Dauerschleife).
     * Es wird empfohlen, eine unkomprimierte WAV-Datei zu verwenden (z. B. PCM).
     * </p>
     *
     * @param dateipfad Der Pfad zur Musikdatei im WAV-Format.
     *                  Dies kann ein relativer Pfad (z. B. "musik.wav")
     *                  oder ein absoluter Pfad (z. B. "C:\\Musik\\titel.wav") sein.
     *
     * @throws IllegalArgumentException wenn die Datei nicht existiert oder
     *         ein Problem beim Abspielen auftritt.
     */
    private void starteMusik(String dateipfad) {
        try {
            File musikDatei = new File(dateipfad);
            if (!musikDatei.exists()) {
                System.err.println("Musikdatei nicht gefunden: " + dateipfad);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musikDatei);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Musik in Dauerschleife abspielen
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Beendet die aktuell laufende Hintergrundmusik, falls vorhanden.
     */
    private void stoppeMusik() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Überschreibt die dispose()-Methode, um beim Schließen die Musik zu stoppen.
     */
    @Override
    public void dispose() {
        stoppeMusik();
        super.dispose();
    }


}
