package org.dragon.yunpeng.jstree.dtos;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsTreeNode2 {
	@JsonProperty("id")
	private String id;

	@JsonProperty("text")
	private String text;

	@JsonProperty("parent")
	private String parent;

	@JsonProperty("data")
	private Map<String, Object> data;
	
	@JsonProperty("children")
	private Boolean children;  // add this

	// Constructors
	public JsTreeNode2() {
	}

    public JsTreeNode2(String id, String text, String parent, Map<String, Object> data, Boolean children) {
        this.id = id;
        this.text = text;
        this.parent = parent;
        this.data = data;
        this.children = children;
    }
    
	public JsTreeNode2(String id, String text, String parent, Map<String, Object> data) {
		this.id = id;
		this.text = text;
		this.parent = parent;
		this.data = data;
	}

	// Getters & setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Boolean getChildren() {
		return children;
	}

	public void setChildren(Boolean children) {
		this.children = children;
	}
}
