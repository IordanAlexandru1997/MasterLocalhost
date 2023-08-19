package com.example.masterthesisproject.controllers;

import com.example.masterthesisproject.DatabaseBenchmark;
import com.example.masterthesisproject.services.ArangoDBService;
import com.example.masterthesisproject.services.Neo4jService;
import com.example.masterthesisproject.services.OrientDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BenchmarkController {

    @Autowired
    private Neo4jService neo4jService;

    @Autowired
    private OrientDBService orientDBService;

    @Autowired
    private ArangoDBService arangoDBService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/runBenchmark")
    public String runBenchmark(@RequestParam String database,
                               @RequestParam(required = false) boolean optimizeFlag,
                               @RequestParam(defaultValue = "0") int percentCreate,
                               @RequestParam(defaultValue = "0") int percentRead,
                               @RequestParam(defaultValue = "0") int percentUpdate,
                               @RequestParam(defaultValue = "0") int percentDelete,
                               @RequestParam(defaultValue = "0") int numEntries,
                               @RequestParam(defaultValue = "0") int minEdgesPerNode,
                               @RequestParam(defaultValue = "0") int maxEdgesPerNode,
                               Model model) {

        neo4jService.setUiOptimizationFlag(optimizeFlag);
        orientDBService.setUiOptimizationFlag(optimizeFlag);
        arangoDBService.setUiOptimizationFlag(optimizeFlag);
        switch (database) {
            case "Neo4j":
                new DatabaseBenchmark(neo4jService, numEntries, optimizeFlag)
                        .runBenchmark(percentCreate, percentRead, percentUpdate, percentDelete, minEdgesPerNode, maxEdgesPerNode);
                break;
            case "OrientDB":
                new DatabaseBenchmark(orientDBService, numEntries, optimizeFlag)
                        .runBenchmark(percentCreate, percentRead, percentUpdate, percentDelete, minEdgesPerNode, maxEdgesPerNode);
                break;
            case "ArangoDB":
                new DatabaseBenchmark(arangoDBService, numEntries, optimizeFlag)
                        .runBenchmark(percentCreate, percentRead, percentUpdate, percentDelete, minEdgesPerNode, maxEdgesPerNode);
                break;
        }

        model.addAttribute("message", "Benchmark completed for " + database);
        return "index";
    }
}
