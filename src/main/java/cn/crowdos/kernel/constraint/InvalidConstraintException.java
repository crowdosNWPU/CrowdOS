package cn.crowdos.kernel.constraint;

import java.io.NotSerializableException;

/**
 * Thrown to indicate that Constrain is not reasonable or can not
 * be satisfied.<P>
 *
 * Note, that although InvalidConstraintException inherits Serializable
 * interface from {@link Exception}, it is not intended to be {@link java.io.Serializable}.
 * Appropriate serialization methods are implement to throw {@link NotSerializableException}.<p>
 *
 * @see Constraint
 * @since 1.0.0
 * @serial exclude
 * @author loyx
 */
public class InvalidConstraintException extends Exception {

    private static final long serialVersionUID = -5699601484610582437L;

    /**
     * Constructs an InvalidConstraintException with the specified
     * cause.
     *
     * @param cause the cause (which is saved for later retrieval by the
     *         {@link Throwable#getCause()} method).
     */
    @Deprecated
    public InvalidConstraintException(Throwable cause) {
        super(cause==null ? null : cause.toString());
        this.initCause(cause);
    }

    /**
     * Constructs an InvalidConstraintException with the specified
     * detail message.
     *
     * @param message the detail message. The detail message is saved for
     *          later retrieval by the {@link Throwable#getMessage()} method.
     */
    public InvalidConstraintException(String message) {
        super(message);
    }

    @Deprecated
    public InvalidConstraintException(){super();}

    /**
     * Throws NotSerializableException, since InvalidConstraintException
     * objects are not intended to be serializable.
     */
    private void writeObject(java.io.ObjectOutputStream out)
            throws NotSerializableException
    {
        throw new NotSerializableException("Not serializable.");
    }

    /**
     * Throws NotSerializableException, since InvalidConstraintException
     * objects are not intended to be serializable.
     */
    private void readObject(java.io.ObjectInputStream in)
            throws NotSerializableException
    {
        throw new NotSerializableException("Not serializable.");
    }
}
