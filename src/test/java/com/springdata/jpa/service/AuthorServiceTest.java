package com.springdata.jpa.service;

import com.springdata.jpa.repository.AuthorRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuthorService.class, AuthorRepository.class})
public class AuthorServiceTest {
	
	@MockBean
	private AuthorRepository authorRepository;
	@MockBean
	private StreamBridge streamBridge;

	@Autowired
	private AuthorService authorService;
	
	@Test
	public void testSendAuthorMessage() {
		JSONObject payload = new JSONObject();
		try {
			payload.put("message", "Author message");
			payload.put("info", "info");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		when(streamBridge.send("authorMessage-out-0", payload.toString())).thenReturn(true);

		authorService.sendAuthorMessage("Author message", "info");
		verify(streamBridge, times(1)).send("createLog-out", payload.toString());
	}
	
}
