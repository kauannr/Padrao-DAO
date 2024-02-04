package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
    public void insert(Department department);

    public void update(Department department);

    public void delete(Department department);

    public Department findById(Integer id);

    public List<Department> findAll();
}
