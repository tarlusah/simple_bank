package com.vinra.simple_banking.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
	private String message;
	private List<String> errors;
}
