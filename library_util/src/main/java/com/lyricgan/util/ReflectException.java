package com.lyricgan.util;

import java.lang.reflect.InvocationTargetException;

/**
 * A unchecked wrapper for any of Java's checked reflection exceptions:
 * <p>
 * These exceptions are
 * <ul>
 * <li> {@link ClassNotFoundException}</li>
 * <li> {@link IllegalAccessException}</li>
 * <li> {@link IllegalArgumentException}</li>
 * <li> {@link InstantiationException}</li>
 * <li> {@link InvocationTargetException}</li>
 * <li> {@link NoSuchMethodException}</li>
 * <li> {@link NoSuchFieldException}</li>
 * <li> {@link SecurityException}</li>
 * </ul>
 */
public class ReflectException extends RuntimeException {
    private static final long serialVersionUID = -6213149635297151442L;

    public ReflectException() {
        super();
    }

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }
}
