package dev.jeonghun.domain;

public enum DepartmentFixture {
	ROOT_TEAM(Department.TOP_DEPARTMENT_NAME, Department.EMPTY),
	A_TEAM("A Team", ROOT_TEAM.department()),
	B_TEAM("B Team", ROOT_TEAM.department());

	private final String name;
	private final Department parent;

	DepartmentFixture(String name, Department parent) {
		this.name = name;
		this.parent = parent;
	}

	public Department department() {
		return Department.builder()
			.name(name)
			.parent(parent)
			.build();
	}

	public static Department newDepartment(String name) {
		return newDepartment(name, Department.EMPTY);
	}

	public static Department newDepartment(String name, Department department) {
		return Department.builder()
			.name(name)
			.parent(department)
			.build();
	}
}
