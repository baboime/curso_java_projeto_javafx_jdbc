package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.servicos.ServicoDepartamento;

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemVendedor;
	
	@FXML
	private MenuItem menuItemDepartamento;
	
	@FXML
	private MenuItem menuItemSobre;
	
	@FXML
	public void onMenuItemVendedorAcao() {
		System.out.println("onMenuItemVendedorAcao");
	}
	
	@FXML
	public void onMenuItemDepartamentoAcao() {
		//carregarView("/gui/ListaDepartamento.fxml");
		carregarView2("/gui/ListaDepartamento.fxml");
	}
	
	@FXML
	public void onMenuItemSobreAcao() {
		carregarView("/gui/Sobre.fxml");
	}
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
	
	private synchronized void carregarView(String nomeAbsoluto) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			VBox novoVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(novoVBox.getChildren());	
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	private synchronized void carregarView2(String nomeAbsoluto) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
			VBox novoVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(novoVBox.getChildren());	
			
			ListaDepartamentoController controle = loader.getController();
			controle.setServicoDepartamento(new ServicoDepartamento());
			controle.atualizarTableView();
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao carregar a view", e.getMessage(), AlertType.ERROR);
		}
	}

}
