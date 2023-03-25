package com.ecomteam.shop_dddv3.domain.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
	// private String type = "Bearer";
	private String id;
	private String username;
	private String email;
	private List<String> roles;

	private String message;
}
