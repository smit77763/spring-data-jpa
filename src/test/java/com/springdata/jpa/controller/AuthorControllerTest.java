package com.springdata.jpa.controller;

import com.springdata.jpa.models.Author;
import com.springdata.jpa.repository.AuthorRepository;
import com.springdata.jpa.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AuthorRepository authorRepository;

	@MockitoBean
	private AuthorService authorService;

	@Test
	public void testGetAuthors() throws Exception {
		List<Author> authors = Arrays.asList(
				new Author(1, "Smit", "Shah", "smit.shah@example.com", 30, null),
				new Author(2, "Harsh", "Patel", "harsh.patel@example.com", 25, null)
		);

		Mockito.when(authorService.getAllAuthors()).thenReturn(authors);

		mockMvc.perform(get("/api/authors/all"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].firstName").value("Smit"))
				.andExpect(jsonPath("$[1].firstName").value("Harsh"));
	}

	@Test
	public void testAddAuthor() throws Exception {
		Author author = new Author(1, "Smit", "Shah", "smit.shah@digite.com", 22, null);

		Mockito.when(authorService.addAuthor(any(Author.class))).thenReturn(author);

		mockMvc.perform(post("/api/authors/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"firstName\":\"Smit\",\"lastName\":\"Shah\",\"email\":\"smit.shah@digite.com\",\"age\":22}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName").value("Smit"));
	}
	
	@Test
	public void testAddMultipleAuthor() throws Exception {
		List<Author> authors = Arrays.asList(
				new Author(1, "Sayan", "Maiti", "sayan.maiti@example.com", 30, null),
				new Author(2, "Vinil", "Rathod", "vinil.rathod@example.com", 25, null)
		);

		Mockito.when(authorService.addMultipleAuthors(any(List.class))).thenReturn(authors);

		mockMvc.perform(post("/api/authors/addMultiple")
						.contentType(MediaType.APPLICATION_JSON)
						.content("[{\"firstName\":\"Sayan\",\"lastName\":\"Maiti\",\"email\":\"sayan.maiti@example.com\",\"age\":30},{\"firstName\":\"Vinil\",\"lastName\":\"Rathod\",\"email\":\"vinil.rathod@example.com\",\"age\":25}]"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value(200))
				.andExpect(jsonPath("$.message").value("Authors Added Successfully"))
				.andExpect(jsonPath("$.data[0].firstName").value("Sayan"))
				.andExpect(jsonPath("$.data[1].firstName").value("Vinil"))
				.andExpect(jsonPath("$.count").value(2));
	}
	
	@Test
	public void testDeleteAuthorById() throws Exception {
		Mockito.doNothing().when(authorService).deleteAuthorById(anyInt());

		mockMvc.perform(delete("/api/authors/delete/1"))
				.andExpect(status().isOk());
	}

	@Test
	public void testUpdateAuthorEmail() throws Exception {
		Mockito.doNothing().when(authorService).updateAuthorEmail(any(String.class), anyInt());

		mockMvc.perform(put("/api/authors/update/1")
						.param("email", "new.mail@mail.com"))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteAllAuthors() throws Exception {
		Mockito.doNothing().when(authorService).deleteAllAuthors();

		mockMvc.perform(delete("/api/authors/deleteAll"))
				.andExpect(status().isOk());
	}
}