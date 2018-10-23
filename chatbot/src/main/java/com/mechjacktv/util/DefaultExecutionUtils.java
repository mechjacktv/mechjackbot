package com.mechjacktv.util;

import com.mechjacktv.util.function.RunnableWithException;
import com.mechjacktv.util.function.SupplierWithException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

public class DefaultExecutionUtils implements ExecutionUtils {

    private static final Logger log = LoggerFactory.getLogger(DefaultExecutionUtils.class);

    /**
     * @deprecated Not deprecated, but discouraged. Use alternative.
     */
    @Override
    @Deprecated
    public void softenException(RunnableWithException runnable) {
        softenException(runnable, SoftenedException.class);
    }

    @Override
    public void softenException(RunnableWithException runnable, Class<? extends RuntimeException> exceptionClass) {
        softenException(() -> {
            runnable.run();
            return null;
        }, exceptionClass);
    }

    /**
     * @deprecated Not deprecated, but discouraged. Use alternative.
     */
    @Override
    @Deprecated
    public <T> T softenException(SupplierWithException<T> supplier) {
        return softenException(supplier, SoftenedException.class);
    }

    @Override
    public <T> T softenException(SupplierWithException<T> supplier, Class<? extends RuntimeException> exceptionClass) {
        try {
            return supplier.get();
        } catch (final RuntimeException e) {
            throw e;
        } catch (final Exception e1) {
            try {
                try {
                    final Constructor<? extends RuntimeException> constructor = exceptionClass
                            .getConstructor(String.class, Exception.class);

                    throw constructor.newInstance(e1.getMessage(), e1);
                } catch (final NoSuchMethodException e2) {
                    final Constructor<? extends RuntimeException> constructor = exceptionClass
                            .getConstructor(Exception.class);

                    throw constructor.newInstance(e1);
                }
            } catch (final Exception e2) {
                log.error(String.format("Failed to instantiate RuntimeException. exceptionClass=%s, ",
                        exceptionClass.getCanonicalName()), e2);
                throw new SoftenedException(e1.getMessage(), e1);
            }
        }
    }

}
