package model.servicos;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartamentoDao;
import model.entidades.Departamento;

public class ServicoDepartamento {
	
	private DepartamentoDao dao = DaoFactory.criarDepartamentoDao();
	
	public List<Departamento> buscarTudo() {
		return dao.buscarTudo();
	}
}
