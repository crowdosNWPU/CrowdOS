package cn.crowdos.kernel.common;

import cn.crowdos.kernel.constraint.Condition;
import cn.crowdos.kernel.wrapper.DateCondition;
import cn.crowdos.kernel.resource.AbstractParticipant;
import cn.crowdos.kernel.resource.ability;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeParticipant extends AbstractParticipant {
    @ability
    final DateCondition activeTime;
    private final SimpleDateFormat format;

    public TimeParticipant(String time) {
        format = new SimpleDateFormat("yyyy.MM.dd");
        try {
            this.activeTime = new DateCondition(format.parse(time).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        status = ParticipantStatus.AVAILABLE;
    }

    @Override
    public boolean hasAbility(Class<? extends Condition> conditionClass) {
        return conditionClass == DateCondition.class;
    }

    @Override
    public Condition getAbility(Class<? extends Condition> conditionClass) {
        if (!hasAbility(conditionClass))
            return null;
        else return activeTime;
    }

    @Override
    public String toString() {
        return "P(" + format.format(activeTime) + ")";
    }
}
