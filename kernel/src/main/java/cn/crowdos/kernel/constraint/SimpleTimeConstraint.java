/*
 * Copyright 2019-2025 CrowdOS_Group, Inc. <https://github.com/crowdosNWPU/CrowdOS>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * imitations under the License.
 */
package cn.crowdos.kernel.constraint;

import cn.crowdos.kernel.DecomposeException;
import cn.crowdos.kernel.Decomposer;
import cn.crowdos.kernel.wrapper.DateCondition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Simple TIme Constraint
 *
 * @author loyx
 * @since 1.0.0
 */
public class SimpleTimeConstraint implements Constraint{

    Date[] dateRange;

    /**
     * The SimpleTimeConstraint function is a constructor for the SimpleTimeConstraint class.
     * It takes two Date objects as parameters, and throws an InvalidConstraintException if the startDate is after or equal to endDate.
     * If no exception is thrown, it sets dateRange to be a Date array containing startDate at index 0 and endDate at index 1.
     *
     * @param startDate Set the start date of the constraint
     * @param endDate Set the end date of the time constraint
     * @throws InvalidConstraintException {@link InvalidConstraintException}
     */
    public SimpleTimeConstraint(Date startDate, Date endDate) throws InvalidConstraintException {
        if (startDate.compareTo(endDate) >= 0) throw new InvalidConstraintException();
        dateRange = new Date[]{startDate, endDate};
    }

    /**
     * The SimpleTimeConstraint function takes in two strings, startDateStr and endDateStr.
     * It then parses these strings into Date objects using the SimpleDateFormat class.
     * If either of these operations fail, it throws an InvalidConstraintException with the cause as its parameter.
     * Otherwise, it checks if startDate is before or equal to endDate; if so, it throws an InvalidConstraintException without a parameter.
     * Finally, dateRange is set to be a Date array containing both dates in chronological order (start first).
     *
     * @param startDateStr Set the start date of the constraint
     * @param endDateStr Parse the end date
     * @throws InvalidConstraintException {@link InvalidConstraintException}
     */
    public SimpleTimeConstraint(String startDateStr, String endDateStr) throws InvalidConstraintException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date startDate;
        Date endDate;
        try {
            startDate = simpleDateFormat.parse(startDateStr);
            endDate = simpleDateFormat.parse(endDateStr);
        } catch (ParseException e) {
            throw new InvalidConstraintException(e.getCause());
        }
        if (startDate.compareTo(endDate) >= 0) throw new InvalidConstraintException();
        dateRange = new Date[]{startDate, endDate};
    }

    /**
     * The satisfy function checks if the given condition is a Date object and
     * whether it falls within the date range of this DateRangeCondition.
     *
     * @param condition Compare the daterange[0] and daterange[0] to the condition
     *
     * @return True if the given date is within the range of start and end dates
     */
    @Override
    public boolean satisfy(Condition condition) {
        if (!(condition instanceof Date)) return false;
        Date date = (Date) condition;
        return dateRange[0].compareTo(date) <= 0 && date.compareTo(dateRange[1]) < 0;
    }

    /**
     * get Condition Class
     *
     * @return The condition class
     */
    @Override
    public Class<? extends Condition> getConditionClass() {
        return DateCondition.class;
    }


    /**
     * The description function returns a string that describes the object.
     *
     * @return A string that is the name of the item
     */
    @Override
    public String description() {
        return this.toString();
    }

    /**
     * The decomposer function is used to decompose a constraint into smaller constraints.
     * The trivialDecompose function returns the smallest possible set of constraints that are equivalent to this one.
     * The scaleDecompose function returns a list of n sub-constraints, where n is the given scale parameter.
     *
     * @return A list of constraints
     */
    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try {
                    return Collections.singletonList(new SimpleTimeConstraint(dateRange[0], dateRange[1]));
                } catch (InvalidConstraintException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public List<Constraint> scaleDecompose(int scale) throws DecomposeException {
                if (scale < 0) throw new DecomposeException("invalid decompose scale");

                if (scale == 1) return trivialDecompose();
                long tLen = (long) Math.ceil(1.0*(dateRange[1].getTime() - dateRange[0].getTime()) / scale);
                List<Constraint> subConstraints = new ArrayList<>(scale);
                for (int i = 0; i < scale-1; i++) {
                    long st = dateRange[0].getTime() + i * tLen;
                    long et = Math.min(dateRange[0].getTime() + (i+1)*tLen, dateRange[1].getTime());
                    try {
                        subConstraints.add(new SimpleTimeConstraint(new Date(st), new Date(et)));
                    } catch (InvalidConstraintException e) {
                        throw new DecomposeException(e);
                    }
                }
                return subConstraints;
            }
        };
    }

    /**
     * The toString function returns a string representation of the TimeConstraint object.
     *
     * @return The date range of the time constraint
     */
    @Override
    public String toString() {
        return "TimeConstraint(" + dateRange[0] + "-" + dateRange[1] + ")";
    }
}
