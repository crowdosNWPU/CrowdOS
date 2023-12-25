package cn.crowdos.kernel;

import cn.crowdos.kernel.constraint.Constraint;

/**
 * <p>The {@code Decomposable} interface must be implement by any class
 * that needs to be decomposed into smaller parts. Usually decomposable
 * user-defied constraints (that implement interface {@link Constraint})
 * need to implement this interface.</p> //todo update doc
 *
 * @param <T> the type of element composed by the decomposer
 * @author loyx
 * @since 1.0.0
 * @see Decomposer
 * @see Constraint
 */
public interface Decomposable<T>{
    /**
     * Return a decomposer over elements of type {@code T}.
     *
     * @return a Decomposer
     */
    Decomposer<T> decomposer();
}
