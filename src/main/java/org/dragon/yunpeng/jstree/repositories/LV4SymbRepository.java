package org.dragon.yunpeng.jstree.repositories;

import java.util.List;

import org.dragon.yunpeng.jstree.entities.LV4Symb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LV4SymbRepository extends JpaRepository<LV4Symb, Long> {
	// Fetch LV4 nodes by LV3 parent ID
	List<LV4Symb> findByLv3Id(Long lv3Id);
}
