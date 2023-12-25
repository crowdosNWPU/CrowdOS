package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.Decomposer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/**
 * <p>In <i>Mobile CrowdSensing</i> (MCS), there are a lots of constraints,
 * which vary in type.For decomposable constraints, e.g. do something on a
 * certain date in a certain year.This can be broken down into specific
 * temporal and spatial constraints.In addition, there are a number of
 * non-decomposable constraints.For this, We use reflection to return the
 * indecomposable constraint decomposer generator we need by getting
 * the Class object of the class created by the upper level developer.</p>
 *
 * <p>The constraints in any real MCS problem need to be mapped directly to
 * a class that implements the {@code Constraint} interface or extend from
 * some abstract Constraint class, such as IndecomposableConstraint</p>
 *
 * Each IndecomposableConstraint can map to a class that implement interface {@code Constraint}. For example, the
 * code for IndecomposableConstraint 1 is shown below.
 *
 * <pre>
 *   import cn.crowdos.kernel.Decomposer;
 *
 *   public class IndecomposableConstraint implements Constraint {
 *
 *      &#64;Override
 *      public Decomposer&#60;Constraint&#62; decomposer() {
 *          ArrayList&#60;Class&#60;?&#62;&#62; argsClass = new ArrayList&#60;&#62;();
 *          ArrayList&#60;Object&#62; args = new ArrayList&#60;&#62;();
 *          Class&#60;IndecomposableConstraint&#62; urgClass = IndecomposableConstraint.class;
 *          return new IndecomposableDecomposerGenerator(argsClass,args,urgClass);
 *      }
 *      ... // other methods
 *   }
 * </pre>
 *
 * <p>After that the constraint class will be involved in the kernel's task
 * allocation process.</p>
 *
 * @since 1.0.2
 * @author yuzy
 */
public class IndecomposableDecomposerGenerator implements Decomposer<Constraint> {
    private final List<Class<?>> argsClass;
    private final List<Object> args;
    private final Class<? extends Constraint> ucClass;

    public IndecomposableDecomposerGenerator(List<Class<?>> argsClass, List<Object> args, Class<? extends Constraint> ucClass) {
        this.argsClass = argsClass;
        this.args = args;
        this.ucClass = ucClass;
    }

    @Override
    public List<Constraint> trivialDecompose() {

        Constraint uc;
        try {
            int len = argsClass.size();
            Class<?>[] aClass = new Class[len];
            Object[] arg = new Object[len];
            for (int i = 0; i < len; i++) {
                aClass[i] = argsClass.get(i);
                arg[i] = args.get(i);
            }

            Constructor<? extends Constraint> constructor = ucClass.getConstructor(aClass);
            uc = constructor.newInstance(arg);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return Collections.singletonList(uc);
    }

    @Override
    public List<Constraint> scaleDecompose(int scale) {
        System.err.println("This constraint cannot be decomposed!");
        return this.trivialDecompose();
    }
}

