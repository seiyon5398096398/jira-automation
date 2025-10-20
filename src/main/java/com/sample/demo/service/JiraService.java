package com.sample.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.demo.models.IssueRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class JiraService {

    @Value("${jira.sample-file}")
    private String sampleFile;

    @Value("${jira.email}")
    private String email;

    @Value("${jira.api-token}")
    private String apiToken;

    @Value("${jira.url}")
    private String jiraUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Read JSON from sample.json in resources
     */
    public IssueRequest readSampleJson() throws Exception {
        ClassPathResource resource = new ClassPathResource(sampleFile);
        return objectMapper.readValue(Files.readAllBytes(resource.getFile().toPath()), IssueRequest.class);
    }
    
    public String createIssueFromJson(String projectKey, String summary, JsonNode descriptionNode, String issueType, String assigneeId) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> fields = new HashMap<>();
        fields.put("project", Map.of("key", projectKey));
        fields.put("summary", summary);

        // Convert descriptionNode (JsonNode) to Map so Jira accepts it
        Map<String, Object> description = new ObjectMapper().convertValue(descriptionNode, Map.class);
        fields.put("description", description);

        fields.put("issuetype", Map.of("name", issueType));
        fields.put("assignee", Map.of("id", assigneeId));

        Map<String, Object> body = Map.of("fields", fields);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(email, apiToken);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                jiraUrl,
                request,
                String.class
        );

        return response.getBody();
    }

    /**
     * Send issue to Jira Cloud via REST API
     */
    public String createIssueInJira(IssueRequest issueRequest) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Basic Auth: email + API token
        String auth = email + ":" + apiToken;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        // Convert issueRequest to JSON string
        String jsonBody = objectMapper.writeValueAsString(issueRequest);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        // POST request to Jira Cloud
        ResponseEntity<String> response = restTemplate.postForEntity(jiraUrl, entity, String.class);

        // Return response (contains issue key and URL)
        return response.getBody();
    }
}
