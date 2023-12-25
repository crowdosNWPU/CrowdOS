package cn.crowdos.kernel.Incentive;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.resource.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>This interface defines an incentive-based approach to trustworthiness.
 * It extends the {@code Resource<CredibilityBasedIncentive>} interface and defines the following methods:</p>
 *
 * <ul>
 *   <li>IncentiveAssignment(): returns a Map containing the rewards received by each participant.</li>
 *   <li>earnTrust(Participant participant): increase credibility of participant.</li>
 *   <li>penalizeTrust(Participant participant): reduced credibility of participant.</li>
 *   <li>getTrust(Participant participant): returns the credibility of the participant.</li>
 *   <li>{@code allocateRewards(double rewards, Task task, Participant firstParticipant, List<Participant> participants)}: allocates rewards to the participants.</li>
 *   <li>removeTrustedParticipant(Participant participant): removes the participant from the trusted chain.</li>
 *   <li>isTrustedParticipant(Participant participant): determines whether the participant is trusted.</li>
 *   <li>getTrustedParticipants(): returns all participants in the trusted chain.</li>
 * </ul>
 *
 * <p>The interface is mainly used to motivate participants to complete specific tasks efficiently and to
 * distribute rewards by reputation, with a little reward to other participants in the trust chain.</p>
 *
 * @author yuzy
 * @since 1.0.3
 */
public interface CredibilityBasedIncentive extends Resource<CredibilityBasedIncentive> {


    Map<Participant, Double> IncentiveAssignment();

    // 信任值计算
    void addCredibility(Participant participant);
    void penalizeCredibility(Participant participant);
    int getCredibility(Participant participant);

    // 奖励分配
    void allocateRewards(double rewards, Task task, Participant firstParticipant, List<Participant> participants);

    // 信任链
    void removeTrustedParticipant(Participant participant);
    boolean isTrustedParticipant(Participant participant);
    List<Participant> getTrustedParticipants();
}
