package com.springdata.jpa.controller;

import com.springdata.jpa.models.Author;
import com.springdata.jpa.repository.AuthorRepository;
import com.springdata.jpa.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
	
	@Autowired
	private AuthorRepository authorRepository;
	
	@Autowired
	private AuthorService AuthorService;
	
	@GetMapping(path = "/all" )
	public List<Author> getAuthors() {
		return AuthorService.getAllAuthors();
	}
	
	@PostMapping(path = "/add")// Send the responseEntity always in return response
	public Author addAuthor(@RequestBody Author author) {
		
		return AuthorService.addAuthor(author);
	}
	
	@PostMapping(path = "/addMultiple") //Change this controller to handle large set of data(How to change)
	public ResponseEntity<Map<String, Object>> addMultipleAuthors(@RequestBody List<Author> authors) {
		
		List<Author> addedAuthors = AuthorService.addMultipleAuthors(authors);
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.OK.value());
		response.put("message", "Authors Added Successfully");
		response.put("data", addedAuthors);
		response.put("count", addedAuthors.size());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/delete/{id}")
	public void deleteAuthorById(@PathVariable("id") int id) {
		
		AuthorService.deleteAuthorById(id);
	}
	
	@PutMapping(path = "/update/{id}")
	public void updateAuthorEmail(@PathVariable("id") int id, @RequestParam("email") String email) {
		AuthorService.updateAuthorEmail(email, id);
	}
	
	@DeleteMapping(path = "/deleteAll")
	public void deleteAllAuthors() {
		AuthorService.deleteAllAuthors();
	}
	
	
}