package ar.com.fwcommon.componentes.error;

public class FWRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -6834059432795429807L;

	public FWRuntimeException() {
        super();
    }

    public FWRuntimeException(String message) {
        super(message);
    }

    public FWRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FWRuntimeException(Throwable cause) {
        super(cause);
    }

}