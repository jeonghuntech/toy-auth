package dev.jeonghun.marker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Retention(RetentionPolicy.RUNTIME)
@Import({RestAssuredTest.SetUpRestAssured.class})
public @interface RestAssuredTest {

	@TestComponent
	class SetUpRestAssured implements ApplicationListener<ServletWebServerInitializedEvent> {

		@Override
		public void onApplicationEvent(ServletWebServerInitializedEvent event) {
			RestAssured.port = event.getWebServer().getPort();
		}
	}
}
