package org.dragon.yunpeng.jstree.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "LV4_SYMB")
public class LV4Symb {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;  // D1, D2 ...

	@Column(nullable = false)
	private String level;

	@Column(nullable = false)
	private String field1;

	@Column(nullable = false)
	private String field2;

	@Column(nullable = false)
	private String field3;

	@Column(nullable = false)
	private String field4;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lv3_id", nullable = false)
	private LV3Symb lv3;

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

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public LV3Symb getLv3() {
		return lv3;
	}

	public void setLv3(LV3Symb lv3) {
		this.lv3 = lv3;
	}
}
