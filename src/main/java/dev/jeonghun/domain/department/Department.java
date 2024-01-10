package dev.jeonghun.domain.department;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id", "name"})
@EqualsAndHashCode(of = {"id", "name"})
public class Department {

    public static final String TOP_DEPARTMENT_NAME = "TOP";

    @Id
    @GeneratedValue
    @Column(name = "deparment_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

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

    public void changeParent(Department parent) {
        removeChildFromPreviousParent();
        addChildFromNewParent(parent);
        this.parent = parent;
    }

    private void removeChildFromPreviousParent() {
        if (parent != null) {
            parent.removeChild(this);
        }
    }

    private void removeChild(Department child) {
        childs.remove(child);
    }

    private void addChildFromNewParent(Department parent) {
        if (parent != null) {
            parent.addChild(this);
        }
    }

    private void addChild(Department child) {
        childs.add(child);
    }
}
