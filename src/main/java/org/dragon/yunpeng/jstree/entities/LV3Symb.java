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
@Table(name = "LV3_SYMB")
public class LV3Symb {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;  // C1, C2 ...

	@Column(nullable = false)
	private String level;

	@Column(nullable = false)
	private String field1;

	@Column(nullable = false)
	private String field2;

	@Column(nullable = false)
	private String field3;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lv2_id", nullable = false)
	private LV2Symb lv2;

	@OneToMany(mappedBy = "lv3", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LV4Symb> children;

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

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public LV2Symb getLv2() {
		return lv2;
	}

	public void setLv2(LV2Symb lv2) {
		this.lv2 = lv2;
	}

	public List<LV4Symb> getChildren() {
		return children;
	}

	public void setChildren(List<LV4Symb> children) {
		this.children = children;
	}
}
