package org.CyfrSheets.models;

import org.CyfrSheets.models.EventTime;
import org.CyfrSheets.models.EventType;
import org.CyfrSheets.models.Participant;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
abstract class EventInterface {

    EventType getType();

    EventTime getTime();

    String getDesc();

    ArrayList<Participant> getParticipants();

}