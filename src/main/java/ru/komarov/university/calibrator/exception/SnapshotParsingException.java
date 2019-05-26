package ru.komarov.university.calibrator.exception;

/**
 * <p>
 * Created on 5/24/2019.
 *
 * @author Vasilii Komarov
 */
public class SnapshotParsingException extends RuntimeException {
    public SnapshotParsingException() {
        super();
    }

    public SnapshotParsingException(String message) {
        super(message);
    }

    public SnapshotParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
