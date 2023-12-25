package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * A constraint that represents a deadline in the form of a LocalDate. It ensures that any date condition satisfies the
 * constraint by making sure it is on or before the specified end date. It can also be decomposed into a list of sub-constraints
 * that form a sequence of non-overlapping time intervals that cover the time between now and the end date.
 */
public class DateDeadline implements Constraint{
    private final LocalDate endDate;

    /**
     *
     * Constructs a new DateDeadline constraint with the specified end date.
     * @param endDate The end date of the constraint.
     * @throws InvalidConstraintException If the end date is before the current date.
     */
    public DateDeadline(LocalDate endDate) throws InvalidConstraintException{
        this.endDate = endDate;
        if(endDate.isBefore(LocalDate.now())){
            throw new InvalidConstraintException(String.format("endDate %s is before currentDate %s",this.endDate,LocalDate.now()));
        }
    }

    /**
     *
     * Constructs a new DateDeadline constraint with the specified end date string in the format "yyyy-MM-dd".
     * @param endDate The end date of the constraint in the format "yyyy-MM-dd".
     * @throws InvalidConstraintException If the end date is before the current date or if the date string is invalid.
     */
    public DateDeadline(String endDate) throws InvalidConstraintException{
        this(LocalDate.parse(endDate));
    }

    /**
     *
     * Returns a Decomposer object that can be used to decompose this Constraint into smaller sub-Constraints.
     * The trivialDecompose() method returns a single sub-Constraint that represents the entire time range of this Constraint.
     * The scaleDecompose(scale) method returns a list of sub-Constraints, where the number of sub-Constraints is equal to the provided scale parameter. Each sub-Constraint represents a non-overlapping time interval within the time range of this Constraint. The start and end dates of each interval are calculated based on the current date and the endDate of this Constraint, and the scale parameter is used to determine the size of each interval.
     *  @return a Decomposer object that can be used to decompose this Constraint
     */
    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try{
                    return Collections.singletonList(new DateDeadline(endDate));
                } catch (InvalidConstraintException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                if (scale<0) throw new DecomposeException("invalid decompose scale");
                if (scale==1) return trivialDecompose();
                Duration duration = Duration.between(LocalDate.now().atStartOfDay(),endDate.atStartOfDay());
                long totalHours = duration.toDays()*24;
                long hoursPerInterval = totalHours/scale;
                List<Constraint> subConstraints = new ArrayList<>(scale);
                for(int i=0;i<scale;i++){
                    LocalDate intervalStartDate = LocalDate.now().plusDays((i*hoursPerInterval)/24);
                    LocalDate intervalEndDate = intervalStartDate.plusDays(hoursPerInterval/24);
                    try{
                        subConstraints.add(new DateInterval(intervalStartDate,intervalEndDate));
                    }catch(InvalidConstraintException e){
                        throw new DecomposeException(e);
                    }
                }
                return subConstraints;
            }
        };
    }

    /**
     *
     * Checks if the specified condition satisfies the constraint by making sure it is on or before the specified end date.
     * @param condition The condition to check.
     * @return True if the condition satisfies the constraint, false otherwise.
     */
    @Override
    public boolean satisfy(Condition condition) {
        if(!(condition instanceof Date)) return false;
        Date date = (Date) condition;
        LocalDate localDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
        return localDate.equals(LocalDate.now()) || (localDate.isAfter(LocalDate.now())) &&localDate.isBefore(endDate);
    }

    /**
     *
     * This class represents a date deadline condition.
     * It implements the interface Condition and specifies that the condition class is DateCondition.
     * It contains an endDate field which represents the deadline date.
     */

    @Override
    public Class<? extends Condition> getConditionClass() {
        return DateCondition.class;
    }

    /**
     * Returns a string representation of the DateDeadline object.
     * The string includes the endDate field.
     * @return a string representation of the DateDeadline object.
     */
    @Override
    public String toString(){
        return "DateDeadline{" +
                "endDate=" + endDate +
                '}';
    }

    /**
     * Returns a string representation of the DateDeadline object, which is the same as toString().
     *  @return a string representation of the DateDeadline object.
     */

    @Override
    public String description() {
        return toString();
    }
}
