package Quiz;

/**
 * Die Klasse PointHandler verwaltet einen Punktewert, der mit der Zeit automatisch sinkt.
 * Beim Starten des zugehörigen Threads werden alle 1 Sekunde 50 Punkte abgezogen,
 * bis 0 erreicht ist.
 */
public class PointHandler {

    /** Aktueller Punktestand */
    private int points;

    /** Thread, der die Punkte automatisch reduziert */
    private Thread thread;

    /**
     * Konstruktor, der den Startwert auf 1000 Punkte setzt und den Thread vorbereitet,
     * der jede Sekunde 50 Punkte abzieht.
     */
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

    /**
     * Startet den Thread zur automatischen Punkte-Reduktion.
     */
    public void start() {
        thread.start();
    }

    /**
     * Gibt den aktuellen Punktestand zurück.
     *
     * @return der aktuelle Punktestand
     */
    public int getPoints() {
        return points;
    }
}
