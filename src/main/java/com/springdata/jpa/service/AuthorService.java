package com.springdata.jpa.service;

import com.springdata.jpa.models.Author;
import com.springdata.jpa.repository.AuthorRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@EnableAsync
public class AuthorService {
	
	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private StreamBridge streamBridge;
	
	
	public List<Author> getAllAuthors() {
		try {
			List<Author> authors = authorRepository.findAll();
			sendAuthorMessage("Authors fetched successfully" + authors.size(), "info");
			return authors;
		} catch ( Exception e) {
			sendAuthorMessage("Error fetching authors "+ e.getMessage(), "error");
			return null;
		}
		
	}
	
	public Author addAuthor(Author author) {
		return authorRepository.save(author);
	}
	
	
	public void addMultipleAuthors(List<Author> authors, int batchSize) {
		
		for (int i = 0; i < authors.size(); i += batchSize) {
			int end = Math.min(i + batchSize, authors.size());
			List<Author> authorBatch = authors.subList(i, end);
			authorRepository.saveAllAndFlush(authorBatch);
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
		sendAuthorMessage("Yeah!! we added the authors successfully and asynchronously" + result.size(), "info");
		return CompletableFuture.completedFuture(result);
	}
	
	public void sendAuthorMessage(String message, String info) {
		JSONObject payload = new JSONObject();
		payload.put("message", message);
		payload.put("info", info);//use streamBridge for non spring message use functional and integration programming
		streamBridge.send("createLog-out", payload.toString());
	}
	
	
	public void updateAuthorEmail(String email, int id) {
		Author a = authorRepository.updateAuthorEmail(email, id);
	}
	
	public void deleteAllAuthors() {
		try {
			authorRepository.deleteAll();
			sendAuthorMessage("All authors deleted successfully", "info");
		} catch (Exception e) {
			sendAuthorMessage("Error deleting authors " + e.getMessage(), "error");
		}
	}
	
	public void deleteAuthorById(int id) {
		authorRepository.deleteById(id);
	}
	
}