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

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 50)
    private String email;

    @Embedded
    private Address address;

    @Column(length = 1, nullable = false)
    @ColumnDefault("'N'")
    @Enumerated(EnumType.STRING)
    private DeleteFlag deleted = DeleteFlag.N;

    @Builder
    public Member(String name, String phoneNumber, String email, Address address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }
}
