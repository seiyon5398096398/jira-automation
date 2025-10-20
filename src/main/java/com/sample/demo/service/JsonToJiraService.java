package com.sample.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class JsonToJiraService {

    @Autowired
    private JiraService jiraService;

    public String processJsonAndCreateIssue(MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(file.getInputStream());
            JsonNode fields = root.get("fields");

            String projectKey = fields.get("project").get("key").asText();
            String summary = fields.get("summary").asText();
            JsonNode descriptionNode = fields.get("description");
            String issueType = fields.get("issuetype").get("name").asText();
            String assigneeId = fields.get("assignee").get("id").asText();

            // Send everything to JiraService
            return jiraService.createIssueFromJson(projectKey, summary, descriptionNode, issueType, assigneeId);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing JSON: " + e.getMessage();
        }
    }
}
