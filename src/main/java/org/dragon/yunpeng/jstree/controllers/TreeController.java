package org.dragon.yunpeng.jstree.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

@RestController
@RequestMapping("/api/tree")
public class TreeController {

	// private static final Path TREE_FILE = Paths.get("tree-data.json");

	private final ObjectMapper objectMapper = new ObjectMapper();

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
}
