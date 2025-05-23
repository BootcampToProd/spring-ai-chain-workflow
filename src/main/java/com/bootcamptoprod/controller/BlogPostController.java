package com.bootcamptoprod.controller;

import com.bootcamptoprod.service.BlogPostChainWorkflow;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog")
public class BlogPostController {

    private final BlogPostChainWorkflow chainWorkflow;

    /**
     * Constructor injection - Spring automatically provides our workflow service
     */
    public BlogPostController(BlogPostChainWorkflow chainWorkflow) {
        this.chainWorkflow = chainWorkflow;
    }

    /**
     * Main endpoint to generate blog posts using our chain workflow
     * Accepts a topic as request body and returns the complete generated blog post
     */
    @PostMapping("/generate")
    public String generateBlogPost(@RequestBody String topic) {
        return chainWorkflow.generateBlogPost(topic.trim());
    }

}