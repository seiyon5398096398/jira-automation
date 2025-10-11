package com.sample.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.demo.models.IssueRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.util.Base64;

//@Service
//public class JiraService {
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    // Read XML or JSON file and parse into IssueRequest
//    public IssueRequest parseIssueFromJsonFile(String filePath) throws IOException {
//        return objectMapper.readValue(new File(filePath), IssueRequest.class);
//    }
//
//    // Simulate Jira issue creation
//    public String createIssue(IssueRequest issueRequest) throws JsonProcessingException {
//    	
//    	
//    	RestTemplate restTemplate = new RestTemplate();
//    	HttpHeaders headers = new HttpHeaders();
//    	headers.setContentType(MediaType.APPLICATION_JSON);
//
//    	// Basic Auth with email + API token
//    	String auth = "seiyonkumaripala@gmail.com:ATATT3xFfGF0eHJ8mVA88yuLYtaNfX6l2InL7lR6eFIOvhBV0nKnkGiQeLHoEhXUH9-kIdPDVmf_qLvt7CQDWyyi3tFpHHGxHlmWbLap0XF89H9PnWoxU01eqRb_PfUez08xUN-6SFV-kyowrSbSLGEdISKKXOBpNeH2RPpX13iZkIAyZn8W1MY=4E3C7AF4";
//    	String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
//    	headers.set("Authorization", "Basic " + encodedAuth);
//
//    	// Convert your IssueRequest to JSON
//    	ObjectMapper objectMapper = new ObjectMapper();
//    	String jsonBody;
//		
//			jsonBody = objectMapper.writeValueAsString(issueRequest);
//		
//
//    	HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
//
//    	// Jira Cloud API endpoint
//    	String jiraUrl = "https://seiyonkumaripala.atlassian.net/rest/api/3/issue";
//    	ResponseEntity<String> response = restTemplate.postForEntity(jiraUrl, entity, String.class);
//
//    	
//    	
//    	return response.getBody();
//    }
//}


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
