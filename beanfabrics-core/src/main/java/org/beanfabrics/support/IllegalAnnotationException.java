/*
 * Beanfabrics Framework Copyright (C) by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.support;

/**
 * @author Max Gensthaler
 */
@SuppressWarnings("serial")
public class IllegalAnnotationException extends RuntimeException {
    public IllegalAnnotationException() {
        super();
    }

    public IllegalAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalAnnotationException(String message) {
        super(message);
    }

    public IllegalAnnotationException(Throwable cause) {
        super(cause);
    }
}