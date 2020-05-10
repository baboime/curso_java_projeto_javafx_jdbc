package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartamentoDao;
import model.entidades.Departamento;

public class DepartamentoDaoJDBC implements DepartamentoDao {
	
	private Connection conexao;
	
	public DepartamentoDaoJDBC(Connection conexao) {
		this.conexao = conexao;
	}

	@Override
	public void inserir(Departamento obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conexao.prepareStatement(
					"INSERT INTO departamento "
					+ "(Nome) "
					+ "VALUES "
					+ "(?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			
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
				throw new DbException("Erro inesperado: Linha nao foi inserida na tabela departamento");
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
	public void atualizar(Departamento obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conexao.prepareStatement("UPDATE departamento SET Nome = ? WHERE Id = ?");
			
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());
			
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
			st = conexao.prepareStatement("DELETE FROM departamento WHERE Id = ?");
			
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
	public Departamento buscarPeloId(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs =null;
		
		try {
			st = conexao.prepareStatement(
					"SELECT departamento.* " 
					+ "FROM departamento "
					+ "WHERE departamento.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if (rs.next()) {
				Departamento dep = instanciarDepartamento(rs);
				return dep;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Departamento instanciarDepartamento(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("Id"));
		dep.setNome(rs.getString("Nome"));
		return dep;
	}

	@Override
	public List<Departamento> buscarTudo() {
		
		PreparedStatement st = null;
		ResultSet rs =null;
		
		try {
			
			st = conexao.prepareStatement("SELECT departamento.* from departamento ORDER BY Nome");
			
			rs = st.executeQuery();
			
			List<Departamento> listaDepartamento = new ArrayList<>();
			
			while (rs.next()) {
				Departamento dep = instanciarDepartamento(rs);
				listaDepartamento.add(dep);
			}
			return listaDepartamento;
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