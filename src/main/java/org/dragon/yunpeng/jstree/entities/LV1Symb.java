package org.dragon.yunpeng.jstree.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "LV1_SYMB")
public class LV1Symb {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name; // A1, A2 ...

	@Column(nullable = false)
	private String level;

	@Column(nullable = false)
	private String field1; 

	@OneToMany(mappedBy = "lv1", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LV2Symb> children;

	// Getters & Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public List<LV2Symb> getChildren() {
		return children;
	}

	public void setChildren(List<LV2Symb> children) {
		this.children = children;
	}
}
