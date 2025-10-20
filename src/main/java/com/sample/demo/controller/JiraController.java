package com.sample.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sample.demo.models.IssueRequest;
import com.sample.demo.service.JiraService;
import com.sample.demo.service.JsonToJiraService;

@RestController
@RequestMapping("/api/jira")
public class JiraController {

    @Autowired
    private JiraService jiraService;

    /**
     * Create Jira issue from sample.json
     * POST http://localhost:8080/api/jira/create
     */
    @PostMapping("/create")
    public String createIssueFromSample() throws Exception {
        IssueRequest issue = jiraService.readSampleJson();
        return jiraService.createIssueInJira(issue);
    }

    /**
     * Create Jira issue from JSON body
     * POST http://localhost:8080/api/jira/createFromBody
     * Body: IssueRequest JSON
     */
    @PostMapping("/createFromBody")
    public String createIssueFromBody(@RequestBody IssueRequest issue) throws Exception {
        return jiraService.createIssueInJira(issue);
    }
    
    @Autowired
    private JsonToJiraService jsonToJiraService;

    @PostMapping("/upload-json")
    public ResponseEntity<String> uploadJson(@RequestParam("file") MultipartFile file) {
        String response = jsonToJiraService.processJsonAndCreateIssue(file);
        return ResponseEntity.ok(response);
    }
}
