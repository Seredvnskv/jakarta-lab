package org.com.recipes.core.exception;

import java.io.Serial;

public class InvalidDataException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8060683743317273156L;

    public InvalidDataException() {
        super();
    }

    public InvalidDataException(Throwable cause) {
        super(cause);
    }

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
