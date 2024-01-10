package dev.jeonghun.domain;

import dev.jeonghun.domain.common.BaseEntity;
import dev.jeonghun.domain.department.Department;
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
    private DeleteFlag deleted = DeleteFlag.N;

    @Builder
    private Member(Contact contact, Address address) {
        this.contact = contact;
        this.address = address;
    }

    public void changeContact(Contact contact) {
        this.contact = contact;
    }

    public void changeDepartment(Department department) {
        this.department = department;
    }
}
