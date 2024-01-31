package dev.jeonghun.marker;

import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ExtendWith(RestDocumentationExtension.class)
public class RestDocsTest {

	@LocalServerPort
	private int port;

	protected RequestSpecification spec;

	@BeforeEach
	void setUp(RestDocumentationContextProvider restDocumentation) {
		RestAssured.port = port;
		this.spec = new RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation))
			.build();
	}
}
