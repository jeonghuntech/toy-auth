package dev.jeonghun.controller;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dev.jeonghun.domain.MemberFixture;
import dev.jeonghun.marker.RestDocsTest;
import dev.jeonghun.service.MemberService;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class MemberControllerTest extends RestDocsTest {

	@Autowired
	MemberService memberService;

	@BeforeEach
	void setup() {
		멤버_목록_저장();
	}

	void 멤버_목록_저장() {
		memberService.saveAll(Arrays.asList(
			MemberFixture.KIM.member(),
			MemberFixture.LEE.member(),
			MemberFixture.PARK.member(),
			MemberFixture.CHOI.member()
		));
	}

	@Test
	void 멤버_목록을_페이징_처리하여_조회한다() {
		ExtractableResponse<Response> response =
			RestAssured
				.given(spec).log().all()
				.filter(document("멤버_목록을_페이징_처리하여_조회한다",
					queryParameters(
						parameterWithName("page").description("요청 페이지"),
						parameterWithName("size").description("보여질 항목 수"),
						parameterWithName("sort").description("정렬")
					),
					responseFields(
						fieldWithPath("data[].id").description("식별자"),
						fieldWithPath("data[].age").description("아이디"),
						fieldWithPath("data[].name").description("나이"),
						fieldWithPath("data[].phoneNumber").description("전화번호"),
						fieldWithPath("data[].email").description("이메일"),

						fieldWithPath("pageInfo.count").description("결과 수"),
						fieldWithPath("pageInfo.totalCount").description("요청 크기"),
						fieldWithPath("pageInfo.perPage").description("보여질 항목 수"),
						fieldWithPath("pageInfo.page").description("요청 페이지"),
						fieldWithPath("pageInfo.totalPage").description("전체 페이지 수")
					)
				))
				.when().get("/member/list?page={page}&size={size}&sort={sort}", 0, 3, "id,desc")
				.then().log().all().extract();

		List<String> memberDtoList = response.jsonPath().getList("data.name", String.class);
		List<String> expectedList = Stream.of(
				MemberFixture.CHOI.member(),
				MemberFixture.PARK.member(),
				MemberFixture.LEE.member()
			)
			.map(member -> member.getContact().getName())
			.toList();

		Assertions.assertThat(expectedList).containsExactlyElementsOf(memberDtoList);
	}
}
