package filum.ai.BackendEngineerIntern.model.payload.exception;

public class CustomAccessDeniedException extends RuntimeException {
    private final String status;

    public CustomAccessDeniedException(String message) {
        super(message);
        this.status = "error";
    }

    public String getStatus() {
        return status;
    }
}

