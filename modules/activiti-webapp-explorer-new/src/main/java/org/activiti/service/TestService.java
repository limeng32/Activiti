package org.activiti.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {
	private String word = "a";

	public String getWord() {
		return word;
	}

}
