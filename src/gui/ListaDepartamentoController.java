package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entidades.Departamento;
import model.servicos.ServicoDepartamento;

public class ListaDepartamentoController implements Initializable {
	
	private ServicoDepartamento servico;
	
	@FXML
	private TableView<Departamento> tableViewDepartamento;
	
	@FXML
	private TableColumn<Departamento, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Departamento, String> tableColumnNome;
	
	@FXML
	private Button btNovo;
	
	private ObservableList<Departamento> obsLista;
	
	@FXML
	public void onBtNovoAcao(ActionEvent evento) {
		Stage stagePai = gui.util.Utils.stageAtual(evento);
		criarDialogForm("/gui/FormDepartamento.fxml", stagePai);
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
			throw new IllegalStateException("Servico está nulo");
		}
		List<Departamento> listaDepartamento = servico.buscarTudo();
		obsLista = FXCollections.observableArrayList(listaDepartamento);
		tableViewDepartamento.setItems(obsLista);
	}
	
	private void criarDialogForm(String nomeAbsoluto, Stage stagePai) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			Pane painel = loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados Departamento");
			dialogStage.setScene(new Scene(painel));
			dialogStage.setResizable(false);
			dialogStage.initOwner(stagePai);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a View", e.getMessage(), AlertType.ERROR);
		}
	}
}
