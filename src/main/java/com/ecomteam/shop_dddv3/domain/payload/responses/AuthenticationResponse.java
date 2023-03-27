package com.ecomteam.shop_dddv3.domain.payload.responses;

import com.ecomteam.shop_dddv3.domain.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
	// private String type = "Bearer";
	private String id;
	private String username;
	private String email;

	private Role role;

}
