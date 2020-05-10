package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.ListenerAlteracaoDeDados;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entidades.Departamento;
import model.exceptions.ValidacaoException;
import model.servicos.ServicoDepartamento;

public class FormDepartamentoController implements Initializable {
	
	private Departamento entidade;
	
	private ServicoDepartamento servico;
	
	private List<ListenerAlteracaoDeDados> listenersAlteracaoDeDados = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private Label labelMensagemDeErro;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	public void setDepartamento(Departamento entidade) {
		this.entidade = entidade;
	}
	
	public void setServicoDepartamento(ServicoDepartamento servico) {
		this.servico = servico;
	}
	
	public void acionarListenerAlteracaoDeDados(ListenerAlteracaoDeDados listener) {
		listenersAlteracaoDeDados.add(listener);
	}
	
	@FXML
	public void onBtSalvarAcao(ActionEvent evento) {
		if (entidade == null) {
			throw new IllegalStateException("Entidade está nula");
		}
		if (servico == null) {
			throw new IllegalStateException("Servico está nulo");
		}
		try {
			entidade = getDadosDoForm();
			servico.salvarOuAtualizar(entidade);
			notificarListenersAlteracaoDeDados();
			Utils.stageAtual(evento).close();
		}
		catch (DbException e) {
			Alerts.showAlert("Erro ao atualizar objeto", null, e.getMessage(), AlertType.ERROR);
		}
		catch (ValidacaoException e) {
			setMensagensDeErro(e.getErros());
		}
	}
	
	private void notificarListenersAlteracaoDeDados() {
		for (ListenerAlteracaoDeDados listener : listenersAlteracaoDeDados) {
			listener.quandoHouverAlteracaoDeDados();
		}
	}

	private Departamento getDadosDoForm() {
		Departamento obj = new Departamento();
		
		ValidacaoException excecao = new ValidacaoException("Erro de Validação");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtNome.getText() == null || txtNome.getText().trim().equalsIgnoreCase("")) {
			excecao.adicionarErro("nome", "Nome deve ser informado");
		}
		
		obj.setNome(txtNome.getText());
		
		if (excecao.getErros().size() > 0) {
			throw excecao;
		}
		
		return obj;
	}

	@FXML
	public void onBtCancelarAcao(ActionEvent evento) {
		Utils.stageAtual(evento).close();
	}
 
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();
	}
	
	private void inicializarNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
	}
	
	public void atualizarFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade está nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
	}
	
	private void setMensagensDeErro(Map<String, String> erros) {
		Set<String> campos = erros.keySet();
		
		if (campos.contains("nome")) {
			labelMensagemDeErro.setText(erros.get("nome"));
		}
	}
	
}
