package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.system.SystemResourceHandler;

import java.util.LinkedList;

public class ParticipantPool extends LinkedList<Participant> implements Resource<ParticipantPool> {
    @Override
    public SystemResourceHandler<ParticipantPool> getHandler() {
        ParticipantPool participants = this;
        return new SystemResourceHandler<ParticipantPool>() {

            @Override
            public ParticipantPool getResourceView() {
                return participants;
            }

            @Override
            public ParticipantPool getResource() {
                return participants;
            }
        };
    }
}
