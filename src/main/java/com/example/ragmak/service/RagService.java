package com.example.ragmak.service;

import java.util.Map;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import com.example.ragmak.store.InMemoryVectorStore;

@Service
public class RagService {
  private final ChatModel chatClient;
  private final InMemoryVectorStore vectorStore;

  public RagService(ChatModel chatClient, InMemoryVectorStore vectorStore) {
    this.chatClient = chatClient;
    this.vectorStore = vectorStore;
  }
  public String answer(String question) {
    // Retrieve relevant documents from the vector store
    var docs = vectorStore.similaritySearch(question, 2);

    
    String context = docs.stream()
        .map(d -> d.content)
        .reduce("", (a, b) -> a + "\n- " + b);

    String template = """
        You are an AI assistant helping users by providing information based on the following context:
        {documents}

        Answer the following question based on the above context:
        {question}
        """;

    // Create the prompt
    PromptTemplate promptTemplate = new PromptTemplate(template);
    Prompt prompt = promptTemplate.create(Map.of(
        "documents", context,
        "question", question
    ));

    // Get the answer from the chat client
    return chatClient.call(prompt).getResult().getOutput().getContent();
  }
}
