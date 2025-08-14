package org.dragon.yunpeng.jstree.services;

import java.util.List;

import org.dragon.yunpeng.jstree.dtos.JsTreeNode;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class JsTreeJsonReader {

	public void constructJsTreeNodeListFromJsonFile() throws IOException, StreamReadException, DatabindException {
		ObjectMapper mapper = new ObjectMapper();

		// Read JSON from a file (replace with your file path)
		String path = "C:\\Projects\\SpringBootJsTree\\mini-symboltree-data.json";

		List<JsTreeNode> nodes = mapper.readValue(new File(path), new TypeReference<List<JsTreeNode>>() {
		});

		// Example: print each node
		for (JsTreeNode node : nodes) {
			printNode(node, 0);
		}
	}

	private String repeat(String s, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(s);
		}
		return sb.toString();
	}

	private void printNode(JsTreeNode node, int level) {
		String indent = repeat("  ", level);
		System.out.println(indent + node.getText() + " - " + node.getData());

		if (node.getChildren() != null) {
			for (JsTreeNode child : node.getChildren()) {
				printNode(child, level + 1);
			}
		}
	}
}
