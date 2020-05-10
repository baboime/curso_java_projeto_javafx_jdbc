package model.dao;

import java.util.List;

import model.entidades.Departamento;
import model.entidades.Vendedor;

public interface VendedorDao {
	
	void inserir(Vendedor obj);
	void atualizar(Vendedor obj);
	void excluirPeloId(Integer id);
	Vendedor buscarPeloId(Integer id);
	List<Vendedor> buscarTudo();
	List<Vendedor> buscarPeloDepartamento(Departamento departamento);
}
