package com.example.ragmak;

import com.example.ragmak.store.InMemoryVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RagmakApplication {

	public static void main(String[] args) {
		SpringApplication.run(RagmakApplication.class, args);
	}

	@Bean
	public InMemoryVectorStore vectorStore(EmbeddingModel embeddingModel) {
		return new InMemoryVectorStore(embeddingModel);
	}
}

