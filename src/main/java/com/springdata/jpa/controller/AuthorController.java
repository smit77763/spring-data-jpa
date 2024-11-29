package com.springdata.jpa.controller;

import com.springdata.jpa.models.Author;
import com.springdata.jpa.repository.AuthorRepository;
import com.springdata.jpa.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
	
	@Autowired
	private AuthorRepository authorRepository;
	
	@Autowired
	private AuthorService authorService;
	
	@GetMapping(path = "/all" )
	public ResponseEntity<Map<String, Object>>  getAuthors() {
		List<Author> authorList= authorService.getAllAuthors();
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK.value());
		response.put("count", authorList.size());
		response.put("data", authorList);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(path = "/add")
	public ResponseEntity<Map<String, Object>> addAuthor(@RequestBody Author author) {

		Author addedAuthor = authorService.addAuthor(author);
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK.value());
		response.put("data", addedAuthor);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(path = "/addMultiple") 
	public ResponseEntity<Map<String, Object>> addMultipleAuthors(@RequestBody List<Author> authors) {
		
		authorService.addMultipleAuthors(authors, 100);
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK.value());
		response.put("message", "Authors Added Successfully");
		response.put("count", authors.size());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(path = "/addMultipleAsync")
	public ResponseEntity<Map<String, Object>> addMultipleAuthorsAsync(@RequestBody List<Author> authors) {
		List<Author> addedAuthors = null;
		try {
			addedAuthors = authorService.addMultipleAuthorsAsync(authors, 10).get();

		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK.value());
		response.put("message", "Authors Added Asynchronously Successfully");
		response.put("count",addedAuthors.size());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<Map<String, Object>> deleteAuthorById(@PathVariable("id") int id) {
		authorService.deleteAuthorById(id);
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK.value());
		response.put("message", "Author with id " + id + " Deleted Successfully");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PutMapping(path = "/update/{id}")
	public ResponseEntity<Map<String, Object>> updateAuthorEmail(@PathVariable("id") int id, @RequestParam("email") String email) {
		authorService.updateAuthorEmail(email, id);
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK.value());
		response.put("message", "Author  with id " + id + " email updated successfully "+ email);
		return new ResponseEntity<>(response, HttpStatus.OK);
		
	}
	
	@DeleteMapping(path = "/deleteAll")
	public ResponseEntity<Map<String, Object>> deleteAllAuthors() {
		
		authorService.deleteAllAuthors();
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK.value());
		response.put("message", "All Authors Deleted Successfully");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
}