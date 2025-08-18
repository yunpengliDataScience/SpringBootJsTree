package org.dragon.yunpeng.jstree.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "JSON_AUDIT_TRAIL")
public class JsonAuditTrail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Store full JSON as CLOB
	@Lob
	@Column(columnDefinition = "CLOB")
	private String jsonContent;

	private String action; // e.g. "CREATE", "UPDATE", "DELETE", "SAVE_TREE"

	private String userName; // optional: who performed the action

	private LocalDateTime timestamp;

	@PrePersist
	public void onCreate() {
		timestamp = LocalDateTime.now();
	}

	// getters and setters
	public Long getId() {
		return id;
	}

	public String getJsonContent() {
		return jsonContent;
	}

	public void setJsonContent(String jsonContent) {
		this.jsonContent = jsonContent;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
