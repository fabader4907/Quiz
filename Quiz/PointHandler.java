package Quiz;

public class PointHandler {
    private int points;
    private Thread thread;

    public PointHandler() {
        points = 1000;

        thread = new Thread(() -> {
            try {
                while (points > 0) {
                    points -= 50;
                    Thread.sleep(1000); // alle 1 Sekunde 50 abziehen
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void start() {
        thread.start();
    }

    public int getPoints() {
        return points;
    }
}
