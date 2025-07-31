package org.dragon.yunpeng.jstree.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

@RestController
@RequestMapping("/api/tree")
public class TreeController {

    private static final Path TREE_FILE = Paths.get("tree-data.json");

    @PostMapping("/saveTree")
    public ResponseEntity<String> saveTree(@RequestBody String jsonTree) {
        try {
        	Files.write(
        		    TREE_FILE,
        		    jsonTree.getBytes(StandardCharsets.UTF_8),
        		    StandardOpenOption.CREATE,
        		    StandardOpenOption.TRUNCATE_EXISTING
        		);
            return ResponseEntity.ok("Tree saved");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error saving tree: " + e.getMessage());
        }
    }
}

