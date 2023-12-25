package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * TemporalInterval constraint
 *
 * @author loyx
 * @since 1.0.1
 */
public class TemporalInterval implements Constraint{
    private final LocalTime startTime;
    private final LocalTime endTime;

    /**
     * The TemporalInterval function takes in two LocalTime objects and creates a TemporalInterval object.
     *
     * @param start Set the start time of the temporalinterval
     * @param end Create a localtime object
     * @throws InvalidConstraintException {@link InvalidConstraintException}
     */
    public TemporalInterval(String start, String end) throws InvalidConstraintException {
        this(LocalTime.parse(start), LocalTime.parse(end));
    }

    /**
     * The TemporalInterval function takes in two LocalTime objects, startTime and endTime.
     * It then checks to see if the end time is before the start time. If it is, an InvalidConstraintException
     * will be thrown with a message explaining that the end time cannot be before the start time. Otherwise,
     * this function will return nothing and set both of these values as instance variables for later use in other functions.
     *
     * @param startTime Set the starttime of the temporalinterval
     * @param  endTime Set the endtime of the temporalinterval object
     * @throws InvalidConstraintException {@link InvalidConstraintException}
     */
    public TemporalInterval(LocalTime startTime, LocalTime endTime) throws InvalidConstraintException{
        this.startTime = startTime;
        this.endTime = endTime;
        if (endTime.isBefore(startTime)){
            throw new InvalidConstraintException(String.format("endTime %s before startTime %s", this.startTime, this.endTime));
        }
    }

    /**
     * The decomposer function is used to split a constraint into smaller constraints.
     * This is useful for when the constraint cannot be satisfied by any single resource,
     * but can be satisfied by multiple resources working together. For example, if a user wants to book an
     * hour-long meeting room from 9am-10am on Monday, and there are no rooms available at that time slot, then the
     * decomposer function will split up that one hour into two half-hour slots (9am-9:30am and 9:30pm - 10pm) so
     * that it can find two different rooms which are both free during those times.
     *
     * @return A decomposer object
     */
    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try {
                    return Collections.singletonList(new TemporalInterval(startTime, endTime));
                } catch (InvalidConstraintException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                if (scale < 0) throw new DecomposeException("invalid decompose scale");

                if (scale == 1) return trivialDecompose();
                Duration duration = Duration.between(startTime, endTime);
                long totalSeconds = duration.getSeconds();
                long secondsPerInterval = totalSeconds / scale;
                List<Constraint> subConstraints = new ArrayList<>(scale);
                for (int i = 0; i < scale; i++) {
                    LocalTime intervalStartTime = startTime.plusSeconds(i * secondsPerInterval);
                    LocalTime intervalEndTime = intervalStartTime.plusSeconds(secondsPerInterval);
                    try {
                        subConstraints.add(new TemporalInterval(intervalStartTime, intervalEndTime));
                    } catch (InvalidConstraintException e) {
                        throw new DecomposeException(e);
                    }
                }
                return subConstraints;
            }
        };
    }

    /**
     * The satisfy function checks if the given condition is a Date object, and if so,
     * it converts that date to a LocalTime object. It then checks whether the time
     * of day represented by that LocalTime is between startTime and endTime (inclusive).

     *
     * @param condition Check if the condition is satisfied
     *
     * @return True if the condition is a date and it's between the starttime and endtime
     *
     */
    @Override
    public boolean satisfy(Condition condition) {
        if (!(condition instanceof Date)) return false;
        Date date = (Date) condition;
        LocalTime localTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
        return localTime.equals(startTime) || (localTime.isAfter(startTime) && localTime.isBefore(endTime));
    }

    /**
     * get the condition Class object
     *
     * @return The class of the condition that is used to check whether a cell matches the filter criteria
     *
     */
    @Override
    public Class<? extends Condition> getConditionClass() {
        return DateCondition.class;
    }

    /**
     * The toString function is a function that returns the string representation of an object.
     * In this case, it will return the start time and end time of a temporal interval.
     *
     * @return A string representation of the object
     *
     */
    @Override
    public String toString() {
        return "TemporalInterval{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    /**
     * The description function returns a string that describes the object.
     *
     * @return A string representation of the object
     *
     */
    @Override
    public String description() {
        return this.toString();
    }
}
