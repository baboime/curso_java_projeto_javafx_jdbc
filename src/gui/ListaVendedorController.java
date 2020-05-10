package gui;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entidades.Vendedor;
import model.servicos.ServicoVendedor;

public class ListaVendedorController implements Initializable, ListenerAlteracaoDeDados {

	private ServicoVendedor servico;

	@FXML
	private TableView<Vendedor> tableViewVendedor;

	@FXML
	private TableColumn<Vendedor, Integer> tableColumnId;

	@FXML
	private TableColumn<Vendedor, String> tableColumnNome;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnEditar;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnExcluir;

	@FXML
	private Button btNovo;

	private ObservableList<Vendedor> obsLista;

	@FXML
	public void onBtNovoAcao(ActionEvent evento) {
		Stage stagePai = gui.util.Utils.stageAtual(evento);
		Vendedor obj = new Vendedor();
		criarDialogForm(obj, "/gui/FormVendedor.fxml", stagePai);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		inicializarNodes();
	}

	public void setServicoVendedor(ServicoVendedor servico) {
		this.servico = servico;
	}

	private void inicializarNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void atualizarTableView() {
		if (servico == null) {
			throw new IllegalStateException("Servico está nulo");
		}
		List<Vendedor> listaVendedor = servico.buscarTudo();
		obsLista = FXCollections.observableArrayList(listaVendedor);
		tableViewVendedor.setItems(obsLista);
		inicializarBotaoDeEdicao();
		inicializarBotaoDeExclusao();
	}

	private void criarDialogForm(Vendedor obj, String nomeAbsoluto, Stage stagePai) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
//			Pane painel = loader.load();
//
//			FormVendedorController controlador = loader.getController();
//			controlador.setVendedor(obj);
//			controlador.setServicoVendedor(new ServicoVendedor());
//			controlador.acionarListenerAlteracaoDeDados(this);
//			controlador.atualizarFormData();
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Dados Vendedor");
//			dialogStage.setScene(new Scene(painel));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(stagePai);
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//		} catch (IOException e) {
//			Alerts.showAlert("IO Exception", "Erro ao carregar a View", e.getMessage(), AlertType.ERROR);
//		}
	}

	@Override
	public void quandoHouverAlteracaoDeDados() {
		atualizarTableView();
	}

	private void inicializarBotaoDeEdicao() {
		tableColumnEditar.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditar.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button botao = new Button("Editar");

			@Override
			protected void updateItem(Vendedor obj, boolean vazio) {
				super.updateItem(obj, vazio);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(botao);
				botao.setOnAction(
						evento -> criarDialogForm(obj, "/gui/FormVendedor.fxml", Utils.stageAtual(evento)));
			}
		});
	}

	private void inicializarBotaoDeExclusao() {
		tableColumnExcluir.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnExcluir.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button botao = new Button("Excluir");

			@Override
			protected void updateItem(Vendedor obj, boolean vazio) {
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

	private void excluirEntidade(Vendedor obj) {
		Optional<ButtonType> resultado = Alerts.solicitarConfirmacao("Confirmação", "Tem certeza que seja excluir?");
		
		if (resultado.get() == ButtonType.OK) {
			if (servico == null) {
				throw new IllegalStateException("Serviço está nulo");
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
