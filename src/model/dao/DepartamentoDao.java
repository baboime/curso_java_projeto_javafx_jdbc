package model.dao;

import java.util.List;

import model.entidades.Departamento;

public interface DepartamentoDao {
	
	void inserir(Departamento obj);
	void atualizar(Departamento obj);
	void excluirPeloId(Integer id);
	Departamento buscarPeloId(Integer id);
	List<Departamento> buscarTudo();

}
