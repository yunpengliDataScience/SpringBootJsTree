package org.dragon.yunpeng.jstree.repositories;

import org.dragon.yunpeng.jstree.entities.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {

}
