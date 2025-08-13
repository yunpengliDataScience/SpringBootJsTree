package org.dragon.yunpeng.jstree.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY) // skip nulls
public class JsTreeNode {
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("text")
	private String text;
	
	@JsonProperty("data")
	private Map<String, Object> data;
	
	@JsonProperty("children")
	private List<JsTreeNode> children = new ArrayList<>();

	// Constructors
	public JsTreeNode() {
	}

	public JsTreeNode(String id, String text, Map<String, Object> data) {
		this.id = id;
		this.text = text;
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

	public List<JsTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<JsTreeNode> children) {
		this.children = children;
	}
}
