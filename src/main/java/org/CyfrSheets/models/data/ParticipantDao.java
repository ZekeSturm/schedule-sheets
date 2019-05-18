package org.CyfrSheets.models.data;

import org.CyfrSheets.models.Participant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ParticipantDao extends CrudRepository<Participant, Integer> {
}
