package com.springdata.jpa.controller;

import com.springdata.jpa.models.Author;
import com.springdata.jpa.repository.AuthorRepository;
import com.springdata.jpa.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

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
	public DeferredResult<ResponseEntity<Map<String, Object>>>  getAuthors() {
		DeferredResult<ResponseEntity<Map<String, Object>>> response = new DeferredResult<>();
		
		response.onTimeout(() -> {
			Map<String, Object> error = new HashMap<>();
			error.put("status", HttpStatus.REQUEST_TIMEOUT.value());
			error.put("message", "Request Timed Out");
			response.setErrorResult(new ResponseEntity<>(error, HttpStatus.REQUEST_TIMEOUT));
		});
		
		response.onError((Throwable t) -> {
			Map<String, Object> error = new HashMap<>();
			error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			error.put("message", "An error occurred");
			response.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
		});
		
		authorService.getAllAuthors()
				.thenAccept(authors -> {
					Map<String, Object> result = new HashMap<>();
					result.put("status", HttpStatus.OK.value());
					result.put("count", authors.size());
					result.put("data", authors);
					response.setResult(new ResponseEntity<>(result, HttpStatus.OK));
				})
				.exceptionally(ex -> {
					Map<String, Object> error = new HashMap<>();
					error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
					error.put("message", ex.getMessage());
					response.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
					return null;
				});
		
		return response;
	}
	

	@PostMapping(path = "/addMultipleAsyncDef")
	public DeferredResult<ResponseEntity<Map<String, Object>>> addMultipleAuthorsAsyncDeferred(@RequestBody List<Author> authors) {
		
		DeferredResult<ResponseEntity<Map<String,Object>>> response = new DeferredResult<>();

		response.onTimeout(() -> {
			Map<String, Object> error = new HashMap<>();
			error.put("status", HttpStatus.REQUEST_TIMEOUT.value());
			error.put("message", "Request Timed Out");
			response.setErrorResult(new ResponseEntity<>(error, HttpStatus.REQUEST_TIMEOUT));
		});

		response.onError((Throwable t) -> {
			Map<String, Object> error = new HashMap<>();
			error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			error.put("message", "An error occurred");
			response.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
		});

		authorService.addMultipleAuthorsAsync(authors, 10)
				.thenAccept(result -> {
					response.setResult(
							new ResponseEntity<>(Map.of(
									"status", HttpStatus.OK.value(),
									"message", "Authors Added Asynchronously Successfully",
									"count", result.size()
							), HttpStatus.OK)
					);
				})
				.exceptionally(ex -> {
					Map<String, Object> error = new HashMap<>();
					error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
					error.put("message", ex.getMessage());
					response.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
					return null;
				});
		
		return response;
	}
	
	
	@DeleteMapping(path = "/deleteAll")
	public DeferredResult<ResponseEntity<Map<String, Object>>> deleteAllAuthors() {

		DeferredResult<ResponseEntity<Map<String, Object>>> response = new DeferredResult<>();
		
		response.onTimeout(() -> {
			Map<String, Object> error = new HashMap<>();
			error.put("status", HttpStatus.REQUEST_TIMEOUT.value());
			error.put("message", "Request Timed Out");
			response.setErrorResult(new ResponseEntity<>(error, HttpStatus.REQUEST_TIMEOUT));
		});
		
		response.onError((Throwable t) -> {
			Map<String, Object> error = new HashMap<>();
			error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			error.put("message", "An error occurred");
			response.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
		});
		
		authorService.deleteAllAuthorsAsync()
				.thenRun(() -> {
					Map<String, Object> result = new HashMap<>();
					result.put("status", HttpStatus.OK.value());
					result.put("message", "All Authors Deleted Successfully");
					response.setResult(new ResponseEntity<>(result, HttpStatus.OK));
				})
				.exceptionally(ex -> {
					Map<String, Object> error = new HashMap<>();
					error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
					error.put("message", ex.getMessage());
					response.setErrorResult(new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR));
					return null;
				});

		return response;
	}
}