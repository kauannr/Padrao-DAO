package application;

import java.util.Date;
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

        /*
         * System.out.println("===Teste inserir novo Seller===");
         * Seller seller2 = new Seller(null, "josefa", "josefa@gmail.com",
         * new Date(), 2000d, new Department(2, null));
         * sellerDao.insert(seller2);
         */

        System.out.println("===Teste update Seller===");
        seller = sellerDao.findById(22);
        seller.setEmail("Brito@gmail.com");
        sellerDao.update(seller);
        System.out.println(seller);

        System.out.println("===Teste de deleção===");
        sellerDao.delete(23);
        sellerDao.delete(24);
        sellerDao.delete(25);
    }
}
