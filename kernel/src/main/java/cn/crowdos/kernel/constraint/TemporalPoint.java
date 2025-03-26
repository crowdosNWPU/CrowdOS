/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University, Inc. <https://github.com/crowdosNWPU/CrowdOS>
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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * TemporalPoint Constraint
 *
 * @author loyx
 * @since 1.0.1
 */
public class TemporalPoint implements Constraint{
    private final long shift;
    private final LocalTime startTime;
    private final LocalTime endTime;

    /**
     * The TemporalPoint function takes a time point and a shift value as input,
     * and returns the startTime and endTime of the temporal window.
     *
     * @param timePoint Set the start time and end time of the temporal point object
     * @param shift Determine the start and end times of a temporal point
     */
    public TemporalPoint(String timePoint, long shift) {
        LocalTime point = LocalTime.parse(timePoint);
        this.shift = shift;
        this.startTime = point.minusMinutes(shift);
        this.endTime = point.plusMinutes(shift);
    }
    /**
     * The TemporalPoint function is a constructor for the TemporalPoint class.
     * It takes in two LocalTime objects, startTime and endTime, as well as a long shift.
     * The function then sets the instance variables of this object to be equal to these values.
     * If startTime is after endTime, an InvalidConstraintException will be thrown with an error message explaining
     * that this cannot happen.
     *
     * @param startTime startTime Set the start time of this temporal point
     * @param endTime endTime Set the end time of the temporal point
     * @param shift Shift the start and end times by a certain amount of time
     */
    private TemporalPoint(LocalTime startTime, LocalTime endTime, long shift) throws InvalidConstraintException {
        this.shift = shift;
        this.startTime = startTime;
        this.endTime = endTime;
        if (startTime.isAfter(endTime)){
            throw new InvalidConstraintException(String.format("startTime %s is after endTime %s", startTime, endTime));
        }
    }

    /**
     * The decomposer function is used to split a constraint into smaller constraints.
     * This is useful for two reasons:
     * 1) It allows the solver to solve problems that are too large for it to handle in one go.
     * 2) It allows the solver to use heuristics that require sub-constraints of a certain type, e.g., TemporalPoint constraints.
     *
     * @return A Decomposer
     *
     */
    @Override
    public Decomposer<Constraint> decomposer() {
        return new Decomposer<Constraint>() {
            @Override
            public List<Constraint> trivialDecompose() {
                try {
                    return Collections.singletonList(new TemporalPoint(startTime, endTime, shift));
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
                        subConstraints.add(new TemporalPoint(intervalStartTime, intervalEndTime, shift));
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
     * @return True if the condition is an instance of date and the local time of that date is equal to starttime or between starttime and endtime
     */
    @Override
    public boolean satisfy(Condition condition) {
        if (!(condition instanceof Date)) return false;
        Date date = (Date) condition;
        LocalTime localTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
        return localTime.equals(startTime) || (localTime.isAfter(startTime) && localTime.isBefore(endTime));
    }

    /**
     * get the condition class Object
     *
     * @return The class of the condition that is used to evaluate the rule
     *
     */
    @Override
    public Class<? extends Condition> getConditionClass() {
        return DateCondition.class;
    }

    /**
     * The description function returns a string that describes the object.
     *
     * @return A string representation of the object
     *
     */
    @Override
    public String description() {
        return toString();
    }

    /**
     * The toString function is used to print out the contents of a TemporalPoint object.
     *
     * @return A string object that contains the values of the shift, starttime and endtime variables
     *
     */
    @Override
    public String toString() {
        return "TemporalPoint{" +
                "shift=" + shift +
                ", start=" + startTime +
                ", end=" + endTime +
                '}';
    }
}
