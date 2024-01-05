package dev.jeonghun.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contact {
    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 50)
    private String email;

    @Builder
    private Contact(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
