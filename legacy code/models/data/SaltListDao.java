package org.CyfrSheets.legacy.models.data;

import org.CyfrSheets.legacy.models.SaltList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SaltListDao extends CrudRepository<SaltList, Integer> {
}
