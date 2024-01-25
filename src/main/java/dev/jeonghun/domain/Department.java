package dev.jeonghun.domain;

import static jakarta.persistence.FetchType.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.BatchSize;

import dev.jeonghun.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Department extends BaseEntity {

	public static final String TOP_DEPARTMENT_NAME = "TOP";

	@Transient
	public static final Department EMPTY = null;

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
		return getClass().getSimpleName() + "(id = " + id + ", name = " + name + ")";
	}
}
