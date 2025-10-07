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
@Table(name = "CHANGE_REQUEST_SANDBOX")
public class ChangeRequestSandbox {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Store full JSON as CLOB
	@Lob
	@Column(columnDefinition = "CLOB")
	private String jsonContent;

	@Lob
	@Column(columnDefinition = "CLOB")
	private String summarizedJsonContent;

	private String userName; // optional: who performed the action

	private LocalDateTime timestamp;

	private String status; // TODO

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSummarizedJsonContent() {
		return summarizedJsonContent;
	}

	public void setSummarizedJsonContent(String summarizedJsonContent) {
		this.summarizedJsonContent = summarizedJsonContent;
	}

}
