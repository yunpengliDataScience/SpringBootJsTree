package org.dragon.yunpeng.jstree.repositories;

import java.util.List;

import org.dragon.yunpeng.jstree.entities.LV2Symb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LV2SymbRepository extends JpaRepository<LV2Symb, Long> {

	// Fetch LV2 nodes by LV1 parent ID
	List<LV2Symb> findByLv1Id(Long lv1Id);
}
