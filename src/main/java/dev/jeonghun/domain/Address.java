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
public class Address {
    @Column(length = 1000)
    private String address;
    @Column(length = 20)
    private String zipcode;

    @Builder
    public Address(String address, String zipcode) {
        this.address = address;
        this.zipcode = zipcode;
    }
}
