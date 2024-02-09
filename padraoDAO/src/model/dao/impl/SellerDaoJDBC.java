package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DB.DB;
import DB.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
    private Connection conn = null;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            st = conn.prepareStatement("INSERT INTO seller"
                    + "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
                    + " VALUES "
                    + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());

            int linhasAfetadas = st.executeUpdate();
            if (linhasAfetadas > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int novoId = rs.getInt(1);
                    seller.setId(novoId);
                    System.out.println("Sucesso! Id gerado: " + novoId);
                }
            } else {
                throw new DbException("Falha na inserção, nenhuma linha afetada\n");
            }
            conn.commit();
        } catch (Exception e) {
            e.getMessage();
            try {
                conn.rollback();
                throw new DbException("Erro na inserção e Rollback ativado\n");
            } catch (SQLException e1) {
                throw new DbException("Erro ao tentar desfazer com rollback " + e1.getMessage());
            }

        } finally {
            DB.closeResultSet(rs);
            DB.closeStatemet(st);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);
            st = conn.prepareStatement("UPDATE seller "
                    + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                    + "WHERE Id = ?");

            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());

            int linhasAfetadas = st.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Sucesso! linhas alteradas: " + linhasAfetadas);
            } else {
                throw new DbException("Erro na atualização, nenhuma linha afetada\n");
            }
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            DB.closeStatemet(st);
        }

    }

    @Override
    public void delete(Integer id) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);
            st = conn.prepareStatement("DELETE FROM seller WHERE id = ?");
            st.setInt(1, id);

            int linhasAfetadas = st.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Sucesso! seller deletado. Linhas afetadas: " + linhasAfetadas);
            } else {
                throw new DbException("Erro na deleção e nenhuma linhas afetada");
            }
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
                System.out.println(e.getMessage() + ". Rollback executado");
            } catch (SQLException e1) {
                throw new DbException("Erro ao voltar com rollback");
            }
        } finally {
            DB.closeStatemet(st);
        }
    }

    @Override
    public Seller findById(Integer id) {

        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT seller.*, department.Name as DepName"
                    + " FROM seller INNER JOIN department"
                    + " ON seller.DepartmentId = department.Id"
                    + " WHERE seller.Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                Seller seller = instantiateSeller(rs, dep);
                return seller;
            }
            return null;
        } catch (Exception e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatemet(st);
            DB.closeResultSet(rs);
        }

    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDate(rs.getDate("BirthDate"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDepartment(dep);
        return seller;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT seller.*, department.Name as DepName"
                    + " FROM seller INNER JOIN department"
                    + " ON seller.DepartmentId = department.Id"
                    + " WHERE DepartmentId = ?"
                    + " ORDER BY Name");
            st.setInt(1, department.getId());
            rs = st.executeQuery();

            List<Seller> listSeller = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(rs, dep);
                listSeller.add(seller);
            }
            return listSeller;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatemet(st);
        }
        return null;

    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT seller.*, department.Name as DepName"
                    + " FROM seller INNER JOIN department"
                    + " ON seller.DepartmentId = department.Id"
                    + " ORDER BY Id");
            rs = st.executeQuery();

            List<Seller> listSeller = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }
                Seller seller = instantiateSeller(rs, dep);
                listSeller.add(seller);
            }
            return listSeller;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatemet(st);
        }
        return null;
    }

}
