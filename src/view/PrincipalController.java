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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import util.AnalisadorLexico;
import util.Token;

public class PrincipalController {
	
	@FXML TextArea codigo;
	@FXML TextArea buffer;
	
	@FXML  TableView<Token> tabTokens;
	@FXML  TableColumn < Token , String > colLexema;
	@FXML  TableColumn < Token , String > colTipoToken;

	String cod;
	String buf;
	
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
				al.lc.terminal += "\nLexema: " + tk.lexema + " - Tipo token: " + tk.nome.toString() + "\n";
			}else {
				break;
			}
		}
		
		if(tk.nome.toString().equals("ERRO")) {
			mostraMensagem("ERRO LÉXICO \nLinha: " + al.lc.contadorLinha + "\nLexema: " + tk.lexema, AlertType.ERROR);
			al.lc.terminal += "\nLinha:" + al.lc.contadorLinha + "\nLexema: " + tk.lexema;
		}else {
			tabelaDeTokens.add(tk);
			al.lc.terminal += "\nLexema: " + tk.lexema + " - Tipo token: " + tk.nome.toString() + "\n";
		}
		
		tableViewTokens();
		iniciaTable();
		buffer.setText(buf = al.lc.terminal);
		Font font = Font.font("consolas", FontWeight.NORMAL, FontPosture.REGULAR, 13);
		buffer.setFont(font);
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
