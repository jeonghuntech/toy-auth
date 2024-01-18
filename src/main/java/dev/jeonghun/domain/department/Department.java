package dev.jeonghun.domain.department;

import dev.jeonghun.domain.Member;
import dev.jeonghun.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Department extends BaseEntity {

    public static final String TOP_DEPARTMENT_NAME = "TOP";

    @Id
    @GeneratedValue
    @Column(name = "department_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @BatchSize(size = 200)
    @OneToMany(mappedBy = "department")
    private List<Member> members = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Department parent;

    @OneToMany(mappedBy = "parent")
    private List<Department> childs = new ArrayList<>();

    @Builder
    private Department(String name, Department parent) {
        this.name = name;
        changeParent(parent);
    }

    public void addChild(Department child) {
        child.changeParent(this);
    }

    public void changeParent(Department parent) {
        removeChildFromPreviousParent();
        addChildFromNewParent(parent);
        this.parent = parent;
    }

    private void removeChildFromPreviousParent() {
        if (parent != null) {
            parent.childs.remove(this);
        }
    }

    private void addChildFromNewParent(Department parent) {
        if (parent != null) {
            parent.childs.add(this);
        }
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public void removeMember(Member member) {
        members.remove(member);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ")";
    }
}
