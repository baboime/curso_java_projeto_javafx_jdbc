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

import db.DB;
import db.DbException;
import model.dao.VendedorDao;
import model.entidades.Departamento;
import model.entidades.Vendedor;

public class VendedorDaoJDBC implements VendedorDao{
	
	private Connection conexao;
	
	public VendedorDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Vendedor obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conexao.prepareStatement(
					"INSERT INTO vendedor "
		            + "(Nome, Email, DataDeNascimento, SalarioBase, IdDepartamento) "
		            + "VALUES "
     		        + "(?, ?, ?, ?, ?)",
     		        Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataDeNascimento().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId());
			
			int linhasInseridas = st.executeUpdate();
			
			if (linhasInseridas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Erro inesperado: Linha não foi inserida na tabela");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);		
		}
	}

	@Override
	public void atualizar(Vendedor obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conexao.prepareStatement(
					"UPDATE vendedor "
					+ "SET Nome = ?, Email = ?, DataDeNascimento = ?, SalarioBase = ?, IdDepartamento = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataDeNascimento().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);		
		}
		
	}

	@Override
	public void excluirPeloId(Integer id) {
		
		PreparedStatement st = null;
		
		try {
			st = conexao.prepareStatement("DELETE FROM vendedor WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Vendedor buscarPeloId(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conexao.prepareStatement(
					"SELECT vendedor.*,departamento.Nome as NomeDoDepartamento "
					+ "FROM vendedor INNER JOIN departamento "
					+ "ON vendedor.IdDepartamento = departamento.Id "
					+ "WHERE vendedor.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if (rs.next()) {
				Departamento dep = instanciarDepartamento(rs);
				Vendedor obj = instanciarVendedor(rs, dep);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	private Vendedor instanciarVendedor(ResultSet rs, Departamento dep) throws SQLException {
		Vendedor obj = new Vendedor();
		obj.setId(rs.getInt("Id"));
		obj.setNome(rs.getString("Nome"));
		obj.setEmail(rs.getString("Email"));
		obj.setDataDeNascimento(rs.getDate("DataDeNascimento"));
		obj.setSalarioBase(rs.getDouble("SalarioBase"));
		obj.setDepartamento(dep);
		return obj;
	}

	private Departamento instanciarDepartamento(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("IdDepartamento"));
		dep.setNome(rs.getString("NomeDoDepartamento"));
		return dep;
	}

	@Override
	public List<Vendedor> buscarTudo() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conexao.prepareStatement(
					"SELECT vendedor.*,departamento.Nome as NomeDoDepartamento "
					+ "FROM vendedor INNER JOIN departamento "
					+ "ON vendedor.IdDepartamento = departamento.Id "
					+ "ORDER BY Nome");
			
			rs = st.executeQuery();
			
			List<Vendedor> listaVendedor = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();
			
			while (rs.next()) {
				
				Departamento dep = map.get(rs.getInt("IdDepartamento"));
				
				if (dep == null) {
					dep = instanciarDepartamento(rs);
					map.put(rs.getInt("IdDepartamento"), dep);
				}
				
				Vendedor obj = instanciarVendedor(rs, dep);
				listaVendedor.add(obj);
			}
			return listaVendedor;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Vendedor> buscarPeloDepartamento(Departamento departamento) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conexao.prepareStatement(
					"SELECT vendedor.*,departamento.Nome as NomeDoDepartamento "
					+ "FROM vendedor INNER JOIN departamento "
					+ "ON vendedor.IdDepartamento = departamento.Id "
					+ "WHERE IdDepartamento = ? "
					+ "ORDER BY Nome");
			
			st.setInt(1, departamento.getId());
			
			rs = st.executeQuery();
			
			List<Vendedor> listaVendedor = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();
			
			while (rs.next()) {
				
				Departamento dep = map.get(rs.getInt("IdDepartamento"));
				
				if (dep == null) {
					dep = instanciarDepartamento(rs);
					map.put(rs.getInt("IdDepartamento"), dep);
				}
				
				Vendedor obj = instanciarVendedor(rs, dep);
				listaVendedor.add(obj);
			}
			return listaVendedor;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
}
