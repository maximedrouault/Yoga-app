package com.openclassrooms.starterjwt.integrations;

import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles("test")
public class SpringBootSecurityJwtApplicationIT {

	@Test
	public void contextLoads() {
	}

	@Test
	void main_shouldLoadApplicationContextWithoutError() {
		assertDoesNotThrow(() -> SpringBootSecurityJwtApplication.main(new String[] {}));
	}
}