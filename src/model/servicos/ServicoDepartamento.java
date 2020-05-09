package model.servicos;

import java.util.ArrayList;
import java.util.List;

import model.entidades.Departamento;

public class ServicoDepartamento {
	
	public List<Departamento> buscarTudo() {
		List<Departamento> listaDepartamento = new ArrayList<>();
		listaDepartamento.add(new Departamento(1, "Livros"));
		listaDepartamento.add(new Departamento(2, "Computadores"));
		listaDepartamento.add(new Departamento(3, "Eletrônicos"));
		return listaDepartamento;
	}
}
