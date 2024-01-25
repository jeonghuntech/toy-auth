package dev.jeonghun.common;

import dev.jeonghun.domain.Member;

import java.util.List;
import java.util.stream.IntStream;

public enum MemberFixture {

    KIM(10, "kim", "01011112222", "kim@naver.com", "busan", "1122"),
    LEE(20, "lee", "01033334444", "lee@naver.com", "seoul", "3344"),
    PARK(30, "park", "01055556666", "park@naver.com", "jeju", "5566"),
    CHOI(40, "choi", "01077778888", "choi@naver.com", "sejong", "7788"),
    ;

    private final int age;
    private final String name;
    private final String phoneNumber;
    private final String email;
    private final String address;
    private final String zipcode;


    MemberFixture(int age, String name, String phoneNumber, String email, String address, String zipcode) {
        this.age = age;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.zipcode = zipcode;
    }


    public Member member() {
        return Member.allBuilder()
                .age(age)
                .name(name)
                .phoneNumber(phoneNumber)
                .email(email)
                .addressDetail(address)
                .zipcode(zipcode)
                .build();
    }

    public static List<Member> newMemberList(String name, int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(index -> newMember(index, name + index))
                .toList();
    }

    public static Member newMember(int age, String name) {
        return Member.allBuilder()
                .age(age)
                .name(name)
                .phoneNumber("01012345678")
                .email("contact@contact.com")
                .addressDetail("아파트 1004동 1004호")
                .zipcode("1002-2")
                .build();
    }
}
