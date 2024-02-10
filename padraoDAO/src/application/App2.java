package application;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class App2 {
    public static void main(String[] args) throws Exception {
        DepartmentDao departmentDao = DaoFactory.createDepertmentDao();

        System.out.println("===TESTE INSERÇÃO DEPARTAMENTO===");
        Department department = new Department(5, "physique");
        // departmentDao.insert(department);

        System.out.println("===TESTE UPDATE DEPARTAMENTO===");
        department.setName("Physique");
        departmentDao.update(department);

        System.out.println("===TESTE DELEÇÃO DEPARTAMENTO===");
        // departmentDao.delete(8);

        System.out.println("===TESTE FINDBYID===");
        Department department2 = departmentDao.findById(5);
        System.out.println(department2);

        System.out.println("===TODOS OS DEPARTAMENTOS===");
        List<Department> list = new ArrayList<>();
        list = departmentDao.findAll();
        for (Department department3 : list) {
            System.out.println(department3);
        }
    }
}
