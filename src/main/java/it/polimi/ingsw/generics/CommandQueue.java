package it.polimi.ingsw.generics;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandQueue
{
    private final ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<>();
    private Thread thread = new Thread(this::threadFunc);
    private static final Logger logger = Logger.getLogger("CommandQueue");

    public CommandQueue (){
        thread.start();
    }

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
    public void run(Runnable newFunc){
        queue.add(newFunc);
        synchronized (queue) {
            queue.notifyAll();
        }
    }
}
