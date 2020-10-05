package util;

public class AnalisadorLexico {
	
	public LeitorDeCodigo lc = new LeitorDeCodigo();
	
	public Token proximoToken() {
		
		Token proximo = null;
		
		espacosEcometarios();
		lc.confirmar();
		
		proximo = fim();
		if(proximo == null) {
			lc.zerar();
		}else {
			lc.confirmar();
			return proximo;
		}
		
		proximo = palavraChave();
		if(proximo == null) {
			lc.zerar();
		}else {
			lc.confirmar();
			return proximo;
		}
		
		
		proximo = variavel();
		if(proximo == null) {
			lc.zerar();
		}else {
			lc.confirmar();
			return proximo;
		}
		
		proximo = numeros();
		if(proximo == null) {
			lc.zerar();
		}else {
			lc.confirmar();
			return proximo;
		}
		
		proximo = cadeia();
		if(proximo == null) {
			lc.zerar();
		}else {
			lc.confirmar();
			return proximo;
		}
		
		proximo = operadorAritmetico();
		if(proximo == null) {
			lc.zerar();
		}else {
			lc.confirmar();
			return proximo;
		}
		
		proximo = operadorRelacional();
		if(proximo == null) {
			lc.zerar();
		}else {
			lc.confirmar();
			return proximo;
		}
		
		proximo = parenteses();
		if(proximo == null) {
			lc.zerar();
		}else {
			lc.confirmar();
			return proximo;
		}
		
		proximo = delimitador();
		if(proximo == null) {
			lc.zerar();
		}else {
			lc.confirmar();
			return proximo;
		}
		
		return null;
		
	}
	
	public Token operadorAritmetico() {
		
		String c = lc.proximoCaracter();
		
		if(c == "*") {
			return new Token(TipoToken.OpAritMult, "*");
			}else if(c == "/") {
				return new Token(TipoToken.OpAritDiv, "/");
			}else if(c == "-") {
				return new Token(TipoToken.OpAritSub, "-");
			}else if(c == "+") {
				return new Token(TipoToken.OpAritSoma, "+");
		}else {
			return null;
		}
	}
	
	public Token delimitador() {
		String c = lc.proximoCaracter();
		
		if(c == ":") {
			return new Token(TipoToken.Delim, ":");
		}else {
			return null;
		}
	}
	
	public Token parenteses() {
		
		String c = lc.proximoCaracter();
		
		if(c == "(") {
			return new Token(TipoToken.AbrePar, "(");
			}else if(c == ")") {
				return new Token(TipoToken.FechaPar, ")");
		}else {
			return null;
		}
	}
	
	public Token operadorRelacional() {
	
		String c = lc.proximoCaracter();
		
		if(c == "<") {
			
			String proxC = lc.proximoCaracter();
			
			if(proxC == ">") {
				return new Token(TipoToken.OpRelDif, "<>");
			}else if (proxC == "=") {
				return new Token(TipoToken.OpRelMenorigual, "<=");
			}else {
				lc.retroceder();
				return new Token(TipoToken.OpRelMenor, "<");
			}
			
		}else if (c == "=") {
			return new Token(TipoToken.opRelIgual, "=");
		}else if (c == ">") {
			
			String proxC = lc.proximoCaracter();
			
			if (proxC == "=") {
				return new Token(TipoToken.OpRelMaior, ">=");
			}else {
				lc.retroceder();
				return new Token(TipoToken.OpRelMaior, ">");
			}
		}else {
			return null;
		}
		
	}
	
	public Token numeros() {
		
		String lexema = "";
		String c = lc.proximoCaracter();
		boolean inteiro = true;
		
		if(Character.isDigit(c.charAt(0))) {
			
			lc.retroceder(); // reinicia variaveis
			c = "0";
			
			while (Character.isDigit(c.charAt(0))) {
				
				c = lc.proximoCaracter();
				
				if (Character.isDigit(c.charAt(0))) {
					lexema += c;
				}else {
					if (c == ".") {
						
						String proxC = lc.proximoCaracter();
						
						if(Character.isDigit(proxC.charAt(0))) {
							inteiro = false;
							lexema += proxC;
							lc.retroceder();
						}else {
							lc.retroceder();
							return null;
						}
					}
				}
			}
		
			c = lc.proximoCaracter();
			
			while (Character.isDigit(c.charAt(0))) {
				c = lc.proximoCaracter();
				lexema += c;
			}
			
			lc.retroceder();
		
			if(inteiro == true) {
				return new Token(TipoToken.NumInt, lexema);
			}else if (inteiro == false) {
				return new Token(TipoToken.NumReal, lexema);
			}
		}
		
		return null;

	}
	
