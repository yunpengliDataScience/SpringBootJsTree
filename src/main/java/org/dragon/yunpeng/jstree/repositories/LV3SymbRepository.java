package org.dragon.yunpeng.jstree.repositories;

import java.util.List;

import org.dragon.yunpeng.jstree.entities.LV3Symb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LV3SymbRepository extends JpaRepository<LV3Symb, Long> {

	// Fetch LV3 nodes by LV2 parent ID
	List<LV3Symb> findByLv2Id(Long lv2Id);
}
