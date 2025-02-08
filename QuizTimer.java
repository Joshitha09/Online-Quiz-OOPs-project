package Onlinequiz;

import java.util.Timer;
import java.util.TimerTask;

public class QuizTimer {
    private Timer timer;
    private TimerTask task;

    public QuizTimer(long durationInMillis, Runnable onTimeUp) {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                onTimeUp.run();
            }
        };
        timer.schedule(task, durationInMillis);
    }

    public void cancel() {
        timer.cancel();
        task.cancel();
    }
}
