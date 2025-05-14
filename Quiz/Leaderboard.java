package Quiz;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Leaderboard {

    private static final String SCORE_FILE = "Quiz/scores.txt";

    public Leaderboard() {
        zeigeLeaderboard();
    }

    private void zeigeLeaderboard() {
        Map<String, Integer> scoreMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(SCORE_FILE))) {
            String zeile;
            while ((zeile = reader.readLine()) != null) {
                String[] teile = zeile.split(":");
                if (teile.length == 2) {
                    String name = teile[0].trim();
                    int punkte = Integer.parseInt(teile[1].trim());
                    scoreMap.put(name, Math.max(scoreMap.getOrDefault(name, 0), punkte));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Fehler beim Lesen der Datei!", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Map.Entry<String, Integer>> eintraege = new ArrayList<>(scoreMap.entrySet());
        eintraege.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        Object[][] daten = new Object[eintraege.size()][2];
        for (int i = 0; i < eintraege.size(); i++) {
            daten[i][0] = eintraege.get(i).getKey();
            daten[i][1] = eintraege.get(i).getValue();
        }

        // GUI Setup
        JFrame fenster = new JFrame("🏆 Leaderboard");
        fenster.setSize(500, 400);
        fenster.setLocationRelativeTo(null);
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenster.setBackground(Color.decode("#f4f4f4"));

        String[] spalten = {"Spieler", "Punkte"};
        JTable tabelle = new JTable(new DefaultTableModel(daten, spalten)) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Design: Tabelle
        tabelle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabelle.setRowHeight(35);
        tabelle.setGridColor(Color.LIGHT_GRAY);
        tabelle.setShowGrid(false);
        tabelle.setSelectionBackground(new Color(220, 235, 255));

        // Header Design
        JTableHeaderDesign(tabelle);

        // Zentrierte Zellen
        DefaultTableCellRenderer zentriert = new DefaultTableCellRenderer();
        zentriert.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tabelle.getColumnCount(); i++) {
            tabelle.getColumnModel().getColumn(i).setCellRenderer(zentriert);
        }

        // ScrollPane mit Padding
        JScrollPane scrollPane = new JScrollPane(tabelle);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        fenster.add(scrollPane, BorderLayout.CENTER);
        fenster.getContentPane().setBackground(Color.decode("#ffffff"));
        fenster.setVisible(true);
    }

    /**
     * Custom Header-Design für moderne Optik.
     */
    private void JTableHeaderDesign(JTable tabelle) {
        tabelle.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 17));
        tabelle.getTableHeader().setOpaque(true);
        tabelle.getTableHeader().setBackground(new Color(240, 240, 240));
        tabelle.getTableHeader().setForeground(Color.DARK_GRAY);
    }
}
