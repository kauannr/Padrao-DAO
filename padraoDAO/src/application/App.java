package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class App {
    public static void main(String[] args) throws Exception {
        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== Teste pelo Id do vendedor ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("===Teste pelo departamento ===");
        List<Seller> listSeller = sellerDao.findByDepartment(new Department(2, null));
        for (Seller seller2 : listSeller) {
            System.out.println(seller2 + "\n");
        }

        System.out.println("===Teste todos os Sellers===");
        listSeller = sellerDao.findAll();
        for (Seller seller2 : listSeller) {
            System.out.println(seller2 + "\n");
        }

        



        
    }
}
