// NEW CONCEPT: THREADING
// Runs a timer in separate thread from the main program; contains methods to get elapsed time, start and stop the timer
// Here, a separate thread is used so that the time can continue counting while the user enters input in the main thread
public class TimerThread {
    private static volatile boolean running = false; // Reserved word volatile caches the variable in shared memory,
                                                    // rather than private, such that changes to running
                                                    // are immediately visible across threads.
    private static int seconds = 0;

    // Returns elapsed time
    public static int getElapsedTime() {
        return seconds;
    }

    // Stops timer
    public static void stop() {
        running = false;
    }

    // Resets timer
    public static void reset() {
        seconds = 0;
    }

    // Creates a new thread, resets timer, starts, and adds to seconds every 1000 milliseconds
    public static void run() {
        if (running) return;
        running = true;
        Thread timer = new Thread(() -> { // Creates a new thread that counts seconds that have elapsed
            seconds = 0;
            while (running) {
                try {
                    Thread.sleep(1000);
                    seconds++;
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        timer.start(); // Reserved method start() activates the timer thread
    }
}
