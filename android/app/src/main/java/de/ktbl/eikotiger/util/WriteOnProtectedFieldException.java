package de.ktbl.eikotiger.util;

public class WriteOnProtectedFieldException extends RuntimeException {
    public WriteOnProtectedFieldException() {
        super();
    }

    public WriteOnProtectedFieldException(String message) {
        super(message);
    }

    public WriteOnProtectedFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public WriteOnProtectedFieldException(Throwable cause) {
        super(cause);
    }
}
