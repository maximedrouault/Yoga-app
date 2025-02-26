package com.openclassrooms.starterjwt.integrations;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SpringBootSecurityJwtApplicationIT {

	@Test
	public void contextLoads() {
	}

}