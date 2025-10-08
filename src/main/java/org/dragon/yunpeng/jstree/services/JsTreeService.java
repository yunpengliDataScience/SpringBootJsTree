package org.dragon.yunpeng.jstree.services;

import org.dragon.yunpeng.jstree.Constants;
import org.dragon.yunpeng.jstree.dtos.JsTreeNode;
import org.dragon.yunpeng.jstree.dtos.JsTreeNode2;
import org.dragon.yunpeng.jstree.entities.ChangeRequestSandbox;
import org.dragon.yunpeng.jstree.entities.LV1Symb;
import org.dragon.yunpeng.jstree.entities.LV2Symb;
import org.dragon.yunpeng.jstree.entities.LV3Symb;
import org.dragon.yunpeng.jstree.entities.LV4Symb;
import org.dragon.yunpeng.jstree.repositories.ChangeRequestSandboxRepository;
import org.dragon.yunpeng.jstree.repositories.LV1SymbRepository;
import org.dragon.yunpeng.jstree.repositories.LV2SymbRepository;
import org.dragon.yunpeng.jstree.repositories.LV3SymbRepository;
import org.dragon.yunpeng.jstree.repositories.LV4SymbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class JsTreeService {

	@Autowired
	private LV1SymbRepository lv1Repo;

	@Autowired
	private LV2SymbRepository lv2Repo;

	@Autowired
	private LV3SymbRepository lv3Repo;

	@Autowired
	private LV4SymbRepository lv4Repo;

	@Autowired
	private ChangeRequestSandboxRepository changeRequestSandboxRepo;

	public List<JsTreeNode> getJsTreeDataHierarchicalFormat() {
		List<JsTreeNode> roots = new ArrayList<>();
		List<LV1Symb> lv1List = lv1Repo.findAll();

		for (LV1Symb lv1 : lv1List) {
			JsTreeNode node = new JsTreeNode();
			node.setId("LV1-" + lv1.getId());
			node.setText(lv1.getName());

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("databaseId", lv1.getId());
			data.put("name", lv1.getName());
			data.put("level", lv1.getLevel());
			data.put("field1", lv1.getField1());
			node.setData(data);

			node.setChildren(getLv2Children(lv1.getId()));
			roots.add(node);
		}
		return roots;
	}

	private List<JsTreeNode> getLv2Children(Long lv1Id) {
		List<JsTreeNode> children = new ArrayList<>();
		List<LV2Symb> lv2List = lv2Repo.findByLv1Id(lv1Id);

		for (LV2Symb lv2 : lv2List) {
			JsTreeNode node = new JsTreeNode();
			node.setId("LV2-" + lv2.getId());
			node.setText(lv2.getName());

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("databaseId", lv2.getId());
			data.put("name", lv2.getName());
			data.put("level", lv2.getLevel());
			data.put("field1", lv2.getField1());
			data.put("field2", lv2.getField2());
			node.setData(data);

			node.setChildren(getLv3Children(lv2.getId()));
			children.add(node);
		}
		return children;
	}

	private List<JsTreeNode> getLv3Children(Long lv2Id) {
		List<JsTreeNode> children = new ArrayList<>();
		List<LV3Symb> lv3List = lv3Repo.findByLv2Id(lv2Id);

		for (LV3Symb lv3 : lv3List) {
			JsTreeNode node = new JsTreeNode();
			node.setId("LV3-" + lv3.getId());
			node.setText(lv3.getName());

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("databaseId", lv3.getId());
			data.put("name", lv3.getName());
			data.put("level", lv3.getLevel());
			data.put("field1", lv3.getField1());
			data.put("field2", lv3.getField2());
			data.put("field3", lv3.getField3());
			node.setData(data);

			node.setChildren(getLv4Children(lv3.getId()));
			children.add(node);
		}
		return children;
	}

	private List<JsTreeNode> getLv4Children(Long lv3Id) {
		List<JsTreeNode> children = new ArrayList<>();
		List<LV4Symb> lv4List = lv4Repo.findByLv3Id(lv3Id);

		for (LV4Symb lv4 : lv4List) {
			JsTreeNode node = new JsTreeNode();
			node.setId("LV4-" + lv4.getId());
			node.setText(lv4.getName());

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("databaseId", lv4.getId());
			data.put("name", lv4.getName());
			data.put("level", lv4.getLevel());
			data.put("field1", lv4.getField1());
			data.put("field2", lv4.getField2());
			data.put("field3", lv4.getField3());
			data.put("field4", lv4.getField4());
			node.setData(data);

			node.setChildren(new ArrayList<>()); // no children
			children.add(node);
		}
		return children;
	}

	public List<JsTreeNode2> getRootNodesFlatFormat() {
		List<JsTreeNode2> nodeList = new ArrayList<>();

		// Level 1
		List<LV1Symb> lv1List = lv1Repo.findAll();
		for (LV1Symb lv1 : lv1List) {
			JsTreeNode2 node = new JsTreeNode2();
			node.setId("LV1-" + lv1.getId());

			node.setParent("#");
			node.setText(lv1.getName());

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("databaseId", lv1.getId());
			data.put("name", lv1.getName());
			data.put("level", lv1.getLevel());
			data.put("field1", lv1.getField1());

			node.setData(data);
			node.setChildren(true);

			nodeList.add(node);
		}

		return nodeList;
	}

	public List<JsTreeNode2> getChildNodesFlatFormat(String parentLevel, String parentNodeId, String parentDatabaseId) {
		List<JsTreeNode2> nodeList = new ArrayList<>();

		if (!"".equals(parentDatabaseId) && !(parentDatabaseId == null)) {

			long parentDBId = Long.parseLong(parentDatabaseId);

			if ("LV1".equals(parentLevel)) {

				// Level 2
				List<LV2Symb> lv2List = lv2Repo.findByLv1Id(parentDBId);
				for (LV2Symb lv2 : lv2List) {
					JsTreeNode2 node = new JsTreeNode2();
					node.setId("LV2-" + lv2.getId());

					node.setParent(parentNodeId);
					node.setText(lv2.getName());

					Map<String, Object> data = new HashMap<String, Object>();

					data.put("databaseId", lv2.getId());
					data.put("name", lv2.getName());
					data.put("level", lv2.getLevel());
					data.put("field1", lv2.getField1());
					data.put("field2", lv2.getField2());

					node.setData(data);
					node.setChildren(true);

					nodeList.add(node);
				}
			} else if ("LV2".equals(parentLevel)) {
				// Level 3
				List<LV3Symb> lv3List = lv3Repo.findByLv2Id(parentDBId);
				for (LV3Symb lv3 : lv3List) {
					JsTreeNode2 node = new JsTreeNode2();
					node.setId("LV3-" + lv3.getId());

					node.setParent(parentNodeId);
					node.setText(lv3.getName());

					Map<String, Object> data = new HashMap<String, Object>();

					data.put("databaseId", lv3.getId());
					data.put("name", lv3.getName());
					data.put("level", lv3.getLevel());
					data.put("field1", lv3.getField1());
					data.put("field2", lv3.getField2());
					data.put("field3", lv3.getField3());

					node.setData(data);
					node.setChildren(true);

					nodeList.add(node);
				}
			} else if ("LV3".equals(parentLevel)) {
				// Level 4
				List<LV4Symb> lv4List = lv4Repo.findByLv3Id(parentDBId);
				for (LV4Symb lv4 : lv4List) {
					JsTreeNode2 node = new JsTreeNode2();
					node.setId("LV4-" + lv4.getId());

					node.setParent(parentNodeId);
					node.setText(lv4.getName());

					Map<String, Object> data = new HashMap<String, Object>();

					data.put("databaseId", lv4.getId());
					data.put("name", lv4.getName());
					data.put("level", lv4.getLevel());
					data.put("field1", lv4.getField1());
					data.put("field2", lv4.getField2());
					data.put("field3", lv4.getField3());
					data.put("field4", lv4.getField4());

					node.setData(data);
					node.setChildren(true);

					nodeList.add(node);
				}
			}
		}

		return nodeList;
	}

	// Get Tree data in flat Json format.
	public List<JsTreeNode2> getJsTreeDataFlatFormat() {
		List<JsTreeNode2> dataList = new ArrayList<>();

		// Level 1
		List<LV1Symb> lv1List = lv1Repo.findAll();
		for (LV1Symb lv1 : lv1List) {
			JsTreeNode2 node = new JsTreeNode2();
			node.setId("LV1-" + lv1.getId());

			node.setParent("#");
			node.setText(lv1.getName());

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("databaseId", lv1.getId());
			data.put("name", lv1.getName());
			data.put("level", lv1.getLevel());
			data.put("field1", lv1.getField1());
			node.setData(data);

			dataList.add(node);
		}

		// Level 2
		List<LV2Symb> lv2List = lv2Repo.findAll();
		for (LV2Symb lv2 : lv2List) {
			JsTreeNode2 node = new JsTreeNode2();
			node.setId("LV2-" + lv2.getId());

			node.setParent("LV1-" + lv2.getLv1().getId());
			node.setText(lv2.getName());

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("databaseId", lv2.getId());
			data.put("name", lv2.getName());
			data.put("level", lv2.getLevel());
			data.put("field1", lv2.getField1());
			data.put("field2", lv2.getField2());
			node.setData(data);

			dataList.add(node);
		}

		// Level 3
		List<LV3Symb> lv3List = lv3Repo.findAll();
		for (LV3Symb lv3 : lv3List) {
			JsTreeNode2 node = new JsTreeNode2();
			node.setId("LV3-" + lv3.getId());

			node.setParent("LV2-" + lv3.getLv2().getId());
			node.setText(lv3.getName());

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("databaseId", lv3.getId());
			data.put("name", lv3.getName());
			data.put("level", lv3.getLevel());
			data.put("field1", lv3.getField1());
			data.put("field2", lv3.getField2());
			data.put("field3", lv3.getField3());
			node.setData(data);

			dataList.add(node);
		}

		// Level 4
		List<LV4Symb> lv4List = lv4Repo.findAll();
		for (LV4Symb lv4 : lv4List) {
			JsTreeNode2 node = new JsTreeNode2();
			node.setId("LV4-" + lv4.getId());

			node.setParent("LV3-" + lv4.getLv3().getId());
			node.setText(lv4.getName());

			Map<String, Object> data = new HashMap<String, Object>();

			data.put("databaseId", lv4.getId());
			data.put("name", lv4.getName());
			data.put("level", lv4.getLevel());
			data.put("field1", lv4.getField1());
			data.put("field2", lv4.getField2());
			data.put("field3", lv4.getField3());
			data.put("field4", lv4.getField4());
			node.setData(data);

			dataList.add(node);
		}

		return dataList;
	}

	// Get Json tree from CHANGE_REQUEST_SANDBOX table or construct Json from symbol
	// tables.
	public String getJsTreeDataForChangeRequest() throws JsonProcessingException {
		String jsonString = null;

		ChangeRequestSandbox changeRequestSandbox = changeRequestSandboxRepo
				.getChangeRequestByNotEqualStatus(Constants.CR_APPROVED);

		if (changeRequestSandbox != null) {
			System.out.println("Json is retrieved from CHANGE_REQUEST_SANDBOX table.");

			jsonString = changeRequestSandbox.getJsonContent();
		} else {
			System.out.println("Json is constructed from database table.");

			List<JsTreeNode2> nodes = getJsTreeDataFlatFormat();

			// Convert List -> JSON string
			// ObjectMapper objectMapper = new ObjectMapper();
			// jsonString = objectMapper.writeValueAsString(nodes);

			// Use Gson instead of ObjectMapper
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			jsonString = gson.toJson(nodes);
		}

		System.out.println(jsonString);

		return jsonString;
	}

	// Retrieve the summarized change Json
	public String getSummarizedJsTreeDataForChangeRequest() throws JsonProcessingException {
		String jsonString = "[{}]";

		ChangeRequestSandbox changeRequestSandbox = changeRequestSandboxRepo
				.getChangeRequestByNotEqualStatus(Constants.CR_APPROVED);

		if (changeRequestSandbox != null) {
			System.out.println("Json is retrieved from CHANGE_REQUEST_SANDBOX table.");

			jsonString = changeRequestSandbox.getSummarizedJsonContent();
		}

		System.out.println(jsonString);

		return jsonString;
	}

	// Construct a summarized Json that only contains the modified nodes and their
	// ancestors.
	public String filterModifiedWithAncestorsFlat(String json) {

		JsonArray flatNodes = JsonParser.parseString(json).getAsJsonArray();

		Map<String, JsonObject> nodeMap = new HashMap<>();
		Set<String> includedIds = new LinkedHashSet<>();

		// Build a lookup map by id
		for (JsonElement el : flatNodes) {
			JsonObject node = el.getAsJsonObject();
			nodeMap.put(node.get("id").getAsString(), node);
		}

		// For each node that is Modified, add ancestors
		for (JsonElement el : flatNodes) {
			JsonObject node = el.getAsJsonObject();

			String status = null;
			if (node.has("data")) {
				JsonObject data = node.getAsJsonObject("data");
				if (data.has("status")) {
					status = data.get("status").getAsString();
				}
			}

			// String status = node.has("status") ? node.get("status").getAsString() : "";
			if ("Modified".equalsIgnoreCase(status)) {
				String currentId = node.get("id").getAsString();
				while (currentId != null && !currentId.equals("#")) {
					if (!includedIds.contains(currentId)) {
						includedIds.add(currentId);
					}
					JsonObject current = nodeMap.get(currentId);
					if (current == null)
						break;
					String parent = current.has("parent") ? current.get("parent").getAsString() : null;
					if (parent == null || parent.equals("#"))
						break;
					currentId = parent;
				}
			}
		}

		// Collect selected nodes preserving order
		JsonArray filtered = new JsonArray();
		for (JsonElement el : flatNodes) {
			JsonObject node = el.getAsJsonObject();
			if (includedIds.contains(node.get("id").getAsString())) {
				filtered.add(node);
			}
		}

		GsonBuilder gsonBuilder = new GsonBuilder();

		String filterdJson = gsonBuilder.setPrettyPrinting().create().toJson(filtered);

		return filterdJson;
	}
}
