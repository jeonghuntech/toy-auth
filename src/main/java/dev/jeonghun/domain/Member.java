package dev.jeonghun.domain;

import java.util.Objects;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.proxy.HibernateProxy;

import dev.jeonghun.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@SQLDelete(sql = "UPDATE member SET deleted = 'Y' WHERE user_id = ?")
public class Member extends BaseEntity {
	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false)
	private int age;

	@Embedded
	private Contact contact;

	@Embedded
	private Address address;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	@ToString.Exclude
	private Department department;

	@Column(length = 1, nullable = false)
	@ColumnDefault("'N'")
	@Enumerated(EnumType.STRING)
	private final DeleteFlag deleted = DeleteFlag.N;

	@Builder(builderMethodName = "allBuilder")
	private Member(int age, String name, String phoneNumber, String email, String addressDetail, String zipcode) {
		this(
			age,
			Contact.builder()
				.name(name)
				.phoneNumber(phoneNumber)
				.email(email)
				.build(),
			Address.builder()
				.zipcode(zipcode)
				.address(addressDetail)
				.build()
		);
	}

	@Builder
	private Member(int age, Contact contact, Address address) {
		this.age = age;
		this.contact = contact;
		this.address = address;
	}

	public void changeContact(Contact contact) {
		this.contact = contact;
	}

	public void changeDepartment(Department department) {
		if (this.department != null) {
			this.department.removeMember(this);
		}
		department.addMember(this);
		this.department = department;
	}

	@Override
	public final boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		Class<?> oEffectiveClass =
			object instanceof HibernateProxy
				? ((HibernateProxy)object).getHibernateLazyInitializer().getPersistentClass()
				: object.getClass();
		Class<?> thisEffectiveClass =
			this instanceof HibernateProxy
				? ((HibernateProxy)this).getHibernateLazyInitializer().getPersistentClass() :
				this.getClass();
		if (thisEffectiveClass != oEffectiveClass) {
			return false;
		}
		Member member = (Member)object;
		return getId() != null && Objects.equals(getId(), member.getId());
	}

	@Override
	public final int hashCode() {
		return !(this instanceof HibernateProxy) ? getClass().hashCode() :
			((HibernateProxy)this).getHibernateLazyInitializer().getPersistentClass().hashCode();
	}
}
