package com.example.ragmak.store;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;

import java.util.*;

public class InMemoryVectorStore {
  private final EmbeddingModel embeddingModel;
  private final List<VectorDocument> documents = new ArrayList<>();
    
  public static class VectorDocument {
    public final String id;
    public final String content;
    public final float[] embedding;

    public VectorDocument(String id, String content, float[] embedding) {
      this.id = id;
      this.content = content;
      this.embedding = embedding;
    }
  }

  public InMemoryVectorStore(EmbeddingModel embeddingModel) {
    this.embeddingModel = embeddingModel;
    initDocs();
  }

  private void initDocs() {
    // Initialize with some sample documents
    addDoc("1", "Spring AI makes it easy to build AI-powered applications.");
    addDoc("2", "RAG stands for Retrieval-Augmented Generation.");
    addDoc("3", "Spring Boot simplifies Java application development. ");
  }

  private void addDoc(String id, String content) {
    EmbeddingResponse response = embeddingModel.call(
        new org.springframework.ai.embedding.EmbeddingRequest(List.of(content), null));
    float[] embedding = response.getResults().get(0).getOutput();
    documents.add(new VectorDocument(id, content, embedding));
  }

  public List<VectorDocument> similaritySearch(String query, int topK) {
    EmbeddingResponse response = embeddingModel.call(
        new org.springframework.ai.embedding.EmbeddingRequest(List.of(query), null));
    float[] queryEmbedding = response.getResults().get(0).getOutput();
    return documents.stream()
        .sorted(Comparator.comparingDouble(doc -> -cosineSimilarity(queryEmbedding, doc.embedding)))
        .limit(topK)
        .toList();
  }

  private double cosineSimilarity(float[] a, float[] b) {
    double dotProduct = 0.0;
    double normA = 0.0;
    double normB = 0.0;
    for (int i = 0; i < a.length; i++) {
      dotProduct += a[i] * b[i];
      normA += a[i] * a[i];
      normB += b[i] * b[i];
    }
    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
  }
}