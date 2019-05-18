package org.CyfrSheets.models.data;

import org.CyfrSheets.models.Salt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SaltDao extends CrudRepository<Salt, Integer> {
}
