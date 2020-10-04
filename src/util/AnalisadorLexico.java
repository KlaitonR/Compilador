package util;

public class AnalisadorLexico {
	
	public LeitorDeCodigo lc = new LeitorDeCodigo();
	String lexema;
	
	public Token analisaBuffer() {
		
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
	
	
	

}
