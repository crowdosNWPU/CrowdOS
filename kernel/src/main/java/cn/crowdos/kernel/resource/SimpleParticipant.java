package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.constraint.Condition;
import cn.crowdos.kernel.constraint.Coordinate;

public class SimpleParticipant extends AbstractParticipant{

    @ability
    Coordinate location;

    @ability
    int something;

    @Override
    public boolean hasAbility(Class<? extends Condition> conditionClass) {
        return false;
    }

    @Override
    public Condition getAbility(Class<? extends Condition> conditionClass) {
        return null;
    }
}
