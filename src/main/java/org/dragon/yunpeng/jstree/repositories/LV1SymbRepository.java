package org.dragon.yunpeng.jstree.repositories;

import org.dragon.yunpeng.jstree.entities.LV1Symb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LV1SymbRepository extends JpaRepository<LV1Symb, Long> {
	// No parent for LV1, basic CRUD is enough
}
