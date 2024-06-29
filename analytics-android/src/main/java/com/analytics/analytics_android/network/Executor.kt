package com.analytics.analytics_android.network



import androidx.annotation.RestrictTo
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future


@RestrictTo(RestrictTo.Scope.LIBRARY)
object Executor {
    private var executor: ExecutorService? = null

    var threadCount =3
        /**
         * Changes the amount of threads the scheduler will be able to use.
         * @param count the thread count
         */
        @JvmStatic
        set(count) {
            if (count >= 2) {
                field = count
            }
        }


    @Synchronized
    @JvmStatic
    private fun getExecutor(): ExecutorService {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(threadCount)
        }
        return executor!!
    }

    /**
     * Sends a runnable to the executor service.
     *
     * @param runnable the runnable to be queued
     * @param exceptionHandler the handler of exception raised by the runnable
     */
    @JvmStatic
    fun execute(runnable: Runnable?, exceptionHandler: ExceptionHandler?) {
        val executor = getExecutor()
        try {
            executor.execute {
                try {
                    runnable?.run()
                } catch (t: Throwable) {
                    exceptionHandler?.handle(t)
                }
            }
        } catch (e: Exception) {
            exceptionHandler?.handle(e)
        }
    }

    /**
     * Sends a callable to the executor service and
     * returns a Future.
     *
     * @param callable the callable to be queued
     * @return the future object to be queried
     */
    @JvmStatic
    fun futureCallable(callable: Callable<*>): Future<*> {
        return getExecutor().submit(callable)
    }

    /**
     * Shuts the executor service down and resets
     * the executor to a null state.
     */
    @JvmStatic
    fun shutdown(): ExecutorService? {
        if (executor != null) {
            executor!!.shutdown()
            val es = executor
            executor = null
            return es
        }
        return null
    }

    fun interface ExceptionHandler {
        fun handle(t: Throwable?)
    }
}
