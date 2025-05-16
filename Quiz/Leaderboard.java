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
 * Modern gestaltetes Leaderboard im Stil des Hauptmenüs.
 */
public class Leaderboard extends JFrame {

    private static final String SCORE_FILE = "Quiz/scores.txt";

    public Leaderboard() {
        setTitle("Leaderboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 30, 50), width, height, new Color(50, 50, 80));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
            }
        };

        mainPanel.add(createTopPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(40, 0, 20, 0));

        JLabel title = new JLabel("Leaderboard – Highscores", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(new Color(255, 215, 0)); // Goldfarben

        panel.setLayout(new BorderLayout());
        panel.add(title, BorderLayout.CENTER);
        return panel;
    }

    private JScrollPane createTablePanel() {
        Map<String, Integer> scoreMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] teile = zeile.split(":");
                if (teile.length == 2) {
                    String name = teile[0].trim();
                    int punkte = Integer.parseInt(teile[1].trim());
                    scoreMap.put(name, scoreMap.getOrDefault(name, 0) + punkte);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Lesen der Datei!", "Fehler", JOptionPane.ERROR_MESSAGE);
        }

        List<Map.Entry<String, Integer>> eintraege = new ArrayList<>(scoreMap.entrySet());
        eintraege.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        Object[][] daten = new Object[eintraege.size()][2];
        for (int i = 0; i < eintraege.size(); i++) {
            daten[i][0] = eintraege.get(i).getKey();
            daten[i][1] = eintraege.get(i).getValue();
        }

        String[] spalten = {"Spieler", "Punkte"};
        JTable tabelle = new JTable(new DefaultTableModel(daten, spalten)) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelle.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        tabelle.setRowHeight(40);
        tabelle.setShowGrid(false);
        tabelle.setForeground(Color.WHITE);
        tabelle.setBackground(new Color(40, 40, 60));
        tabelle.setSelectionBackground(new Color(70, 130, 180));

        DefaultTableCellRenderer zentriert = new DefaultTableCellRenderer();
        zentriert.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelle.getColumnCount(); i++) {
            tabelle.getColumnModel().getColumn(i).setCellRenderer(zentriert);
        }

        JTableHeader header = tabelle.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setBackground(new Color(80, 80, 120));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tabelle);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 60, 60, 60));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        return scrollPane;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 40, 0));

        JButton backButton = new JButton("Zurück zum Hauptmenü") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Schatteneffekt
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fill(new RoundRectangle2D.Double(5, 5, getWidth() - 1, getHeight() - 1, 20, 20));

                // Button-Hintergrund
                g2.setColor(new Color(100, 149, 237)); // gleiches Blau wie im Hauptmenü
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));

                g2.dispose();
                super.paintComponent(g);
            }
        };

        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        backButton.setPreferredSize(new Dimension(400, 80));

        // Aktion: zurück zum Hauptmenü
        backButton.addActionListener(e -> {
            dispose();
            new Hauptmenu("Spieler"); // Optional: Spielername übergeben
        });

        bottomPanel.add(backButton);
        return bottomPanel;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Leaderboard::new);
    }
}