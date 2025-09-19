package org.dragon.yunpeng.jstree.controllers;

import org.dragon.yunpeng.jstree.Constants;
import org.dragon.yunpeng.jstree.dtos.JsTreeNode;
import org.dragon.yunpeng.jstree.dtos.JsTreeNode2;
import org.dragon.yunpeng.jstree.services.JsTreeSaveService;
import org.dragon.yunpeng.jstree.services.JsTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
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

	// Alternative JSON format (flat)
	@PostMapping("/saveAndApproveChangeRequest")
	public ResponseEntity<String> saveAndApproveChangeRequest(@RequestBody Object jsonTree) {

		System.out.println("jsonTree to save:");
		System.out.println(jsonTree);

		try {
			String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonTree);

			System.out.println("Flat JSON to save:");
			System.out.println(prettyJson);

			ObjectMapper mapper = new ObjectMapper();
			List<JsTreeNode2> nodes = mapper.readValue(prettyJson, new TypeReference<List<JsTreeNode2>>() {
			});

			// Save data into real symbol tables.
			jsTreeSaveService.saveModifiedNodesFlatFormat(nodes);

			// Mark change request as Approved.
			jsTreeSaveService.saveChangeRequest(prettyJson, Constants.CR_APPROVED, "SYS_USER");

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
			String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonTree);

			jsTreeSaveService.saveChangeRequest(prettyJson, Constants.CR_DRAFT, "SYS_USER");

			System.out.println("Flat JSON to save:");
			System.out.println(prettyJson);

			return ResponseEntity.ok("Pretty JSON tree saved.");
		} catch (IOException e) {
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
}
