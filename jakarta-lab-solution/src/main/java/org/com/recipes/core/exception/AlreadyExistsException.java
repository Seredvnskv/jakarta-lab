package org.com.recipes.core.exception;

import java.io.Serial;

public class AlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4651210440067308620L;

    public AlreadyExistsException(String message) {
        super(message);
    }
}
