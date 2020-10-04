package view;

import java.util.ArrayList;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import util.AnalisadorLexico;
import util.Token;

public class PrincipalController {
	
	
	@FXML Label codigo;
	
	String cod;
	
	ArrayList<Token> tabelaDeTokens = new ArrayList<>();
	
	@FXML
	public void lerCodigo() {
		
		cod = codigo.getText();
		AnalisadorLexico al = new AnalisadorLexico();
		al.lc.carregaCodigo(cod);
		Token tk = null;
		
		while (!(tk = al.proximoToken()).lexema.equals("Fim")){
			tabelaDeTokens.add(tk);
			System.out.println(tk);
		}
		
	}
	
}
