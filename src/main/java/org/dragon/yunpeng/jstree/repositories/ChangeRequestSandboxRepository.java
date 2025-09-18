package org.dragon.yunpeng.jstree.repositories;

import org.dragon.yunpeng.jstree.entities.ChangeRequestSandbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChangeRequestSandboxRepository extends JpaRepository<ChangeRequestSandbox, Long>{
	
	
	 @Query("SELECT crs FROM ChangeRequestSandbox crs WHERE crs.status <> :status")
	 ChangeRequestSandbox getChangeRequestByNotEqualStatus(@Param("status") String status);
}
