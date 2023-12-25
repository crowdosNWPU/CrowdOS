package cn.crowdos.kernel.system.resource;

import cn.crowdos.kernel.resource.Participant;
import cn.crowdos.kernel.resource.SimpleParticipant;
import cn.crowdos.kernel.resource.SimpleTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MissionTest {

    Mission mission;
    private List<Participant> participants;
    private SimpleTask simpleTask;

    @BeforeEach
    void setUp() {
        simpleTask = new SimpleTask(null, null);
        participants = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            participants.add(new SimpleParticipant());
        }
        mission = new Mission(simpleTask, participants);
    }

    @Test
    void getFirstSubmitParticipant() throws MissionUpdateException {
        assertNull(mission.getFirstSubmitParticipant());
        mission.updateSubmit(participants.get(1));
        assertNotNull(mission.getFirstSubmitParticipant());
        assertEquals(mission.getFirstSubmitParticipant(), participants.get(1));
    }

    @Test
    void getParticipants() {
        System.out.println(mission.getParticipants());
    }

    @Test
    void belongTo() {
        SimpleTask task2 = new SimpleTask(null, null);
        assertTrue(mission.belongTo(simpleTask));
        assertFalse(mission.belongTo(task2));
    }

    @Test
    void updateSubmitError() {
        SimpleParticipant p = new SimpleParticipant();
        assertThrows(MissionUpdateException.class, ()->mission.updateSubmit(p));
    }

    @Test
    void updateSubmit() throws MissionUpdateException, ParseException {
        mission.updateSubmit(participants.get(0));
        assertEquals(mission.getFirstSubmitParticipant(), participants.get(0));
        SimpleDateFormat sf = new SimpleDateFormat("YY");
        mission.updateSubmit(participants.get(1), sf.parse("1999"));
        assertEquals(mission.getFirstSubmitParticipant(), participants.get(1));
    }

    @Test
    void involved() {
        for (Participant participant : participants) {
            assertTrue(mission.involved(participant));
        }
        SimpleParticipant p2 = new SimpleParticipant();
        assertFalse(mission.involved(p2));

    }
}