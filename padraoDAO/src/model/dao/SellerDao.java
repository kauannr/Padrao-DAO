package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {
    public void insert(Seller seller);
    public void update(Seller seller);
    public void delete(Seller seller);
    public Seller findId(Integer id);
    public List<Seller> findAll();
}
