package org.dragon.yunpeng.jstree.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "LV2_SYMB")
public class LV2Symb {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name; // B1, B2 ...

	@Column(nullable = false)
	private String level;

	@Column(nullable = false)
	private String field1;

	@Column(nullable = false)
	private String field2;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lv1_id", nullable = false)
	private LV1Symb lv1;

	@OneToMany(mappedBy = "lv2", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LV3Symb> children;

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

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public LV1Symb getLv1() {
		return lv1;
	}

	public void setLv1(LV1Symb lv1) {
		this.lv1 = lv1;
	}

	public List<LV3Symb> getChildren() {
		return children;
	}

	public void setChildren(List<LV3Symb> children) {
		this.children = children;
	}
}
