package dev.jeonghun.domain;

import dev.jeonghun.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE user_id = ?")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String phoneNumber;

    private String email;

    private String address;
    private String zipcode;

    @Column(columnDefinition = "boolean not null default false")
    private boolean deleted;
}
