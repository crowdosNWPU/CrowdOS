package cn.crowdos.kernel.resource;

import cn.crowdos.kernel.constraint.Condition;

public interface Participant {

    // Defining a new type called `ParticipantStatus` with three possible values: `AVAILABLE`, `BUSY`, and `DISABLED`.
    enum ParticipantStatus {
        AVAILABLE,
        BUSY,
        DISABLED,
    }

    /**
     * Returns the status of the participant
     *
     * @return The status of the participant.
     */
    ParticipantStatus getStatus();

    /**
     * Sets the status of the participant
     *
     * @param status The status of the participant.
     */
    void setStatus(ParticipantStatus status);

    /**
     * Returns true if the given condition is present in the list of conditions.
     *
     * @param conditionClass The class of the condition to check for.
     * @return A boolean value.
     */
    boolean hasAbility(Class<? extends Condition> conditionClass);

    /**
     * Get the ability of the given class.
     *
     * @param conditionClass The class of the condition you want to get.
     * @return A condition object.
     */
    Condition getAbility(Class<? extends Condition> conditionClass);

    /**
     * Returns true if the next token is a number.
     *
     * @return A boolean value.
     */
    boolean available();

}
