package wojia.link.ddns.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author wilson mywilson2019@gmail.com </br>
 * @since 2023/6/21 <br/>
 */
@UtilityClass
public class RetryUtil {

    private static final Consumer<Throwable> defaultErrorHandler = e -> {};

    public static <T> T retryIfThrowException(Callable<T> callable, int times, long period) throws Throwable {
        return retryIfThrowException(callable, times, period, defaultErrorHandler);
    }

    public static <T> T retryIfThrowException(Callable<T> callable, int times, long period, Consumer<Throwable> errorHandler) throws Exception {
        int count = 0;
        while (true) {
            try {
                return callable.call();
            } catch (Throwable e) {
                errorHandler.accept(e);
                count++;

                if (count >= times) {
                    throw e;
                }
            }

            try {
                TimeUnit.MICROSECONDS.sleep(period);
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void retryIfThrowException(Runnable runnable, int times, long period) throws Throwable {
        retryIfThrowException(runnable, times, period, defaultErrorHandler);
    }

    public static void retryIfThrowException(Runnable runnable, int times, long period, Consumer<Throwable> errorHandler) throws Exception {
        int count = 0;
        while (true) {
            try {
                runnable.run();
                return;
            } catch (Throwable e) {
                errorHandler.accept(e);
                count++;

                if (count >= times) {
                    throw e;
                }
            }

            try {
                TimeUnit.MICROSECONDS.sleep(period);
            } catch (InterruptedException ignored) {
            }
        }
    }

}
