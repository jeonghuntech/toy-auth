package dev.jeonghun.domain;

import dev.jeonghun.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;

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
    private String name;

    private String phoneNumber;

    private String email;

    private String address;
    private String zipcode;

    @Column(length = 1, nullable = false)
    @ColumnDefault("'N'")
    @Enumerated(EnumType.STRING)
    private DeleteFlag deleted = DeleteFlag.N;

    @Builder
    public Member(String name, String phoneNumber, String email, String address, String zipcode) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.zipcode = zipcode;
    }
}
