package filum.ai.BackendEngineerIntern.model.payload.exception;

public class ErrorException extends RuntimeException {
    private final String status;

    public ErrorException(String status, String message) {
        super(message);
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

