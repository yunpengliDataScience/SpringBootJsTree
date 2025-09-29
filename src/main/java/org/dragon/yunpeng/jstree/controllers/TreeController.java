package org.dragon.yunpeng.jstree.controllers;

import org.dragon.yunpeng.jstree.Constants;
import org.dragon.yunpeng.jstree.dtos.JsTreeNode;
import org.dragon.yunpeng.jstree.dtos.JsTreeNode2;
import org.dragon.yunpeng.jstree.entities.ChangeRequestSandbox;
import org.dragon.yunpeng.jstree.services.JsTreeSaveService;
import org.dragon.yunpeng.jstree.services.JsTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tree")
public class TreeController {

	// private static final Path TREE_FILE = Paths.get("tree-data.json");

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private JsTreeService jsTreeService;

	@Autowired
	private JsTreeSaveService jsTreeSaveService;

	@PostMapping("/saveTree/{fileName}")
	public ResponseEntity<String> saveTree(@RequestBody Object jsonTree, @PathVariable String fileName) {

		Path TREE_FILE = Paths.get(fileName);

		try {
			String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonTree);

			Files.write(TREE_FILE, prettyJson.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);

			return ResponseEntity.ok("Pretty JSON tree saved.");
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Error: " + e.getMessage());
		}
	}

	@PostMapping("/saveTreeToDatabase")
	public ResponseEntity<String> saveTreeToDatabase(@RequestBody Object jsonTree) {

		ObjectMapper mapper = new ObjectMapper();

		try {
			String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonTree);

			jsTreeSaveService.saveAudit(prettyJson, "", "SYS_USER");

			List<JsTreeNode> nodes = mapper.readValue(prettyJson, new TypeReference<List<JsTreeNode>>() {
			});

			System.out.println(prettyJson);

			jsTreeSaveService.saveModifiedNodesHierarchicalFormat(nodes);

			return ResponseEntity.ok("Pretty JSON tree saved.");
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Error: " + e.getMessage());
		}
	}

	// Alternative JSON format (flat)
	@PostMapping("/saveTreeToDatabase2")
	public ResponseEntity<String> saveTreeToDatabase2(@RequestBody Object jsonTree) {

		ObjectMapper mapper = new ObjectMapper();

		System.out.println("jsonTree to save:");
		System.out.println(jsonTree);

		try {
			String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonTree);

			jsTreeSaveService.saveAudit(prettyJson, "", "SYS_USER");

			List<JsTreeNode2> nodes = mapper.readValue(prettyJson, new TypeReference<List<JsTreeNode2>>() {
			});

			System.out.println("Flat JSON to save:");
			System.out.println(prettyJson);

			jsTreeSaveService.saveModifiedNodesFlatFormat(nodes);

			return ResponseEntity.ok("Pretty JSON tree saved.");
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Error: " + e.getMessage());
		}
	}

	// 1. Save the flat JSON and approve the change request.
	// 2. Save data into symbol tables.
	@PostMapping("/saveAndApproveChangeRequest")
	public ResponseEntity<String> saveAndApproveChangeRequest(@RequestBody Object jsonTree) {

		System.out.println("jsonTree to save:");
		System.out.println(jsonTree);

		try {
			String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonTree);

			System.out.println("Flat JSON to save:");
			System.out.println(prettyJson);

			// Mark change request as Approved.
			ChangeRequestSandbox changeRequest = jsTreeSaveService.saveChangeRequest(prettyJson, Constants.CR_APPROVED,
					"SYS_USER");

			ObjectMapper mapper = new ObjectMapper();
			List<JsTreeNode2> nodes = mapper.readValue(prettyJson, new TypeReference<List<JsTreeNode2>>() {
			});

			// TODO: combine jsTreeSaveService.saveChangeRequest() in one transaction.
			// Save data into real symbol tables.
			jsTreeSaveService.saveModifiedNodesFlatFormat(nodes, changeRequest.getId()); // TODO

			return ResponseEntity.ok("Pretty JSON tree saved.");
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Error: " + e.getMessage());
		}
	}

	// Alternative JSON format (flat)
	@PostMapping("/saveDraftChangeRequest")
	public ResponseEntity<String> saveDraftChangeRequest(@RequestBody Object jsonTree) {

		System.out.println("jsonTree to save:");
		System.out.println(jsonTree);

		try {

			// String prettyJson =
			// objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonTree);

			// Use Gson instead of Jackson ObjectMapper
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String prettyJson = gson.toJson(jsonTree);

			jsTreeSaveService.saveChangeRequest(prettyJson, Constants.CR_DRAFT, "SYS_USER");

			System.out.println("Flat JSON to save:");
			System.out.println(prettyJson);

			return ResponseEntity.ok("Pretty JSON tree saved.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error: " + e.getMessage());
		}
	}

	@GetMapping("/loadTree/{fileName}")
	public List<Map<String, Object>> loadTreeData(@PathVariable String fileName) throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		File file = new File(fileName); // relative path
		if (!file.exists()) {
			throw new RuntimeException("JSON file not found at " + file.getAbsolutePath());
		}
		return mapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {
		});
	}

	@GetMapping("/getJsTreeDataHierarchicalFormat")
	public ResponseEntity<List<JsTreeNode>> getJsTreeDataHierarchicalFormat() {
		List<JsTreeNode> nodes = jsTreeService.getJsTreeDataHierarchicalFormat();

		return ResponseEntity.ok(nodes); // Spring automatically converts to proper JSON
	}

	@GetMapping("/getJsTreeDataFlatFormat")
	public ResponseEntity<List<JsTreeNode2>> getJsTreeDataFlatFormat() {

		System.out.println("Retrieving data through /getJsTreeDataFlatFormat");
		List<JsTreeNode2> nodes = jsTreeService.getJsTreeDataFlatFormat();

		return ResponseEntity.ok(nodes); // Spring automatically converts to proper JSON
	}

	@GetMapping("/getJsTreeDataForChangeRequest")
	public ResponseEntity<String> getJsTreeDataForChangeRequest() {
		try {
			System.out.println("Retrieving data through /getJsTreeDataForChangeRequest");

			String jsonString = jsTreeService.getJsTreeDataForChangeRequest();

			// Return raw string, but flagged as JSON; otherwise, the JsTree is unable to
			// consume the raw string.
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonString);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON)
					.body("{\"error\":\"Failed to convert JsTreeNode2 list to JSON\"}");
		}
	}

	// --------------------------------------------------------------
	private Map<String, Object> node(String id, String parent, String text, boolean hasChildren) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("parent", parent);
		map.put("text", text);
		if (hasChildren) {
			map.put("children", true); // jsTree will lazy load this
		}
		return map;
	}

	// Preloaded flat JSON (comes from DB in your real case)
	@GetMapping("/preloaded")
	public List<Map<String, Object>> getPreloadedTree() {
		List<Map<String, Object>> nodes = new ArrayList<>();

		// Root A
		nodes.add(node("1", "#", "Root A (Preloaded)", false));
		nodes.add(node("1_1", "1", "Child A1 (Preloaded)", false));
		nodes.add(node("1_2", "1", "Child A2 (Preloaded)", false));

		// Root B
		nodes.add(node("2", "#", "Root B (Preloaded)", false));
		nodes.add(node("2_1", "2", "Child B1 (Preloaded)", false));

		// Preloaded children under B1
		nodes.add(node("2_1_1", "2_1", "B1-Child 1 (Preloaded)", false));
		nodes.add(node("2_1_2", "2_1", "B1-Child 2 (Preloaded)", false));
		// B1-Child 3 now has lazy descendants
		nodes.add(node("2_1_3", "2_1", "B1-Child 3 (Preloaded, has lazy children)", true));

		nodes.add(node("2_2", "2", "Child B2 (Lazy)", true));

		// Root C (lazy load)
		nodes.add(node("3", "#", "Root C (Lazy)", true));

		return nodes;
	}

	// Lazy loaded children
	@GetMapping("/children/{id}")
	public List<Map<String, Object>> getChildren(@PathVariable String id) {

		System.out.println("Lazy loading children for node id: " + id);

		List<Map<String, Object>> children = new ArrayList<>();

		if ("2_1_3".equals(id)) {
			// Lazy children under B1-Child 3
			children.add(node("2_1_3_1", "2_1_3", "Lazy Child B1-3-1", false));
			children.add(node("2_1_3_2", "2_1_3", "Lazy Child B1-3-2", true)); // further lazy
		} else if ("2_2".equals(id)) {
			children.add(node("2_2_1", "2_2", "Lazy Child B2-1", false));
			children.add(node("2_2_2", "2_2", "Lazy Child B2-2", true));
		} else if ("3".equals(id)) {
			children.add(node("3_1", "3", "Lazy Child C1", false));
			children.add(node("3_2", "3", "Lazy Child C2", false));
		} else if ("2_2_2".equals(id)) {
			children.add(node("2_2_2_1", "2_2_2", "Deep Lazy Child B2-2-1", false));
			children.add(node("2_2_2_2", "2_2_2", "Deep Lazy Child B2-2-2", false));
		}

		return children;
	}

	// ---------------------------------------------------------------------------

	private final Gson gson = new Gson();

	@GetMapping("/getRootNodes")
	public List<JsTreeNode2> getRootNodes() {
		System.out.println("getRootNodes() is called");

		List<JsTreeNode2> nodes = new ArrayList<>();

		Map<String, Object> data1 = new HashMap<>();
		data1.put("level", "LV1");
		nodes.add(new JsTreeNode2("LV1_1", "Root Node 1", "#", data1, true)); // 👈 true = expandable

		Map<String, Object> data2 = new HashMap<>();
		data2.put("level", "LV1");
		nodes.add(new JsTreeNode2("LV1_2", "Root Node 2", "#", data2, true));

		return nodes;
	}

	@GetMapping("/getChildren/{parentId}")
	public List<JsTreeNode2> getChildren(@PathVariable("parentId") String parentId,
			@RequestParam("level") String level) {

		System.out.println("getChildren() is called, parentId=" + parentId);

		List<JsTreeNode2> nodes = new ArrayList<>();

		if ("LV1".equals(level)) {
			Map<String, Object> data = new HashMap<>();
			data.put("level", "LV2");
			nodes.add(new JsTreeNode2(parentId + "_LV2_1", "Child A1", parentId, data, true));
			nodes.add(new JsTreeNode2(parentId + "_LV2_2", "Child A2", parentId, data, true));
		} else if ("LV2".equals(level)) {
			Map<String, Object> data = new HashMap<>();
			data.put("level", "LV3");
			nodes.add(new JsTreeNode2(parentId + "_LV3_1", "Child A1-1", parentId, data, true));
		} else if ("LV3".equals(level)) {
			Map<String, Object> data = new HashMap<>();
			data.put("level", "LV4");
			nodes.add(new JsTreeNode2(parentId + "_LV4_1", "Leaf Node", parentId, data, false));
		}

		return nodes;
	}

}
