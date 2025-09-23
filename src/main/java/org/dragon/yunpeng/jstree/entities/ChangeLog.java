package org.dragon.yunpeng.jstree.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "CHANGE_LOG")
public class ChangeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "change_request_id", nullable = false)
    private Long changeRequestId; // FK to ChangeRequestSandbox.id (if applicable)

    @Column(name = "table_name", nullable = false, length = 255)
    private String tableName;

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "operation_type", nullable = false, length = 50)
    private String operationType; // INSERT, UPDATE, DELETE

    @Column(name = "column_name", length = 255)
    private String columnName;

    @Column(name = "old_value", length = 2000)
    private String oldValue;

    @Column(name = "new_value", length = 2000)
    private String newValue;

    @Column(name = "changed_by", length = 255)
    private String changedBy;

    @Column(name = "changed_time")
    private LocalDateTime changedTime;

    @PrePersist
    public void onCreate() {
        if (changedTime == null) {
            changedTime = LocalDateTime.now();
        }
    }

    // ===== Getters and Setters =====
    public Long getId() {
        return id;
    }

    public Long getChangeRequestId() {
        return changeRequestId;
    }

    public void setChangeRequestId(Long changeRequestId) {
        this.changeRequestId = changeRequestId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

	public LocalDateTime getChangedTime() {
		return changedTime;
	}

	public void setChangedTime(LocalDateTime changedTime) {
		this.changedTime = changedTime;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
}
