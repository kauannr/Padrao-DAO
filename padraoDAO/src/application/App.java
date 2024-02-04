package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Department;
import model.entities.Seller;

public class App {
    public static void main(String[] args) throws Exception {
        Department department = new Department(1, "Computers");
        Seller seller = new Seller(1, "Dudu", "dudu@gmail.com", new Date(), 1500d, department);
        System.out.println(seller);

        SellerDao seller1 = DaoFactory.createSellerDao();
    }
}
