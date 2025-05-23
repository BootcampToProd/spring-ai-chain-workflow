package com.bootcamptoprod.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class BlogPostChainWorkflow {

    private final ChatClient chatClient;
    private final String[] systemPrompts;

    /**
     * Constructor that initializes our ChatClient and sets up the chain prompts
     * Spring will automatically inject the ChatClient.Builder
     */
    public BlogPostChainWorkflow(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        this.systemPrompts = initializeChainPrompts();
    }

    /**
     * Main method that processes a topic through our 4-step chain using a for loop
     * Each step's output becomes the input for the next step in the chain
     */
    public String generateBlogPost(String topic) {
        String response = topic; // Start with the user's input topic

        // Process through each step in our chain sequentially
        for (int i = 0; i < systemPrompts.length; i++) {
            System.out.println("Processing Step " + (i + 1) + ": " + getStepName(i));

            // Combine the current step's prompt with the previous step's output
            String input = String.format("%s\n\nInput: %s", systemPrompts[i], response);

            // Send to AI and get the response for this step
            response = chatClient.prompt(input).call().content();

            System.out.println("Completed Step " + (i + 1));
        }

        return response; // Return the final processed result
    }

    /**
     * Initialize our 4-step chain with specific prompts for each phase
     * Each prompt is designed to take the previous step's output and refine it further
     */
    private String[] initializeChainPrompts() {
        return new String[]{
                // Step 1: Research Phase - Generate key points and insights
                """
            You are a research assistant. Given a topic, generate 5-7 key research points 
            that would be important to cover in a comprehensive blog post. Focus on:
            - Current trends and developments
            - Common challenges or problems  
            - Best practices and solutions
            - Real-world applications
            
            Format your response as a numbered list with brief explanations for each point.
            Topic to research:
            """,

                // Step 2: Structure Phase - Create a logical outline
                """
            You are a content strategist. Based on the research points provided, 
            create a detailed blog post outline with:
            - Compelling title
            - Introduction hook
            - 4-6 main sections with subpoints
            - Conclusion that ties everything together
            
            Format as a clear outline with headers and bullet points.
            Research points to structure:
            """,

                // Step 3: Content Phase - Write the full blog post
                """
            You are an expert technical writer. Using the outline provided, 
            write a complete, engaging blog post (800-1200 words) that:
            - Has a compelling introduction that hooks the reader
            - Explains concepts clearly for beginners
            - Uses practical examples and analogies
            - Includes actionable insights
            - Has smooth transitions between sections
            - Ends with a strong conclusion
            
            Write in a conversational yet professional tone.
            Outline to expand:
            """,

                // Step 4: Polish Phase - Enhance readability and engagement
                """
            You are an editor specializing in technical content. Review and improve 
            the blog post by:
            - Adding engaging subheadings
            - Including relevant analogies or metaphors
            - Improving readability and flow
            - Adding a compelling call-to-action
            - Ensuring the tone is beginner-friendly but authoritative
            
            Return the polished, final version.
            Blog post to polish:
            """
        };
    }

    /**
     * Helper method to get descriptive names for each step (used for logging)
     * This makes it easier to track which step is currently being processed
     */
    private String getStepName(int stepIndex) {
        return switch (stepIndex) {
            case 0 -> "Research Phase";
            case 1 -> "Structure Phase";
            case 2 -> "Content Creation Phase";
            case 3 -> "Polish & Enhancement Phase";
            default -> "Unknown Step";
        };
    }
}