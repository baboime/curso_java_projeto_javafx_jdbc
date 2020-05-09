package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public void onBtNovoAcao() {
		System.out.println("onBtNovoAcao");
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
}
