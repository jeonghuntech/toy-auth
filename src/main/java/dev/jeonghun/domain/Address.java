package dev.jeonghun.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public class Address {
	@Column(length = 1000)
	private String address;
	@Column(length = 20)
	private String zipcode;

	@Builder
	private Address(String address, String zipcode) {
		this.address = address;
		this.zipcode = zipcode;
	}
}