	public Token variavel() {
		
		String c = lc.proximoCaracter();
		//String lexema = "";
		
		if(Character.isLetter(c.charAt(0))) {
			
			while (Character.isLetterOrDigit(c.charAt(0))) {
				//lexema += c;
				c = lc.proximoCaracter();
			}
			
			lc.retroceder();
			return new Token(TipoToken.Var, lc.Lexema());
		
		}
		return null;
	}
	
	public Token cadeia() {
		
		String c = lc.proximoCaracter();
		//String lexema = "";
		
		if(c == "'") {
			
			lc.retroceder(); // reinicia variaveis
			c = "0";
			
			while(c != "\n" || c != "'") {
				c = lc.proximoCaracter();
				//lexema += c;
			}
			
			if(c == "\n") {
				return null;
			}else if (c == "'") {
				return new Token(TipoToken.Cadeia, lc.Lexema());
			}
			
		}
		
		return null;
	
	}
	
	public void espacosEcometarios() {
		
		String c = lc.proximoCaracter();
		
		if(c == "%") {
			
			while(c != "\n") {
				c = lc.proximoCaracter();
			}
			
			lc.retroceder();
			return;
			
		}else if (Character.isWhitespace(c. charAt(0))) {
			return;
		}else if (Character.isWhitespace(c.charAt(0)) || c == "%") {
			lc.retroceder();
			return;
		}
	
	}
	
	public Token palavraChave() {
		
		String c = lc.proximoCaracter();
		//String lexema = "";
		
		if(Character.isLetter(c.charAt(0))) {
			
			lc.retroceder(); // reinicia variaveis
			c = "a";
			
			while(Character.isLetter(c.charAt(0))) {
				c = lc.proximoCaracter();
				//lexema += c;
			}
			
			if(lc.Lexema().equals("DECLARACOES")) {
				return new Token(TipoToken.PCDeclaracoes, lc.Lexema());
			}else if (lc.Lexema().equals("ALGORITMO")) {
				return new Token(TipoToken.PCAlgoritmo, lc.Lexema());
			}else if (lc.Lexema().equals("INT")) {
				return new Token(TipoToken.PCInteiro, lc.Lexema());
			}else if (lc.Lexema().equals("REAL")) {
				return new Token(TipoToken.PCReal, lc.Lexema());
			}else if (lc.Lexema().equals("ATRIBUIR")) {
				return new Token(TipoToken.PCAtribuir, lc.Lexema());
			}else if (lc.Lexema().equals("A")) {
				return new Token(TipoToken.PCA, lc.Lexema());
			}else if (lc.Lexema().equals("LER")) {
				return new Token(TipoToken.PCLer, lc.Lexema());
			}else if (lc.Lexema().equals("IMPRIMIR")) {
				return new Token(TipoToken.PCImprimir, lc.Lexema());
			}else if (lc.Lexema().equals("SE")) {
				return new Token(TipoToken.PCSe, lc.Lexema());
			}else if (lc.Lexema().equals("ENTAO")) {
				return new Token(TipoToken.PCEntao, lc.Lexema());
			}else if (lc.Lexema().equals("ENQUANTO")) {
				return new Token(TipoToken.PCEnquanto, lc.Lexema());
			}else if (lc.Lexema().equals("INICIO")) {
				return new Token(TipoToken.PCInicio, lc.Lexema());
			}else if (lc.Lexema().equals("FIM")) {
				return new Token(TipoToken.PCFim, lc.Lexema());
			}else if (lc.Lexema().equals("E")) {
				return new Token(TipoToken.OpBoolE, lc.Lexema());
			}else if (lc.Lexema().equals("OU")) {
				return new Token(TipoToken.OpBoolOu, lc.Lexema());
			}else {
				return null;
			}
		}
		
		return null;
		
	}
	
	public Token fim() {
		
		if(lc.Lexema().equals("FIM")) {
			return new Token(TipoToken.Fim, "Fim");
		}else {
			return null;
		}
		
	}
	
	
	

}
