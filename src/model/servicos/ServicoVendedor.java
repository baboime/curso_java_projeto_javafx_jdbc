package model.servicos;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.VendedorDao;
import model.entidades.Vendedor;

public class ServicoVendedor {
	
	private VendedorDao dao = DaoFactory.criarVendedorDao();
	
	public List<Vendedor> buscarTudo() {
		return dao.buscarTudo();
	}
	
	public void salvarOuAtualizar(Vendedor obj) {
		if (obj.getId() == null) {
			dao.inserir(obj);
		}
		else {
			dao.atualizar(obj);
		}
	}
	
	public void remover(Vendedor obj) {
		dao.excluirPeloId(obj.getId());
	}
}
