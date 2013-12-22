package utilities;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by me on 12/21/13.
 */
public class MWareThreadPool {

    private int poolSize;
    private ExecutorService executorService;
    private static MWareThreadPool instance = null;
    /**
     * this variable prohibits multiple attempts of running this shutdown procedure,
     * since the first thread which is calling this sets shutDown to true...
     */
    private boolean shutDown = false;

    /**
     * time to wait for (in seconds) existing tasks to terminate, after shutDown-mehtod was called
     */
    private final int SHUTDOWN_WAITING_TIME_SECS = 60;

    /**
     * @param poolSize size of the FixedThreadPool, if < 2, a CachedThreadPool is used instead
     */
    private MWareThreadPool(int poolSize) {

        if (poolSize < 2) {
            this.executorService = Executors.newCachedThreadPool();
        } else {
            this.poolSize = poolSize;
            this.executorService = Executors.newFixedThreadPool(poolSize);
            ((ThreadPoolExecutor) this.executorService).setMaximumPoolSize(poolSize);
        }
    }

    private MWareThreadPool() {
        this.executorService = Executors.newCachedThreadPool();
    }


    /**
     * @param poolSize size of the FixedThreadPool, if < 2, a CachedThreadPool is used instead, obviously only adjustable with the first call of this factory-method, because MWareThreadPool is a singleton
     * @return MWareThreadPool with the ExecutorServiceType that is/was set the first time tis method is/was called
     */
    public static synchronized MWareThreadPool getFixedThreadPoolInstance(int poolSize) {
        if (instance == null) {
            instance = new MWareThreadPool(poolSize);
        }

        return instance;
    }

    /**
     * creates a MWareThreadPool as a (non fixed) CachedThreadPool if not former constructed by using another factory methode, if so MWareThreadPool stays as it is and the singleton instance is given back to you
     *
     * @return MWareThreadPool with the ExecutorServiceType that is/was set the first time tis method is/was called
     */
    public static synchronized MWareThreadPool getCachedThreadPoolInstance() {
        if (instance == null) {
            instance = new MWareThreadPool();
        }

        return instance;

    }

    /**
     * creates a MWareThreadPool as a (non fixed) CachedThreadPool if not former constructed by using another factory methode, if so MWareThreadPool stays as it is and the singleton instance is given back to you
     *
     * @return MWareThreadPool with the ExecutorServiceType that is/was set the first time tis method is/was called
     */
    public static synchronized MWareThreadPool getInstance() {
        if (instance == null) {
            instance = new MWareThreadPool();
        }
        return instance;
    }

    public synchronized void execute(Thread thread) {
        this.executorService.execute(thread);


    }

    public synchronized Class getExecutorServiceType() {
        return this.executorService.getClass();
    }


    public synchronized void shutdownAndAwaitTermination() {

        // shutDown variable prohibits multiple attempts of running this shutdown procedure,
        // since the first thread which is calling this sets shutDown to true...
        if (this.shutDown == false) {
            this.shutDown = true;
            this.executorService.shutdown(); // Disable new tasks from being submitted
            try {
                // Wait a while for existing tasks to terminate
                if (!this.executorService.awaitTermination(SHUTDOWN_WAITING_TIME_SECS, TimeUnit.SECONDS)) {
                    this.executorService.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!this.executorService.awaitTermination(SHUTDOWN_WAITING_TIME_SECS, TimeUnit.SECONDS))
                        System.err.println("Pool did not terminate");

                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                this.executorService.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
    }


}
