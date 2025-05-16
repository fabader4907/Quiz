package Quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AdminThemenauswahl extends JFrame {

    private final Color backgroundColorStart = new Color(30, 30, 50);
    private final Color backgroundColorEnd = new Color(50, 50, 80);
    private final Color buttonColor = new Color(100, 149, 237);
    private final Color buttonTextColor = new Color(255, 255, 255);

    public AdminThemenauswahl() {
        setTitle("Themenbereiche – Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gradient = new GradientPaint(0, 0, backgroundColorStart, width, height, backgroundColorEnd);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
            }
        };

        JLabel titleLabel = new JLabel("Admin – Themenauswahl", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 30, 30, 30);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.CENTER;


        // Drei Themen-Buttons
        String[] themen = {"Themenbereich 1", "Themenbereich 2", "Themenbereich 3"};
        for (int i = 0; i < themen.length; i++) {
            gbc.gridy = i;
            JButton btn = createStyledButton(themen[i]);
            buttonPanel.add(btn, gbc);

            int bereich = i + 1;
            btn.addActionListener(e -> {
                dispose(); // Admin-Fenster schließen
                switch (bereich) {
                    case 1 -> new FragenerstellenStart("Quiz/allgemein.txt");
                    case 2 -> new FragenerstellenStart("Quiz/lieder.txt");
                    case 3 -> new FragenerstellenStart("Quiz/lyrics.txt");
                }
            });

        }

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 100));
                g2.fill(new RoundRectangle2D.Double(5, 5, getWidth() - 1, getHeight() - 1, 20, 20));

                g2.setColor(buttonColor);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));

                g2.dispose();
                super.paintComponent(g);
            }
        };


        btn.setFont(new Font("Arial", Font.BOLD, 26));
        btn.setForeground(buttonTextColor);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        btn.setPreferredSize(new Dimension(600, 100));

        return btn;
    }
}
