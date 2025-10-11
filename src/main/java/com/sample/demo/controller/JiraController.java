package com.sample.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sample.demo.models.IssueRequest;
import com.sample.demo.service.JiraService;

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
}
