package org.bahmni.feed.openelis.feed;

public class FeedException extends RuntimeException {
    public FeedException(String message) {
        super(message);
    }

    public FeedException(Throwable cause) {
        super(cause);
    }
}