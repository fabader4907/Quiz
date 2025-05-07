import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class LeaderboardFenster extends JFrame {

    public LeaderboardFenster(String aktuellerBenutzer) {
        setTitle("🏆 Leaderboard");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 16));
        area.setEditable(false);

        List<UserScore> scores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("leaderboard.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    try {
                        int score = Integer.parseInt(parts[1]);
                        scores.add(new UserScore(parts[0], score));
                    } catch (NumberFormatException e) {}
                }
            }
        } catch (IOException e) {
            area.setText("❌ Fehler beim Laden des Leaderboards.");
        }

        scores.sort((a, b) -> Integer.compare(b.punkte, a.punkte));

        StringBuilder sb = new StringBuilder("🏆 TOP 5 Spieler:\n\n");
        for (int i = 0; i < Math.min(5, scores.size()); i++) {
            UserScore us = scores.get(i);
            sb.append(i + 1).append(". ").append(us.name).append(" - ").append(us.punkte).append(" Punkte");
            if (us.name.equals(aktuellerBenutzer)) {
                sb.append("  <--- DU");
            }
            sb.append("\n");
        }

        area.setText(sb.toString());
        add(new JScrollPane(area));
        setVisible(true);
    }

    private static class UserScore {
        String name;
        int punkte;

        public UserScore(String name, int punkte) {
            this.name = name;
            this.punkte = punkte;
        }
    }
}
