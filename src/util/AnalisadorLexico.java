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
		
		return proximo = new Token(TipoToken.ERRO, erroLexico());
		
	}
	
	public Token operadorAritmetico() {
			
			String c = lc.proximoCaracter();
			
			if(c != null){
			
				if(c.equals("*")) {
					return new Token(TipoToken.OpAritMult, "*");
				}else if(c.equals("/")) {
					return new Token(TipoToken.OpAritDiv, "/");
				}else if(c.equals("-")) {
					return new Token(TipoToken.OpAritSub, "-");
				}else if(c.equals("+")) {
					return new Token(TipoToken.OpAritSoma, "+");
				}else {
					lc.retroceder();
					return null;
				}
		
		}else {
			return null;
		}
	}
	
	public Token delimitador() {
		
		String c = lc.proximoCaracter();
		
		if(c != null){
		
			if(c.equals(":")) {
				return new Token(TipoToken.Delim, ":");
			}else {
				lc.retroceder();
				return null;
			}
		}else {
			return null;
		}
	}
	
	public Token parenteses() {
		
		String c = lc.proximoCaracter();
		
		if(c != null){
			
			if(c.equals("(")) {
				return new Token(TipoToken.AbrePar, "(");
			}else if(c.equals(")")) {
				return new Token(TipoToken.FechaPar, ")");
			}else {
				lc.retroceder();
				return null;
			}
		
		}else {
			return null;
		}
	}
	
	public Token operadorRelacional() {
	
		String c = lc.proximoCaracter();
		
		if(c != null){
			if(c.equals("<")) {
				
				String proxC = lc.proximoCaracter();
				
				if(proxC == null)
					return null;
				
				if(proxC.equals(">")) {
					return new Token(TipoToken.OpRelDif, "<>");
				}else if (proxC.equals("=")) {
					return new Token(TipoToken.OpRelMenorIgual, "<=");
				}else {
					lc.retroceder();
					return new Token(TipoToken.OpRelMenor, "<");
				}
				
			}else if (c.equals("=")) {
				return new Token(TipoToken.opRelIgual, "=");
			}else if (c.equals(">")) {
				
				String proxC = lc.proximoCaracter();
				
				if(proxC != null) {
					if (proxC.equals("=")) {
						return new Token(TipoToken.OpRelMaiorIgual, ">=");
					}else {
						lc.retroceder();
						return new Token(TipoToken.OpRelMaior, ">");
					}
				}else {
					return null;
				}
					
			}else {
				lc.retroceder();
				return null;
			}
			
		}else {
			return null;
		}
	}
	
	public Token numeros() {

		String c = lc.proximoCaracter();
		boolean inteiro = true;
		
		if(c != null) {
			
			if(Character.isDigit(c.charAt(0))) {
				
				while (Character.isDigit(c.charAt(0))) {
					
					c = lc.proximoCaracter();
					if(c == null)
						return null;
					
					if (Character.isDigit(c.charAt(0))) {
						continue;
						
					}else if (c.equals(".")){
						
						String proxC = lc.proximoCaracter();
							
						if(proxC != null) {
							if(Character.isDigit(proxC.charAt(0))) {
								inteiro = false;
								lc.retroceder();
							}else {
								lc.retroceder();
								return null;
							}
						}else {
							return null;
						}
							
					}else if(!c.equals(" ") && !c.equals("\n") & !c.equals("%")){
						return null;
					}else {
						lc.retroceder();
						return new Token(TipoToken.NumInt, lc.Lexema());
					}
				}
			
				c = lc.proximoCaracter();
				
				if(c == null)
					return null;
				
				if(!inteiro && Character.isDigit(c.charAt(0))) {

					while (Character.isDigit(c.charAt(0))) {
						c = lc.proximoCaracter();
						if(c == null)
							return null;
					}
					
					if(!c.equals(" ") && !c.equals("\n") & !c.equals("%")) {
						return null;
					}else {
						lc.retroceder();
						return new Token(TipoToken.NumReal, lc.Lexema());
					}
					
				}else {
					return null;
				}
				
			}else {
				lc.retroceder();
				return null;
			}
			
		}else {
			return null;
		}
	}
	
	public Token variavel() {
		
		String c = lc.proximoCaracter();
		
		if(c != null){
		
			if(Character.isLetter(c.charAt(0))) {
				
				while (Character.isLetterOrDigit(c.charAt(0))) {
					c = lc.proximoCaracter();
					if(c == null)
						break;
				}
				
				if(c != null) {
					lc.retroceder();
					return new Token(TipoToken.Var, lc.Lexema());
				}else {
					return null;
				}
			
			}else {
				lc.retroceder();
				return null;
			}
			
		}else {
			return null;
		}
	}
	
	public Token cadeia() {
		
		String c = lc.proximoCaracter();
		String pc = "";
		
		if(c != null){
			
			if(c.equals("'")) {
	
				while(!pc.equals("\n") && !pc.equals("'")) {
					pc = lc.proximoCaracter();
					if(pc == null)
						return null;
				}
				
				c = pc;
				
				if(c == "\n") { //Houve quebra de linha na cadeia, erro l�xico
					return null;
				}else{ // Se n�o � uma aspas simples "'" (fim da cadeia)
					return new Token(TipoToken.Cadeia, lc.Lexema());
				}
				
			}else {
				lc.retroceder();
				return null;
			}
			
		}else {
			return null;
		}
	}
	
	public void espacosEcometarios() {
		
		String c = lc.proximoCaracter();
		String pc;
			
		if(c != null) {
			if(c.equals("%")) {
				
				while(!c.equals("\n")) { 
					c = lc.proximoCaracter();
					if(c == null)
						return;
				}
				
				pc = lc.proximoCaracter();
				if(pc == null)
					return;
			
				if(!pc.equals(" ") && !pc.equals("\n") && !pc.equals("%") && !c.equals("\t")) {
					lc.retroceder();
					return; 
				}else {
					lc.retroceder();
					espacosEcometarios();  
				}
				
				return;
				
			}else if (c.equals(" ")) {
			
				while(c.equals(" ")) { 
					c = lc.proximoCaracter();
					if(c == null)
						return;
				}
					
				if(!c.equals(" ") && !c.equals("\n") && !c.equals("%") && !c.equals("\t")) {
					lc.retroceder();
					return; 
				}else {
					lc.retroceder();
					espacosEcometarios();
				}
			
			}else if(c.equals("\n")) {
				// String pc = lc.proximoCaracter();
				
				while(c.equals("\n")) {  //pc.equals
				
					c = lc.proximoCaracter();
					if(c == null)
						return;
					
					lc.contadorLinha++;
				}
				
				if(!c.equals(" ") && !c.equals("\n") && !c.equals("%") && !c.equals("\t")) { //pc.equals
					lc.retroceder();
					return; 
				}else {
					lc.retroceder();
					espacosEcometarios();
				}
				
			}else if (c.equals("\t")) {
				
				while(c.equals("\t")) {
					
					c = lc.proximoCaracter();
					if(c == null)
						return;
				}
				
				if(!c.equals(" ") && !c.equals("\n") && !c.equals("%") && !c.equals("\t")) { 
					lc.retroceder();
					return; 
				}else {
					lc.retroceder();
					espacosEcometarios();
				}
				
			}else if (!c.equals(" ") && !c.equals("%") && !c.equals("\n") && !c.equals("\t")) {
				lc.retroceder();
				return;
			}
			
		}else {
			return;
		}	
	}
	
	public Token palavraChave() {
		
		String c = lc.proximoCaracter();
		
		if(c != null) {
		
			if(Character.isLetter(c.charAt(0))) {
				
				while(Character.isLetter(c.charAt(0))) {
					c = lc.proximoCaracter();
					if(c == null)
						return null;
				}
				
				lc.retroceder();
		
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
			}else {
				lc.retroceder();
				return null;
			}
			
		}else {
			return null;
		}
	}
	
	public Token fim() {
		
		String c = lc.proximoCaracter();
	
		if(c != null) {
	
			if(Character.isLetter(c.charAt(0))) {
				
				while(Character.isLetter(c.charAt(0))) { 
					c = lc.proximoCaracter();
					if(c == null)
						return null;
					
					if(lc.Lexema().equals("Fim")) {
						break;
					}
				}
				
				if(lc.Lexema().equals("Fim")) {
					String proxC = lc.proximoCaracter();
					if(proxC == null) {
						return new Token(TipoToken.Fim, lc.Lexema());
					}else {
						lc.retroceder();
						return new Token(TipoToken.ERRO, "Fim - Token Fim j� declarado");
					}
				}else {
					return null;
				}
				
//				if(lc.Lexema().equals("Fim")) {
//					return new Token(TipoToken.Fim, "Fim");
//				}else {
//					return null;
//				}	
				
			}else {
				lc.retroceder();
				return null;
			}
		
		}else {
			return null;
		}
		
	}
	
	public String erroLexico() {
		
		String erro = "", c = "";
		c = lc.proximoCaracter();
		
		if(c == null) {
			return "null - Aus�ncia token Fim";
		}
		
		while(!c.equals(" ") && !c.equals("\n") ) {
			erro += c;
			c = lc.proximoCaracter();
			if(c == null)
				return erro + " - Aus�ncia token Fim"; 
		}
		
		return erro;
	}

}
