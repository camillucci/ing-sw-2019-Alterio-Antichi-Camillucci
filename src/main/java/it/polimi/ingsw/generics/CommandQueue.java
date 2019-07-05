package it.polimi.ingsw.generics;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class used to get a bottle neck effect for when multiple threads should be activated from the server at the
 * same time. The class is used to create a queue, that stocks the waiting threads so that they don't run in a parallel
 * way.
 */
public class CommandQueue
{
    private final ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<>();

    /**
     * Thread used to run functions in order to obtain a bottle neck effect
     */
    private Thread thread = new Thread(this::threadFunc);
    private static final Logger logger = Logger.getLogger("CommandQueue");

    public CommandQueue (){
        thread.start();
    }

    /**
     * Method that endlessly checks if there are functions waiting in the queue. If that's the case, the function is
     * executed. If, instead, there are no functions waiting, the queue is put to a temporary stop.
     */
    private void threadFunc() {
        while(true)
        {
            Runnable cur = queue.poll();
            if(cur != null)
                cur.run();
            else
                try {
                    synchronized (queue) {
                        queue.wait();
                    }
                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, e.getMessage());
                }
        }
    }

    /**
     * Function is added to the queue and will be executed when the others are finished.
     * @param newFunc Function that's going to be added to the queue
     */
    public void run(Runnable newFunc){
        queue.add(newFunc);
        synchronized (queue) {
            queue.notifyAll();
        }
    }
}
