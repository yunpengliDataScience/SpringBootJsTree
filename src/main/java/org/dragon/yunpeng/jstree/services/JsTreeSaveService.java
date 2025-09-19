package org.dragon.yunpeng.jstree.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.dragon.yunpeng.jstree.Constants;
import org.dragon.yunpeng.jstree.dtos.JsTreeNode;
import org.dragon.yunpeng.jstree.dtos.JsTreeNode2;
import org.dragon.yunpeng.jstree.entities.ChangeRequestSandbox;
import org.dragon.yunpeng.jstree.entities.JsonAuditTrail;
import org.dragon.yunpeng.jstree.entities.LV1Symb;
import org.dragon.yunpeng.jstree.entities.LV2Symb;
import org.dragon.yunpeng.jstree.entities.LV3Symb;
import org.dragon.yunpeng.jstree.entities.LV4Symb;
import org.dragon.yunpeng.jstree.repositories.ChangeRequestSandboxRepository;
import org.dragon.yunpeng.jstree.repositories.JsonAuditTrailRepository;
import org.dragon.yunpeng.jstree.repositories.LV1SymbRepository;
import org.dragon.yunpeng.jstree.repositories.LV2SymbRepository;
import org.dragon.yunpeng.jstree.repositories.LV3SymbRepository;
import org.dragon.yunpeng.jstree.repositories.LV4SymbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsTreeSaveService {

	@Autowired
	private LV1SymbRepository lv1Repo;
	@Autowired
	private LV2SymbRepository lv2Repo;
	@Autowired
	private LV3SymbRepository lv3Repo;
	@Autowired
	private LV4SymbRepository lv4Repo;
	@Autowired
	private JsonAuditTrailRepository jsonAuditTrailRepository;
	@Autowired
	private ChangeRequestSandboxRepository changeRequestSandboxRepo;

	@Transactional
	public void saveModifiedNodesHierarchicalFormat(List<JsTreeNode> nodes) {
		for (JsTreeNode node : nodes) {
			processNode(node, null); // root level has no parent
		}
	}

	private void processNode(JsTreeNode node, Long parentId) {
		Map<String, Object> data = node.getData();
		if (data == null) {
			return;
		}

		String status = (String) data.get("status");
		String databaseIdStr = "";

		Object databaseIdObj = data.get("databaseId");
		if (databaseIdObj instanceof Integer) {
			databaseIdStr = ((Integer) databaseIdObj).toString();
		}

		String level = (String) data.get("level");

		if ("Modified".equalsIgnoreCase(status)) {
			if ("LV1".equalsIgnoreCase(level)) {
				LV1Symb entity = null;
				if (databaseIdStr == null || databaseIdStr.isEmpty()) {
					entity = new LV1Symb();
				} else {
					entity = lv1Repo.findById(Long.parseLong(databaseIdStr)).orElse(new LV1Symb());
				}
				entity.setName(node.getText());
				entity.setLevel(level);
				entity.setField1((String) data.get("field1"));
				lv1Repo.save(entity);
				databaseIdStr = String.valueOf(entity.getId()); // update id for children
			} else if ("LV2".equalsIgnoreCase(level)) {
				LV2Symb entity = null;
				if (databaseIdStr == null || databaseIdStr.isEmpty()) {
					entity = new LV2Symb();

					if (parentId != null) {
						LV1Symb parent = lv1Repo.getById(parentId);
						entity.setLv1(parent);
					}

				} else {
					entity = lv2Repo.findById(Long.parseLong(databaseIdStr)).orElse(new LV2Symb());
				}
				entity.setName(node.getText());
				entity.setLevel(level);
				entity.setField1((String) data.get("field1"));
				entity.setField2((String) data.get("field2"));
				lv2Repo.save(entity);
				databaseIdStr = String.valueOf(entity.getId());
			} else if ("LV3".equalsIgnoreCase(level)) {
				LV3Symb entity = null;
				if (databaseIdStr == null || databaseIdStr.isEmpty()) {
					entity = new LV3Symb();

					if (parentId != null) {
						LV2Symb parent = lv2Repo.getById(parentId);
						entity.setLv2(parent);
					}

				} else {
					entity = lv3Repo.findById(Long.parseLong(databaseIdStr)).orElse(new LV3Symb());
				}
				entity.setName(node.getText());
				entity.setLevel(level);
				entity.setField1((String) data.get("field1"));
				entity.setField2((String) data.get("field2"));
				entity.setField3((String) data.get("field3"));
				lv3Repo.save(entity);
				databaseIdStr = String.valueOf(entity.getId());
			} else if ("LV4".equalsIgnoreCase(level)) {
				LV4Symb entity = null;
				if (databaseIdStr == null || databaseIdStr.isEmpty()) {
					entity = new LV4Symb();

					if (parentId != null) {
						LV3Symb parent = lv3Repo.getById(parentId);
						entity.setLv3(parent);
					}

				} else {
					entity = lv4Repo.findById(Long.parseLong(databaseIdStr)).orElse(new LV4Symb());
				}
				entity.setName(node.getText());
				entity.setLevel(level);
				entity.setField1((String) data.get("field1"));
				entity.setField2((String) data.get("field2"));
				entity.setField3((String) data.get("field3"));
				entity.setField4((String) data.get("field4"));
				lv4Repo.save(entity);
				databaseIdStr = String.valueOf(entity.getId());
			}
		}

		// Process children recursively
		if (node.getChildren() != null) {
			Long thisId = (databaseIdStr == null || databaseIdStr.isEmpty()) ? null : Long.valueOf(databaseIdStr);
			for (JsTreeNode child : node.getChildren()) {
				processNode(child, thisId);
			}
		}
	}

	@Transactional
	public void saveAudit(String json, String action, String userName) {
		JsonAuditTrail audit = new JsonAuditTrail();
		audit.setJsonContent(json);
		audit.setAction(action);
		audit.setUserName(userName);
		jsonAuditTrailRepository.save(audit);
	}

	@Transactional
	public void saveChangeRequest(String json, String status, String userName) {

		// retrieve an unapproved change request in json format
		ChangeRequestSandbox changeRequest = changeRequestSandboxRepo.getChangeRequestByNotEqualStatus(Constants.CR_APPROVED);

		// create a new record if unapproved change request does not exist.
		if (changeRequest == null) {
			changeRequest = new ChangeRequestSandbox();
		}
		changeRequest.setJsonContent(json);
		changeRequest.setStatus(status);
		changeRequest.setUserName(userName);
		changeRequest.setTimestamp(LocalDateTime.now());

		changeRequestSandboxRepo.save(changeRequest);
	}

	@Transactional
	public void saveModifiedNodesFlatFormat(List<JsTreeNode2> nodes) {

		Map<String, JsTreeNode2> idNodeMap = constructIdAndNodeMap(nodes);

		for (JsTreeNode2 node : nodes) {
			Map<String, Object> data = node.getData();
			if (data != null) {
				String status = (String) data.get("status");
				String databaseIdStr = "";

				Object databaseIdObj = data.get("databaseId");
				if (databaseIdObj instanceof Integer) {
					databaseIdStr = ((Integer) databaseIdObj).toString();
				}

				String level = (String) data.get("level");
				String nodeId = node.getId();
				String parentId = node.getParent();

				if ("Modified".equalsIgnoreCase(status)) {
					if ("LV1".equalsIgnoreCase(level)) {
						LV1Symb entity = null;
						if (databaseIdStr == null || databaseIdStr.isEmpty()) {
							entity = new LV1Symb();
						} else {
							entity = lv1Repo.findById(Long.parseLong(databaseIdStr)).orElse(new LV1Symb());
						}
						entity.setName(node.getText());
						entity.setLevel(level);
						entity.setField1((String) data.get("field1"));
						lv1Repo.save(entity);

						// update id for children
						JsTreeNode2 currentNode = idNodeMap.get(nodeId);
						currentNode.getData().put("databaseId", entity.getId());

					} else if ("LV2".equalsIgnoreCase(level)) {
						LV2Symb entity = null;
						if (databaseIdStr == null || databaseIdStr.isEmpty()) {
							entity = new LV2Symb();

							if (parentId != null) {
								JsTreeNode2 parentNode = idNodeMap.get(parentId);

								long parentDbId = retrieveParentDbId(parentNode);

								LV1Symb parent = lv1Repo.getById(parentDbId);
								entity.setLv1(parent);
							}

						} else {
							entity = lv2Repo.findById(Long.parseLong(databaseIdStr)).orElse(new LV2Symb());
						}
						entity.setName(node.getText());
						entity.setLevel(level);
						entity.setField1((String) data.get("field1"));
						entity.setField2((String) data.get("field2"));
						lv2Repo.save(entity);

						JsTreeNode2 currentNode = idNodeMap.get(nodeId);
						currentNode.getData().put("databaseId", entity.getId());

					} else if ("LV3".equalsIgnoreCase(level)) {
						LV3Symb entity = null;
						if (databaseIdStr == null || databaseIdStr.isEmpty()) {
							entity = new LV3Symb();

							if (parentId != null) {
								JsTreeNode2 parentNode = idNodeMap.get(parentId);
								long parentDbId = retrieveParentDbId(parentNode);

								LV2Symb parent = lv2Repo.getById(parentDbId);
								entity.setLv2(parent);
							}

						} else {
							entity = lv3Repo.findById(Long.parseLong(databaseIdStr)).orElse(new LV3Symb());
						}
						entity.setName(node.getText());
						entity.setLevel(level);
						entity.setField1((String) data.get("field1"));
						entity.setField2((String) data.get("field2"));
						entity.setField3((String) data.get("field3"));
						lv3Repo.save(entity);

						JsTreeNode2 currentNode = idNodeMap.get(nodeId);
						currentNode.getData().put("databaseId", entity.getId());

					} else if ("LV4".equalsIgnoreCase(level)) {
						LV4Symb entity = null;
						if (databaseIdStr == null || databaseIdStr.isEmpty()) {
							entity = new LV4Symb();

							if (parentId != null) {
								JsTreeNode2 parentNode = idNodeMap.get(parentId);
								long parentDbId = retrieveParentDbId(parentNode);

								LV3Symb parent = lv3Repo.getById(parentDbId);
								entity.setLv3(parent);
							}

						} else {
							entity = lv4Repo.findById(Long.parseLong(databaseIdStr)).orElse(new LV4Symb());
						}
						entity.setName(node.getText());
						entity.setLevel(level);
						entity.setField1((String) data.get("field1"));
						entity.setField2((String) data.get("field2"));
						entity.setField3((String) data.get("field3"));
						entity.setField4((String) data.get("field4"));
						lv4Repo.save(entity);

						JsTreeNode2 currentNode = idNodeMap.get(nodeId);
						currentNode.getData().put("databaseId", entity.getId());
					}
				}
			}

		}
	}

	private long retrieveParentDbId(JsTreeNode2 parentNode) {
		long parentDbId;
		Object parentDbIdObj = parentNode.getData().get("databaseId");
		if (parentDbIdObj instanceof Integer) {
			parentDbId = ((Integer) parentDbIdObj).longValue();
		} else {
			parentDbId = (Long) parentDbIdObj;
		}
		return parentDbId;
	}

	private Map<String, JsTreeNode2> constructIdAndNodeMap(List<JsTreeNode2> nodes) {
		Map<String, JsTreeNode2> idNodeMap = new HashMap<String, JsTreeNode2>();
		for (JsTreeNode2 node : nodes) {
			String nodeId = node.getId();

			idNodeMap.put(nodeId, node);
		}

		return idNodeMap;
	}
}
