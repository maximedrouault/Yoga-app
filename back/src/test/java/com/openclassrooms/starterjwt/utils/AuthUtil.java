package com.openclassrooms.starterjwt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
public class AuthUtil {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    public String obtainAccessToken(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(username);
        loginRequest.setPassword(password);

        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtResponse jwtResponse = objectMapper.readValue(response, JwtResponse.class);

        return jwtResponse.getToken();
    }

    public String obtainAdminJwtToken() throws Exception {
        return obtainAccessToken("yoga@studio.com", "test!1234");
    }

    public String obtainNonAdminJwtToken() throws Exception {
        return obtainAccessToken("john.doe@example.com", "password123");
    }
}
