package com.springdata.jpa.service;

import com.springdata.jpa.models.Author;
import com.springdata.jpa.repository.AuthorRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AuthorService {
	
	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private StreamBridge streamBridge;
	
	
	@Async
	public CompletableFuture<List<Author>> getAllAuthors() {
		try {
			List<Author> authors = authorRepository.findAll();
			return CompletableFuture.completedFuture(authors);
		} catch ( Exception e) { //Change the Exception and always use the specific exception and register the particular to service method which can we passed by controller
			return CompletableFuture.failedFuture(null);
		}
		
	}
	
	@Async
	public CompletableFuture<List<Author>> addMultipleAuthorsAsync(List<Author> authors, int batchSize) {
		List <Author> result = 
		IntStream.range(0, (authors.size() + batchSize - 1) / batchSize)
				.mapToObj(i -> authors.subList(i * batchSize, Math.min(i * batchSize + batchSize, authors.size())))
				.map(authorRepository::saveAllAndFlush)
				.flatMap(List::stream)
				.collect(Collectors.toList());
		return CompletableFuture.completedFuture(result);
	}
	
	@Async
	public CompletableFuture<Void> deleteAllAuthorsAsync() {
		try {
			authorRepository.deleteAll();
			sendAuthorMessage("All authors deleted successfully", "info");
			return CompletableFuture.completedFuture(null); 
		} catch (Exception e) {
			sendAuthorMessage("Error deleting authors: " + e.getMessage(), "error");
			return CompletableFuture.failedFuture(e);
		}
	}

	public void sendAuthorMessage(String message, String info) {
		JSONObject payload = new JSONObject();
		payload.put("message", message);
		payload.put("info", info);//use streamBridge for non spring message use functional and integration programming
		streamBridge.send("createLog-out", payload.toString());
	}
	
	
}