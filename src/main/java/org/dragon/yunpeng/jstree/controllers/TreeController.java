package org.dragon.yunpeng.jstree.controllers;

import org.dragon.yunpeng.jstree.dtos.JsTreeNode;
import org.dragon.yunpeng.jstree.dtos.JsTreeNode2;
import org.dragon.yunpeng.jstree.services.JsTreeSaveService;
import org.dragon.yunpeng.jstree.services.JsTreeService;
import org.springframework.beans.factory.annotation.Autowired;
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

			jsTreeSaveService.saveModifiedNodes(nodes);

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

			jsTreeSaveService.saveModifiedNodes2(nodes);

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

	@GetMapping("/jstree-data")
	public ResponseEntity<List<JsTreeNode>> getJsTreeData() {
		List<JsTreeNode> nodes = jsTreeService.getJsTreeData();

		return ResponseEntity.ok(nodes); // Spring automatically converts to proper JSON
	}

	@GetMapping("/jstree-data2")
	public ResponseEntity<List<JsTreeNode2>> getJsTreeData2() {

		System.out.println("Retrieving data through /jstree-data2");
		List<JsTreeNode2> nodes = jsTreeService.getJsTreeData2();

		return ResponseEntity.ok(nodes); // Spring automatically converts to proper JSON
	}
}
