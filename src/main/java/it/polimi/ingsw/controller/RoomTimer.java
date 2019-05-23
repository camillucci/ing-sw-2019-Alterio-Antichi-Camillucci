package it.polimi.ingsw.controller;

import it.polimi.ingsw.generics.Event;
import it.polimi.ingsw.generics.IEvent;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomTimer
{
    public final IEvent<RoomTimer, Integer> timeoutEvent = new Event<>();
    public final IEvent<RoomTimer, Integer> timerTickEvent = new Event<>();

    private ScheduledThreadPoolExecutor timer;
    private int timeout;
    private int period;
    private int elapsed = 0;

    Logger logger = Logger.getLogger("roomTimer");

    public RoomTimer(int timeout, int period){
        this.timeout = timeout;
        this.period = period;
    }

    private void onTimerTick(){
        elapsed += period;
        if(elapsed >= timeout) {
            elapsed = 0;
            ((Event<RoomTimer, Integer>)timeoutEvent).invoke(this, timeout);
            this.stop();
        }
        else
            ((Event<RoomTimer, Integer>)timerTickEvent).invoke(this, elapsed);
    }

    public void start(){
        timer = new ScheduledThreadPoolExecutor(1);
        timer.scheduleWithFixedDelay(this::onTimerTick, period, period, TimeUnit.SECONDS);
    }

    public void stop(){
        if(timer == null)
            return;

        timer.shutdown();
        while (true) {
            try {
                if (timer.awaitTermination(200, TimeUnit.MILLISECONDS)) {
                    timer = null;
                    break;
                }
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, e.getMessage());
            }
        }
    }
}
