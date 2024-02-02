package application;

import model.entities.Department;

public class App {
    public static void main(String[] args) throws Exception {
        Department department = new Department(1, "Computers");
        System.out.println(department);
    }
}
