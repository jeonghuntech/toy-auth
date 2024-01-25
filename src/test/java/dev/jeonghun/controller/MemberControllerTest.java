package dev.jeonghun.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import dev.jeonghun.domain.MemberFixture;
import dev.jeonghun.service.MemberService;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class MemberControllerTest extends RestTest {

	@Autowired
	MemberService memberService;

	@BeforeEach
	void setup() {
		super.setUp();

		memberService.saveAll(Arrays.asList(
			MemberFixture.KIM.member(),
			MemberFixture.LEE.member(),
			MemberFixture.PARK.member(),
			MemberFixture.CHOI.member()
		));
	}

	@Test
	void 멤버_목록을_페이징_처리하여_조회한다() {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/member/list?page={page}&size={size}&sort={sort}", 0, 3, "id,desc")
			.then().log().all().extract();

		List<String> memberDtoList = response.jsonPath().getList("content.name", String.class);
		List<String> expectedList = Stream.of(
				MemberFixture.CHOI.member(),
				MemberFixture.PARK.member(),
				MemberFixture.LEE.member())
			.map(member -> member.getContact().getName())
			.toList();

		Assertions.assertThat(expectedList).containsExactlyElementsOf(memberDtoList);
	}
}
