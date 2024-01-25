package dev.jeonghun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.jeonghun.domain.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
