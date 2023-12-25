package cn.crowdos.kernel.wrapper;

import cn.crowdos.kernel.constraint.Condition;

public abstract class PrimitiveCondition<T> implements Condition {
    public final T primitive;

    protected PrimitiveCondition(T primitive) {
        this.primitive = primitive;
    }
}
