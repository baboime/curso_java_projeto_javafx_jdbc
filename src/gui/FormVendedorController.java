package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.ListenerAlteracaoDeDados;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entidades.Departamento;
import model.entidades.Vendedor;
import model.exceptions.ValidacaoException;
import model.servicos.ServicoDepartamento;
import model.servicos.ServicoVendedor;

public class FormVendedorController implements Initializable {

	private Vendedor entidade;

	private ServicoVendedor servico;

	private ServicoDepartamento servicoDepartamento;

	private List<ListenerAlteracaoDeDados> listenersAlteracaoDeDados = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpDataDeNascimento;

	@FXML
	private TextField txtSalarioBase;

	@FXML
	private ComboBox<Departamento> comboBoxDepartamento;

	@FXML
	private Label labelMensagemDeErroNome;

	@FXML
	private Label labelMensagemDeErroEmail;

	@FXML
	private Label labelMensagemDeErroDataDeNascimento;

	@FXML
	private Label labelMensagemDeErroSalarioBase;

	@FXML
	private Button btSalvar;

	@FXML
	private Button btCancelar;

	@FXML
	private ObservableList<Departamento> obsLista;

	public void setVendedor(Vendedor entidade) {
		this.entidade = entidade;
	}

	public void setServicos(ServicoVendedor servico, ServicoDepartamento servicoDepartamento) {
		this.servico = servico;
		this.servicoDepartamento = servicoDepartamento;
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
		} catch (DbException e) {
			Alerts.showAlert("Erro ao atualizar objeto", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidacaoException e) {
			setMensagensDeErro(e.getErros());
		}
	}

	private void notificarListenersAlteracaoDeDados() {
		for (ListenerAlteracaoDeDados listener : listenersAlteracaoDeDados) {
			listener.quandoHouverAlteracaoDeDados();
		}
	}

	private Vendedor getDadosDoForm() {
		Vendedor obj = new Vendedor();

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
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldDouble(txtSalarioBase);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatarDatePicker(dpDataDeNascimento, "dd/MM/yyyy");
		inicializarComboBoxDepartmento();
	}

	public void atualizarFormData() {
		if (entidade == null) {
			throw new IllegalStateException("Entidade está nula");
		}
		txtId.setText(String.valueOf(entidade.getId()));
		txtNome.setText(entidade.getNome());
		txtEmail.setText(entidade.getEmail());
		Locale.setDefault(Locale.US);
		txtSalarioBase.setText(String.format("%.2f", entidade.getSalarioBase()));
		if (entidade.getDataDeNascimento() != null) {
			dpDataDeNascimento
					.setValue(LocalDate.ofInstant(entidade.getDataDeNascimento().toInstant(), ZoneId.systemDefault()));
		}
		
		if (entidade.getDepartamento() == null) {
			comboBoxDepartamento.getSelectionModel().selectFirst();
		}
		else {
			comboBoxDepartamento.setValue(entidade.getDepartamento());
		}
	}

	public void carregarObjetosAssociados() {
		if (servicoDepartamento == null) {
			throw new IllegalStateException("Serviço do Departamento está vazio");
		}
		List<Departamento> lista = servicoDepartamento.buscarTudo();
		obsLista = FXCollections.observableArrayList(lista);
		comboBoxDepartamento.setItems(obsLista);
	}

	private void setMensagensDeErro(Map<String, String> erros) {
		Set<String> campos = erros.keySet();

		if (campos.contains("nome")) {
			labelMensagemDeErroNome.setText(erros.get("nome"));
		}
	}

	private void inicializarComboBoxDepartmento() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean vazio) {
				super.updateItem(item, vazio);
				setText(vazio ? "" : item.getNome());
			}
		};
		comboBoxDepartamento.setCellFactory(factory);
		comboBoxDepartamento.setButtonCell(factory.call(null));
	}

}
