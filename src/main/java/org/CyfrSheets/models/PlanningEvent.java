package org.CyfrSheets.models;

import javax.persistence.Entity;

@Entity
public class PlanningEvent extends SEvent {

    private EventType type;
    private EventTime time;



}
