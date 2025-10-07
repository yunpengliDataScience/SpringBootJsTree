package org.dragon.yunpeng.jstree.sandbox;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsTreeFlatFilter {
	public static JsonArray filterModifiedWithAncestorsFlat(JsonArray flatNodes) {
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
            String status = node.has("status") ? node.get("status").getAsString() : "";
            if ("Modified".equalsIgnoreCase(status)) {
                String currentId = node.get("id").getAsString();
                while (currentId != null && !currentId.equals("#")) {
                    if (!includedIds.contains(currentId)) {
                        includedIds.add(currentId);
                    }
                    JsonObject current = nodeMap.get(currentId);
                    if (current == null) break;
                    String parent = current.has("parent") ? current.get("parent").getAsString() : null;
                    if (parent == null || parent.equals("#")) break;
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

        return filtered;
    }

    // Example usage
    public static void main(String[] args) {
    	String json = "["
                + "{\"id\":\"1\",\"text\":\"Root A\",\"parent\":\"#\",\"status\":\"Normal\"},"
                + "{\"id\":\"2\",\"text\":\"Child A1\",\"parent\":\"1\",\"status\":\"Modified\"},"
                + "{\"id\":\"3\",\"text\":\"Child A2\",\"parent\":\"1\",\"status\":\"Normal\"},"
                + "{\"id\":\"4\",\"text\":\"Grandchild A1a\",\"parent\":\"2\",\"status\":\"Normal\"},"
                + "{\"id\":\"5\",\"text\":\"Root B\",\"parent\":\"#\",\"status\":\"Normal\"}"
                + "]";

        JsonArray flatTree = JsonParser.parseString(json).getAsJsonArray();
        JsonArray filtered = filterModifiedWithAncestorsFlat(flatTree);

        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(filtered));
    }
}
