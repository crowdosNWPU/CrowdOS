package cn.crowdos.kernel;

/**
 * Thrown to indicate that some operation has occurred on an uninitialized {@link Kernel}.
 * the {@link Kernel} should be initialized by {@link Kernel#initial()} before it can
 * accept messages.
 *
 * <p>{@code UninitializedException} is <em>unchecked
 * exceptions</em>.  Unchecked exceptions do <em>not</em> need to be
 * declared in a method or constructor's {@code throws} clause if they
 * can be thrown by the execution of the method or constructor and
 * propagate outside the method or constructor boundary.
 *
 * @author  loyx
 * @since   1.0.0
 * @see Kernel
 */
public class UninitializedKernelException extends RuntimeException{


    private static final long serialVersionUID = -8648370761596264456L;

    /** Constructs a new UninitializedException with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public UninitializedKernelException() {
        super();
    }

    /** Constructs a new UninitializedException with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public UninitializedKernelException(String message) {
        super(message);
    }

    /**
     * Constructs a new UninitializedException with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param  message the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A {@code null} value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public UninitializedKernelException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs a new UninitializedException with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of
     * {@code cause}).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A {@code null} value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     */
    public UninitializedKernelException(Throwable cause) {
        super(cause);
    }
}
