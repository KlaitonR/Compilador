package util;

public class AnalisadorLexico {
	
	public LeitorDeCodigo lc = new LeitorDeCodigo();
	String lexema;
	
	public Token proximoToken() {
		
		Token proximo = null;
		
		espacosEcometarios();
	
		return new Token(TipoToken.AbrePar, "lexema");
		
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
		String lexema = "";
		
		if(Character.isLetter(c.charAt(0))) {
			
			while (Character.isLetterOrDigit(c.charAt(0))) {
				lexema += c;
				c = lc.proximoCaracter();
			}
			
			lc.retroceder();
			return new Token(TipoToken.Var, lexema);
		
		}
		return null;
	}
	
	public Token cadeia() {
		
		String c = lc.proximoCaracter();
		String lexema = "";
		
		if(c == "'") {
			
			lc.retroceder(); // reinicia variaveis
			c = "0";
			
			while(c != "\n" || c != "'") {
				c = lc.proximoCaracter();
				lexema += c;
			}
			
			if(c == "\n") {
				return null;
			}else if (c == "'") {
				return new Token(TipoToken.Cadeia, lexema);
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
			
		}else if (Character.isWhitespace(c.charAt(0))) {
			return;
		}else if (Character.isWhitespace(c.charAt(0)) || c == "%") {
			lc.retroceder();
			return;
		}
	
	}
	
	public Token palavraChave() {
		
		String c = lc.proximoCaracter();
		String lexema = "";
		
		if(Character.isLetter(c.charAt(0))) {
			
			lc.retroceder(); // reinicia variaveis
			c = "a";
			
			while(Character.isLetter(c.charAt(0))) {
				c = lc.proximoCaracter();
				lexema += c;
			}
			
			if(lexema.equals("DECLARACOES")) {
				return new Token(TipoToken.PCDeclaracoes, lexema);
			}else if (lexema.equals("ALGORITMO")) {
				return new Token(TipoToken.PCAlgoritmo, lexema);
			}else if (lexema.equals("INT")) {
				return new Token(TipoToken.PCInteiro, lexema);
			}else if (lexema.equals("REAL")) {
				return new Token(TipoToken.PCReal, lexema);
			}else if (lexema.equals("ATRIBUIR")) {
				return new Token(TipoToken.PCAtribuir, lexema);
			}else if (lexema.equals("A")) {
				return new Token(TipoToken.PCA, lexema);
			}else if (lexema.equals("LER")) {
				return new Token(TipoToken.PCLer, lexema);
			}else if (lexema.equals("IMPRIMIR")) {
				return new Token(TipoToken.PCImprimir, lexema);
			}else if (lexema.equals("SE")) {
				return new Token(TipoToken.PCSe, lexema);
			}else if (lexema.equals("ENTAO")) {
				return new Token(TipoToken.PCEntao, lexema);
			}else if (lexema.equals("ENQUANTO")) {
				return new Token(TipoToken.PCEnquanto, lexema);
			}else if (lexema.equals("INICIO")) {
				return new Token(TipoToken.PCInicio, lexema);
			}else if (lexema.equals("FIM")) {
				return new Token(TipoToken.PCFim, lexema);
			}else if (lexema.equals("E")) {
				return new Token(TipoToken.OpBoolE, lexema);
			}else if (lexema.equals("OU")) {
				return new Token(TipoToken.OpBoolOu, lexema);
			}else {
				return null;
			}
		}
		
		return null;
		
	}
	
	public Token fim() {
		
		String c = lc.proximoCaracter();
		
		if(c == null) {
			return new Token(TipoToken.Fim, "Fim");
		}else {
			return null;
		}
		
	}
	
	
	

}
