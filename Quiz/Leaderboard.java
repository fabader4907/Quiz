package Quiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Diese Klasse implementiert ein modernes Leaderboard-Fenster im Stil des Hauptmenüs.
 * Es zeigt eine Tabelle mit Spielernamen und deren kumulierten Punktzahlen an,
 * die aus einer Datei geladen und sortiert werden.
 * Das GUI ist farblich und typografisch ansprechend gestaltet.
 */
public class Leaderboard extends JFrame {

    /**
     * Pfad zur Datei, die die gespeicherten Punktestände enthält.
     * Format der Datei: "Spielername:Punktzahl"
     */
    private static final String SCORE_FILE = "Quiz/scores.txt";

    /**
     * Konstruktor für das Leaderboard-Fenster.
     * Initialisiert das Fenster, setzt Layout und fügt die UI-Komponenten hinzu.
     */
    public Leaderboard() {
        setTitle("Leaderboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fenster maximieren
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Fenster zentrieren

        // Hauptpanel mit Farbverlauf-Hintergrund
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                // Farbverlauf von dunkelblau zu etwas hellerem Blau
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 30, 50),
                        width, height, new Color(50, 50, 80));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
            }
        };

        // Verschiedene Bereiche des Fensters hinzufügen
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);    // Überschrift
        mainPanel.add(createTablePanel(), BorderLayout.CENTER); // Tabelle mit Scores
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH); // Zurück-Button

        add(mainPanel);
        setVisible(true); // Fenster anzeigen
    }

    /**
     * Erzeugt das obere Panel mit dem Titel des Leaderboards.
     * @return JPanel mit zentriertem Titel
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false); // transparent für Hintergrund durchscheinen lassen
        panel.setBorder(new EmptyBorder(40, 0, 20, 0)); // Abstand nach oben/unten

        JLabel title = new JLabel("Leaderboard – Highscores", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48)); // große Schriftart
        title.setForeground(new Color(255, 215, 0)); // goldene Farbe

        panel.setLayout(new BorderLayout());
        panel.add(title, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Erzeugt das mittlere Panel mit der Tabelle der Spieler und Punkte.
     * Die Daten werden aus der Datei SCORE_FILE gelesen und nach Punktzahl sortiert.
     * @return JScrollPane mit der erstellten Tabelle
     */
    private JScrollPane createTablePanel() {
        Map<String, Integer> scoreMap = new HashMap<>();

        // Datei lesen und Punktzahlen summieren
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] teile = zeile.split(":");
                if (teile.length == 2) {
                    String name = teile[0].trim();
                    int punkte = Integer.parseInt(teile[1].trim());
                    // Punkte aufsummieren, falls Spieler mehrfach vorkommt
                    scoreMap.put(name, scoreMap.getOrDefault(name, 0) + punkte);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Fehler beim Lesen der Datei!",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Einträge nach Punktzahl absteigend sortieren
        List<Map.Entry<String, Integer>> eintraege = new ArrayList<>(scoreMap.entrySet());
        eintraege.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        // Daten für JTable vorbereiten
        Object[][] daten = new Object[eintraege.size()][2];
        for (int i = 0; i < eintraege.size(); i++) {
            daten[i][0] = eintraege.get(i).getKey();
            daten[i][1] = eintraege.get(i).getValue();
        }

        String[] spalten = {"Spieler", "Punkte"};

        // Tabelle erstellen, Zellen nicht editierbar machen
        JTable tabelle = new JTable(new DefaultTableModel(daten, spalten)) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Tabellenformatierung
        tabelle.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        tabelle.setRowHeight(40);
        tabelle.setShowGrid(false);
        tabelle.setForeground(Color.WHITE);
        tabelle.setBackground(new Color(40, 40, 60));
        tabelle.setSelectionBackground(new Color(70, 130, 180));

        // Text zentrieren in allen Spalten
        DefaultTableCellRenderer zentriert = new DefaultTableCellRenderer();
        zentriert.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelle.getColumnCount(); i++) {
            tabelle.getColumnModel().getColumn(i).setCellRenderer(zentriert);
        }

        // Tabellenkopf formatieren
        JTableHeader header = tabelle.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setBackground(new Color(80, 80, 120));
        header.setForeground(Color.WHITE);

        // Tabelle in ScrollPane einfügen mit Rand und transparentem Hintergrund
        JScrollPane scrollPane = new JScrollPane(tabelle);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 60, 60, 60));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        return scrollPane;
    }

    /**
     * Erzeugt das untere Panel mit einem Button, um zum Hauptmenü zurückzukehren.
     * Der Button hat einen Schatteneffekt und passt farblich zum Hauptmenü.
     * @return JPanel mit dem Zurück-Button
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 40, 0));

        JButton backButton = new JButton("Zurück zum Hauptmenü") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Schatteneffekt zeichnen
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fill(new RoundRectangle2D.Double(5, 5, getWidth() - 1, getHeight() - 1, 20, 20));
                // Hintergrundfarbe des Buttons
                g2.setColor(new Color(100, 149, 237)); // Cornflower Blue
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.dispose();
                super.paintComponent(g);
            }
        };

        // Button-Einstellungen
        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        backButton.setPreferredSize(new Dimension(400, 80));

        // Aktion beim Klicken: Fenster schließen und Hauptmenü öffnen
        backButton.addActionListener(e -> {
            dispose(); // dieses Fenster schließen
            new Hauptmenu("Spieler"); // neues Hauptmenü öffnen (optional mit Spielername)
        });

        bottomPanel.add(backButton);
        return bottomPanel;
    }

    /**
     * Startmethode, die das Leaderboard im Event-Dispatch-Thread startet.
     * @param args Kommandozeilenargumente (nicht genutzt)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Leaderboard::new);
    }
}