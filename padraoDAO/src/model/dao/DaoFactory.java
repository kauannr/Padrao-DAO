package model.dao;

import DB.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDaoJDBC createSellerDao() {
        return new SellerDaoJDBC(DB.getconnection());
    }

    public static DepartmentDaoJDBC createDepertmentDao() {
        return new DepartmentDaoJDBC(DB.getconnection());
    }
}
