package it.polimi.ingsw.controller;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to provide the events and methods relative to the timers used by the turn and login handlers
 */
public class RoomTimer
{
    /**
     * Event invoked when the timer reaches an end
     */
    public final IEvent<RoomTimer, Integer> timeoutEvent = new Event<>();

    /**
     * Event invoked every *period* once the timer is started
     */
    public final IEvent<RoomTimer, Integer> timerTickEvent = new Event<>();

    private ScheduledThreadPoolExecutor timer;

    /**
     * Duration of the timer
     */
    private int timeout;

    /**
     * Amount of time between every timerTick event
     */
    private int period;

    /**
     * Amount of seconds passed since the start of the timer
     */
    private int elapsed = 0;

    Logger logger = Logger.getLogger("roomTimer");

    /**
     * Constructor that assigns the input parameters to their correspondences
     * @param timeout Duration of the timer
     * @param period Amount of time between every timerTick event
     */
    public RoomTimer(int timeout, int period){
        this.timeout = timeout;
        this.period = period;
    }

    /**
     * Method called when timerTick event is invoked. Updates elapsed and checks whether the timeout limit has been
     * reached. If that's the case, the timer is stopped.
     */
    private synchronized void onTimerTick(){
        elapsed += period;
        if(elapsed >= timeout) {
            ((Event<RoomTimer, Integer>)timeoutEvent).invoke(this, timeout);
            this.stop();
        }
        else
            ((Event<RoomTimer, Integer>)timerTickEvent).invoke(this, elapsed);
    }

    /**
     * Restarts the timer
     */
    public synchronized void reset()
    {
        elapsed = 0;
    }

    /**
     * Creates a new timer if there wasn't one running already
     */
    public synchronized void start(){
        if(timer != null)
            return;

        timer = new ScheduledThreadPoolExecutor(1);
        timer.scheduleWithFixedDelay(this::onTimerTick, period, period, TimeUnit.SECONDS);
    }

    /**
     * Stops the timer if there was one running already.
     */
    public synchronized void stop(){
        if(timer == null)
            return;

        timer.shutdown();
        while (true) {
            try {
                if (timer.awaitTermination(20000, TimeUnit.MILLISECONDS)) {
                    timer = null;
                    break;
                }
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, e.getMessage());
            }
        }
    }

    public int getElapsed(){
        return elapsed;
    }
}
