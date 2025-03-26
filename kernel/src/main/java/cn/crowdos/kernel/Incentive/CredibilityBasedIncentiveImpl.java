/*
 * Copyright 2019-2025 CrowdOS Group, Northwestern Polytechnical University. <https://github.com/crowdosNWPU/CrowdOS>
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
package cn.crowdos.kernel.Incentive;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.Task;
import cn.crowdos.kernel.system.SystemResourceHandler;

import java.util.*;

/**
 * CredibilityBasedIncentiveImpl is a concrete implementation of a credibility-based incentive.
 * It implements the CredibilityBasedIncentive interface.
 *
 * @author yuzy
 * @since 1.0.3
 */
public class CredibilityBasedIncentiveImpl implements CredibilityBasedIncentive {

    private final Map<Participant, Integer> trustValues; // 工作者和信誉度的映射
    private final List<Participant> trustedParticipants; // 受信任参与者集合
    private final Map<Participant, Double> rewardValues; // 工作者和报酬的映射

    /**
     * The CredibilityBasedIncentiveImpl function is a constructor for the CredibilityBasedIncentiveImpl class.
     * The function creating new instances of HashMap and ArrayList to be used by this object.
     */
    public CredibilityBasedIncentiveImpl() {
        rewardValues = new HashMap<>();
        trustValues = new HashMap<>();
        trustedParticipants = new ArrayList<>();
    }

    /**
     * The addCredibility function add one to the credibility of the participant in trustValues.
     *
     * @param participant Determine which participant is being updated
     *
     */
    @Override
    public void addCredibility(Participant participant) {
        int trustValue = trustValues.getOrDefault(participant, 0) + 1;
        trustValues.put(participant, trustValue);
    }

    /**
     * The penalizeCredibility function reduce one to the credibility of the participant in trustValues.
     *
     * @param participant Determine which participant is being updated
     *
     */
    @Override
    public void penalizeCredibility(Participant participant) {
        int trustValue = trustValues.getOrDefault(participant, 0) - 1;
        trustValues.put(participant, trustValue);
    }

    /**
     * The getCredibility function query the credibility value of the specified participant in trustValues.
     *
     * @param participant Determine which participant is being updated
     *
     * @return One credibility value or zero
     */
    @Override
        public int getCredibility(Participant participant) {
        return trustValues.getOrDefault(participant, 0);
    }

    /**
     * The allocateRewards function This method distributes rewards for task completer with others in the chain of trust.
     *
     * @param rewards The total remuneration for this task
     * @param task Determine which task is being performed
     * @param firstParticipant Determine who the first finisher of the task is being
     * @param participants Determine who is in the chain of trusts
     *
     */
    @Override
    public void allocateRewards(double rewards, Task task, Participant firstParticipant, List<Participant> participants) {
        // 假设奖励分配规则为：任务完成者获得80%的奖励，信任链中的参与者共享20%的奖励
        //该第一个完成的工作者得到的报酬
        double firstTrust = getCredibility(firstParticipant);
        double firstReward = (rewards * 0.8) + ((firstTrust / 10) * 8);
        rewardValues.put(firstParticipant,firstReward);
        double trustReward = (rewards-firstReward)* 0.2 / participants.size();
        for (Participant participant : participants) {
            if( participant.equals(firstParticipant) ){
                break;
            }else {
                rewardValues.put(participant,isTrustedParticipant(participant) ? trustReward : 0);
            }
        }
    }

    /**
     * The IncentiveAssignment function returns the incentive scheme assigned to the task.
     *
     * @return An incentive scheme
     */
    public Map<Participant, Double> IncentiveAssignment() {
        return rewardValues;
    }

    /**
     * The removeTrustedParticipant function removes a participant from the trust chain.
     *
     * @param participant Determine which participant is being updated
     *
     */
    @Override
    public void removeTrustedParticipant(Participant participant) {
        for (Map.Entry<Participant, Integer> entry : trustValues.entrySet()) {
            if (entry.getValue() < 0) {
                trustedParticipants.remove(participant);
            }
        }
    }

    /**
     * The isTrustedParticipant function determines whether a particular participant is in the trust chain or not.
     *
     * @param participant Determine which participant is being judged
     *
     * @return A judgment result
     */
    @Override
    public boolean isTrustedParticipant(Participant participant) {
        return trustedParticipants.contains(participant);
    }

    /**
     * The getTrustedParticipants function returns a list of participants who in the trust chain.
     *
     * @return A list of participants
     */
    @Override
    public List<Participant> getTrustedParticipants() {
        return trustedParticipants;
    }

    @Override
    // A method that returns a CredibilityBasedIncentiveImpl object.
    public SystemResourceHandler<CredibilityBasedIncentive> getHandler() {
        CredibilityBasedIncentiveImpl credibilityBasedIncentiveImpl = this;
        return new SystemResourceHandler<CredibilityBasedIncentive>() {
            @Override
            public CredibilityBasedIncentive getResourceView() {
                return credibilityBasedIncentiveImpl;
            }

            @Override
            public CredibilityBasedIncentive getResource() {
                return credibilityBasedIncentiveImpl;
            }
        };
    }
}


