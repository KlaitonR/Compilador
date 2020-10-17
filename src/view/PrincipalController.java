package view;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import util.AnalisadorLexico;
import util.Token;

public class PrincipalController {
	
	@FXML TextArea codigo;
	
	@FXML  TableView<Token> tabTokens;
	@FXML  TableColumn < Token , String > colLexema;
	@FXML  TableColumn < Token , String > colTipoToken;
	
	String cod;
	
	ArrayList<Token> tabelaDeTokens = new ArrayList<>();
	
	@FXML
	public void lerCodigo() {
		
		cod = codigo.getText();
		AnalisadorLexico al = new AnalisadorLexico();
		al.lc.carregaCodigo(cod);
		Token tk = null;
		
		tabelaDeTokens.clear();
		
		while (!(tk = al.proximoToken()).lexema.equals("Fim")){
		
			if (!tk.nome.toString().equals("ERRO")) {
				tabelaDeTokens.add(tk);
				System.out.println("Lexema:" + tk.lexema + "    Tipo token: " + tk.nome.toString());
			}else {
				break;
			}
		}
		
		if(tk.nome.toString().equals("ERRO")) {
			mostraMensagem("ERRO LÉXICO \nLinha: " + al.lc.contadorLinha + "\nLexema: " + tk.lexema, AlertType.ERROR);
		}else {
			tabelaDeTokens.add(tk);
			System.out.println("Lexema:" + tk.lexema + "    Tipo token: " + tk.nome.toString());
		}
		
		tableViewTokens();
		iniciaTable();
		
	}
	
	@FXML
	 private void tableViewTokens(){  
		tabTokens.setItems(FXCollections.observableArrayList(tabelaDeTokens));
	 }
	
	@FXML
	public void iniciaTable() {
		colLexema.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().lexema));
		colTipoToken.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().nome.toString()));
	}
	
	 private void mostraMensagem (String msg, AlertType tipo) {
			
			Alert a = new Alert (tipo);
		
			a.setHeaderText(null);
			a.setContentText(msg);
			a.show();
		}
	
}
