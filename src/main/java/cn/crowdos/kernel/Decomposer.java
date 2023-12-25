package cn.crowdos.kernel;
import java.util.Iterator;
import java.util.List;


/**
 * <p>A decomposer and decompose a object that implement {@link Decomposable} into
 * multiple fine-grained parts.</p>
 *
 * <p>This interface provides two styles of access to decompositions.</p>
 * <ul>
 *     <li>Collection style that return a {@link List}.</li>
 *     <li>Iterator style that return a {@link Iterator}.</li>
 * </ul>
 * // todo update doc
 *
 * @param <T> the type of decompositions returned by this decomposer.
 * @author loyx
 * @since 1.0.0
 * @see Decomposer
 */
public interface Decomposer<T> {

    /**
     * <p>Decompose a decomposable. {@link Decomposer#trivialDecompose()} by default.
     * @return List style decompositions.
     */
    default List<T> decompose(){
        return trivialDecompose();
    }

    /**
     * <p>Decompose a decomposable by specified scale. {@link Decomposer#scaleDecompose(int)} by default.
     * @param scale specified decomposition scale.
     * @return List style decompositions.
     * @throws DecomposeException if any exceptions occur during decompose.
     */
    default List<T> decompose(int scale) throws DecomposeException {
        return scaleDecompose(scale);
    }

    List<T> trivialDecompose();

    List<T> scaleDecompose(int scale) throws DecomposeException;


}
