package model.dao.impl;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DB.DB;
import DB.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection conn = null;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            st = conn.prepareStatement("INSERT INTO department"
                    + "(Name)"
                    + " VALUES "
                    + "(?)", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, department.getName());

            int linhasAfetadas = st.executeUpdate();
            if (linhasAfetadas > 0) {
                rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int novoId = rs.getInt(1);
                    department.setId(novoId); // Já atribuir à instancia do department o id do banco
                    System.out.println("Sucesso! Id gerado: " + novoId);
                }
                conn.commit();
            } else {
                throw new DbException("Erro na inserção. Nenhuma linha afetada");
            }
        } catch (Exception e) {
            try {
                conn.rollback();
                System.out.println(e.getMessage() + ". Rollback ativado");
            } catch (SQLException e1) {
                throw new DbException("Erro no rollback");
            }
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatemet(st);
        }
    }

    @Override
    public void update(Department department) {
        // TODO Auto-generated method stub
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);
            st = conn.prepareStatement("UPDATE department"
                    + " SET Name = ?"
                    + " WHERE id = ?");

            st.setString(1, department.getName());
            st.setInt(2, department.getId());
            int linhasAfetadas = st.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Sucesso na atualização! linhas afetadas: " + linhasAfetadas);
            } else {
                throw new DbException("Erro na atualização e nenhuma linha afetada");
            }
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
                System.out.println(e.getMessage() + ". Rollback ativado");
            } catch (SQLException e1) {
                throw new DbException("Erro no rollback");
            }
        } finally {
            DB.closeStatemet(st);
        }
    }

    @Override
    public void delete(Integer id) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);
            st = conn.prepareStatement("DELETE FROM department WHERE id = ?");
            st.setInt(1, id);

            int linhasAfetadas = st.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Sucesso na deleção! linhas afetadas: " + linhasAfetadas);
            } else {
                throw new DbException("Erro na deleção");
            }
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
                System.out.println(e.getMessage() + ". Rollback ativado");
            } catch (SQLException e1) {
                throw new DbException("Erro no rollback");
            }
        } finally {
            DB.closeStatemet(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            st = conn.prepareStatement("SELECT * FROM department WHERE Id = ?");
            st.setInt(1, id);

            rs = st.executeQuery();
            Map<Integer, Department> mapDepartment = new HashMap<>();
            if (rs.next()) {

                Department dep = mapDepartment.get(rs.getInt("Id"));
                if (dep == null) {
                    dep = instantiateDepartment(rs);
                    mapDepartment.put(rs.getInt("Id"), dep);
                    return dep;
                }
            } else {
                throw new DbException("Id não encontrado.");
            }
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
                System.out.println(e.getMessage() + ". Rollbac ativado");
                e.printStackTrace();
            } catch (SQLException e1) {
                throw new DbException("Erro no rollback");
            }
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatemet(st);
        }
        return null;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department(rs.getInt("Id"), rs.getString("Name"));
        return dep;
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM department ORDER BY id");

            rs = st.executeQuery();
            List<Department> listaDepartments = new ArrayList<>();
            while (rs.next()) {
                Department department = instantiateDepartment(rs);
                listaDepartments.add(department);
            } 
            return listaDepartments;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatemet(st);
        }

        return null;
    }

}
