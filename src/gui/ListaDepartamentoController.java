package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbException;
import gui.listeners.ListenerAlteracaoDeDados;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidades.Departamento;
import model.servicos.ServicoDepartamento;

public class ListaDepartamentoController implements Initializable, ListenerAlteracaoDeDados {

	private ServicoDepartamento servico;

	@FXML
	private TableView<Departamento> tableViewDepartamento;

	@FXML
	private TableColumn<Departamento, Integer> tableColumnId;

	@FXML
	private TableColumn<Departamento, String> tableColumnNome;

	@FXML
	private TableColumn<Departamento, Departamento> tableColumnEditar;

	@FXML
	private TableColumn<Departamento, Departamento> tableColumnExcluir;

	@FXML
	private Button btNovo;

	private ObservableList<Departamento> obsLista;

	@FXML
	public void onBtNovoAcao(ActionEvent evento) {
		Stage stagePai = gui.util.Utils.stageAtual(evento);
		Departamento obj = new Departamento();
		criarDialogForm(obj, "/gui/FormDepartamento.fxml", stagePai);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();
	}

	public void setServicoDepartamento(ServicoDepartamento servico) {
		this.servico = servico;
	}

	private void inicializarNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartamento.prefHeightProperty().bind(stage.heightProperty());
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("Servico est� nulo");
		}
		List<Departamento> listaDepartamento = servico.buscarTudo();
		obsLista = FXCollections.observableArrayList(listaDepartamento);
		tableViewDepartamento.setItems(obsLista);
		inicializarBotaoDeEdicao();
		inicializarBotaoDeExclusao();
	}

	private void criarDialogForm(Departamento obj, String nomeAbsoluto, Stage stagePai) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane painel = loader.load();

			FormDepartamentoController controlador = loader.getController();
			controlador.setDepartamento(obj);
			controlador.setServicoDepartamento(new ServicoDepartamento());
			controlador.acionarListenerAlteracaoDeDados(this);
			controlador.atualizarFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados Departamento");
			dialogStage.setScene(new Scene(painel));
			dialogStage.setResizable(false);
			dialogStage.initOwner(stagePai);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro ao carregar a View", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void quandoHouverAlteracaoDeDados() {
		atualizarTableView();
	}

	private void inicializarBotaoDeEdicao() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button botao = new Button("Editar");

			@Override
			protected void updateItem(Departamento obj, boolean vazio) {
				super.updateItem(obj, vazio);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(botao);
				botao.setOnAction(
						evento -> criarDialogForm(obj, "/gui/FormDepartamento.fxml", Utils.stageAtual(evento)));
			}
		});
	}

	private void inicializarBotaoDeExclusao() {
		tableColumnExcluir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnExcluir.setCellFactory(param -> new TableCell<Departamento, Departamento>() {
			private final Button botao = new Button("Excluir");

			@Override
			protected void updateItem(Departamento obj, boolean vazio) {
				super.updateItem(obj, vazio);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(botao);
				botao.setOnAction(event -> excluirEntidade(obj));
			}
		});
	}

	private void excluirEntidade(Departamento obj) {
		Optional<ButtonType> resultado = Alerts.solicitarConfirmacao("Confirma��o", "Tem certeza que seja excluir?");
		
		if (resultado.get() == ButtonType.OK) {
			if (servico == null) {
				throw new IllegalStateException("Servi�o est� nulo");
			}
			try {
				servico.remover(obj);
				atualizarTableView();
			}
			catch (DbException e) {
				Alerts.showAlert("Erro ao excluir o objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}
