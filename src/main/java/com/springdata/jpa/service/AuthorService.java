package com.springdata.jpa.service;

import com.springdata.jpa.models.Author;
import com.springdata.jpa.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorService {
	
	@Autowired
	private AuthorRepository authorRepository;
	
	public List<Author> getAllAuthors() {
		return authorRepository.findAll();
	}
	
	public Author addAuthor(Author author) {
		return authorRepository.save(author);
	}
	
	public List<Author> addMultipleAuthors(List<Author> authors) {
		return authorRepository.saveAll(authors);
	}
	
	public void updateAuthorEmail(String email, int id) {
		authorRepository.updateAuthorEmail(email, id);
	}
	
	public void deleteAllAuthors() {
		authorRepository.deleteAll();
//		authorRepository.deleteAll();
	}
	
	public void deleteAuthorById(int id) {
		authorRepository.deleteById(id);
	}
	
}