package util;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSintatico {
	
	private final int TAMANHO_BUFFER = 10;
	List<Token> bufferTokens;
	AnalisadorLexico lex;
	boolean fim;
	TabelaDeSimbolos ts;

	public String erro = "";
	
	public AnalisadorSintatico(AnalisadorLexico lex) {
		ts = new TabelaDeSimbolos();
		this.lex = lex;
		bufferTokens = new ArrayList<Token>();
		lerToken();
	}
	
	private void lerToken() {
		
		if(bufferTokens.size() > 0) {
			bufferTokens.remove(0);
		}
		
		while (bufferTokens.size() < TAMANHO_BUFFER && !fim) {
			Token proximo = lex.proximoToken();
			bufferTokens.add(proximo);
			if(proximo.nome == TipoToken.Fim) {
				fim = true;
			}
		}
		  
		if(lookahead(1) != null)
			System.out.println("Lido " + lookahead(1).nome);
		
	}
	
	 void match(TipoToken tipo) {
	        if (lookahead(1).nome == tipo) {
	            System.out.println("Match: " + lookahead(1).nome);
	            lerToken();
	        } else {
	            erroSintatico(tipo.name());
	        }
	    }
	
	public Token lookahead(int k) {
		
		if(bufferTokens.isEmpty()) {
			return null;
		}
		
		if(k -1 >= bufferTokens.size()) {
			return bufferTokens.get(bufferTokens.size() - 1);
		}
		
		return bufferTokens.get(k -1);
	}
	
	void erroSintatico(String... tokensEsperados) {
		String msg = "Erro sintático: esperando um dos seguintes tokens (";
		
		for(int i = 0; i<tokensEsperados.length; i++) {
			msg += tokensEsperados[i];
			if(i<tokensEsperados.length-1)
				msg += ",";
		}
		
		msg += "), mas foi encontrado " + lookahead(1).nome + " na linha " + lex.lc.contadorLinha + "\n";
		erro += msg;
		System.out.println(msg);
	}
	
	  //programa : ':' 'DECLARACOES' listaDeclaracoes ':' 'ALGORITMO' listaComandos;
    public void programa() {
        match(TipoToken.Delim);
        match(TipoToken.PCDeclaracoes);
        listaDeclaracoes();
        match(TipoToken.Delim);
        match(TipoToken.PCAlgoritmo);
        listaComandos();
        match(TipoToken.Fim);
    }

    //listaDeclaracoes : declaracao listaDeclaracoes | declaracao;
    void listaDeclaracoes() {
        if (lookahead(4).nome == TipoToken.Delim) {
            declaracao();
        } else if (lookahead(4).nome == TipoToken.Var) {
            declaracao();
            listaDeclaracoes();
        } else {
            erroSintatico(TipoToken.Delim.name(), TipoToken.Var.name());
        }
    }

    //declaracao : VARIAVEL ':' tipoVar;
    void declaracao() {
        String nomeVar = lookahead(1).lexema;
//*        System.out.println("nomeVar= "+nomeVar);
        match(TipoToken.Var);
        match(TipoToken.Delim);
        TipoVariavel tipoVar = tipoVar();
//*        System.out.println("tipoVar= "+tipoVar);
        ts.inserirNaTabela(nomeVar, tipoVar); // instalar o nome na lista
    }

    //tipoVar : 'INTEIRO' | 'REAL';
    TipoVariavel tipoVar() { 
        if (lookahead(1).nome == TipoToken.PCInteiro) {
            match(TipoToken.PCInteiro);
            return TipoVariavel.INT; 
        } else if (lookahead(1).nome == TipoToken.PCReal) {
            match(TipoToken.PCReal);
            return TipoVariavel.REAL; 
        } else {
            erroSintatico("INTEIRO","REAL");
            return null;
        }
    }

    //expressaoAritmetica : expressaoAritmetica '+' termoAritmetico | expressaoAritmetica '-' termoAritmetico | termoAritmetico;
    TipoVariavel expressaoAritmetica() { // TipoVariavel
        TipoVariavel tipoTermo = termoAritmetico(); //Lado esquerdo da expressão (2) 
        TipoVariavel tipoExp = expressaoAritmetica2();//Lado direito da expressão(+3)
        if (tipoTermo == TipoVariavel.REAL || tipoExp == TipoVariavel.REAL){
            return TipoVariavel.REAL;//Caso 1 dos 2 retorne REAL, então a expressão 
        } else {                     //assume tipo REAL
            return TipoVariavel.INT;//Caso contrário assume INT
        }
    }

    TipoVariavel expressaoAritmetica2() {
        if (lookahead(1).nome == TipoToken.OpAritSoma || 
                lookahead(1).nome == TipoToken.OpAritSub) {
            TipoVariavel tipoTermo = expressaoAritmetica2SubRegra1();
            TipoVariavel tipoExp = expressaoAritmetica2();
            if (tipoTermo == TipoVariavel.REAL || tipoExp == TipoVariavel.REAL){
                return TipoVariavel.REAL;
            } else {
                return TipoVariavel.INT;
            }
        } else { // vazio
            return null;
        }
    }

    TipoVariavel expressaoAritmetica2SubRegra1() {
        if (lookahead(1).nome == TipoToken.OpAritSoma) {
            match(TipoToken.OpAritSoma);
            TipoVariavel tipoTermo = termoAritmetico();
            if (tipoTermo == TipoVariavel.REAL){
                return TipoVariavel.REAL;
            } else {
                return TipoVariavel.INT;
            }
        } else if (lookahead(1).nome == TipoToken.OpAritSub) {
            match(TipoToken.OpAritSub);
            TipoVariavel tipoTermo = termoAritmetico();
            if (tipoTermo == TipoVariavel.REAL){
                return TipoVariavel.REAL;
            } else {
                return TipoVariavel.INT;
            }
        } else {
            erroSintatico("+","-");
            return null; 
        }
    }

    //termoAritmetico : termoAritmetico '*' fatorAritmetico | termoAritmetico '/' fatorAritmetico | fatorAritmetico;
     TipoVariavel termoAritmetico() {
        TipoVariavel tipoFator = fatorAritmetico();
        TipoVariavel tipoTermo = termoAritmetico2(); 
        if (tipoFator == TipoVariavel.REAL || tipoTermo == TipoVariavel.REAL){
                return TipoVariavel.REAL;
            } else {
                return TipoVariavel.INT;
            }

    }

    TipoVariavel termoAritmetico2() {
        if (lookahead(1).nome == TipoToken.OpAritMult || 
                lookahead(1).nome == TipoToken.OpAritDiv) {
            TipoVariavel tipoTermo = termoAritmetico2SubRegra1();
            TipoVariavel tipoTermo2 = termoAritmetico2();
            if (tipoTermo == TipoVariavel.REAL || tipoTermo2 == TipoVariavel.REAL){
                return TipoVariavel.REAL;
            } else {
                return TipoVariavel.INT;
            }
        } else { // vazio
            return null; 
        }
    }

    TipoVariavel termoAritmetico2SubRegra1() {
        if (lookahead(1).nome == TipoToken.OpAritMult) {
            match(TipoToken.OpAritMult);
            TipoVariavel tipoFator = fatorAritmetico(); 
            if (tipoFator == TipoVariavel.REAL){
                return TipoVariavel.REAL;
            } else {
                return TipoVariavel.INT;
            }//!!!!
        } else if (lookahead(1).nome == TipoToken.OpAritDiv) {
            match(TipoToken.OpAritDiv);
            TipoVariavel tipoFator = fatorAritmetico(); 
            if (tipoFator == TipoVariavel.REAL){
                return TipoVariavel.REAL;
            } else {
                return TipoVariavel.INT;
            }
        } else {
            erroSintatico("*","/");
            return null; 
        }
    }

    //fatorAritmetico : NUMINT | NUMREAL | VARIAVEL | '(' expressaoAritmetica ')'
    TipoVariavel fatorAritmetico() {
        if (lookahead(1).nome == TipoToken.NumInt) {
            match(TipoToken.NumInt);
            return TipoVariavel.INT;
        } else if (lookahead(1).nome == TipoToken.NumReal) {
            match(TipoToken.NumReal);
            return TipoVariavel.REAL;
        } else if (lookahead(1).nome == TipoToken.Var) {
            // tabela de símbolos
            String nomeVar = lookahead(1).lexema;
            if (!ts.jaFoiDeclarado(nomeVar)) {
                throw new RuntimeException("Erro Semântico: A variável " + nomeVar + " não está declarada!!");
            }
            TipoVariavel tipoVar = ts.determinaTipo(nomeVar);
            match(TipoToken.Var);
            return tipoVar;//Retorna o tipo da variável para ajudar a compor o
                            // tipo da expressão
        } else if (lookahead(1).nome == TipoToken.AbrePar) {
            match(TipoToken.AbrePar);
            TipoVariavel tipoExp = expressaoAritmetica();
            match(TipoToken.FechaPar);
        } else {
            erroSintatico(TipoToken.NumInt.toString(), TipoToken.NumReal.toString(), TipoToken.Var.toString(), "(");
            return null; 
        }
        return null; 
    }

    //expressaoRelacional : expressaoRelacional operadorBooleano termoRelacional | termoRelacional;
    void expressaoRelacional() {
        termoRelacional();
        expressaoRelacional2();
    }

    void expressaoRelacional2() {
        if (lookahead(1).nome == TipoToken.OpBoolE || lookahead(1).nome == TipoToken.OpBoolOu) {
            operadorBooleano();
            termoRelacional();
            expressaoRelacional2();
        } else { // vazio
        }
    }

    //termoRelacional : expressaoAritmetica OP_REL expressaoAritmetica | '(' expressaoRelacional ')';
    void termoRelacional() {
        if (lookahead(1).nome == TipoToken.NumInt
                || lookahead(1).nome == TipoToken.NumReal
                || lookahead(1).nome == TipoToken.Var
                || lookahead(1).nome == TipoToken.AbrePar) {
           expressaoAritmetica();
            opRel();
            expressaoAritmetica();
        } else {
            erroSintatico(TipoToken.NumInt.toString(),TipoToken.NumReal.toString(),TipoToken.Var.toString(),"(");
        }
    }

    void opRel() {
        if (lookahead(1).nome == TipoToken.OpRelDif) {
            match(TipoToken.OpRelDif);
        } else if (lookahead(1).nome == TipoToken.OpRelIgual) {
            match(TipoToken.OpRelIgual);
        } else if (lookahead(1).nome == TipoToken.OpRelMaior) {
            match(TipoToken.OpRelMaior);
        } else if (lookahead(1).nome == TipoToken.OpRelMaiorIgual) {
            match(TipoToken.OpRelMaiorIgual);
        } else if (lookahead(1).nome == TipoToken.OpRelMenor) {
            match(TipoToken.OpRelMenor);
        } else if (lookahead(1).nome == TipoToken.OpRelMenorIgual) {
            match(TipoToken.OpRelMenorIgual);
        } else {
            erroSintatico("<>","=",">",">=","<","<=");
        }
    }

    //operadorBooleano : 'E' | 'OU';
    void operadorBooleano() {
        if (lookahead(1).nome == TipoToken.OpBoolE) {
            match(TipoToken.OpBoolE);
        } else if (lookahead(1).nome == TipoToken.OpBoolOu) {
            match(TipoToken.OpBoolOu);
        } else {
            erroSintatico("E","OU");
        }
    }

    //listaComandos : comando listaComandos | comando;
    void listaComandos() {
        comando();
        listaComandosSubRegra1();
    }

    void listaComandosSubRegra1() {
        if (lookahead(1).nome == TipoToken.PCAtribuir ||
        lookahead(1).nome == TipoToken.PCLer ||
        lookahead(1).nome == TipoToken.PCImprimir ||
        lookahead(1).nome == TipoToken.PCSe ||
        lookahead(1).nome == TipoToken.PCEnquanto ||
        lookahead(1).nome == TipoToken.PCInicio) {
            listaComandos();
        } else {
            // vazio
        }
    }

    //comando : comandoAtribuicao | comandoEntrada | comandoSaida | comandoCondicao | comandoRepeticao | subAlgoritmo;
    void comando() {
        if (lookahead(1).nome == TipoToken.PCAtribuir) {
            comandoAtribuicao();
        } else if (lookahead(1).nome == TipoToken.PCLer) {
            comandoEntrada();
        } else if (lookahead(1).nome == TipoToken.PCImprimir) {
            comandoSaida();
        } else if (lookahead(1).nome == TipoToken.PCSe) {
            comandoCondicao();
        } else if (lookahead(1).nome == TipoToken.PCEnquanto) {
            comandoRepeticao();
        } else if (lookahead(1).nome == TipoToken.PCInicio) {
            subAlgoritmo();
        } else {
            erroSintatico("ATRIBUIR","LER","IMPRIMIR","SE","ENQUANTO","INICIO");
        }
    }

    //comandoAtribuicao : 'ATRIBUIR' expressaoAritmetica 'A' VARIAVEL;
    void comandoAtribuicao() {
        match(TipoToken.PCAtribuir);
        TipoVariavel tipoExp = expressaoAritmetica();
        match(TipoToken.PCA);
        String nomeVar = lookahead(1).lexema;
        if(!ts.jaFoiDeclarado(nomeVar)){
            throw new RuntimeException("Erro Semântico: Variavel "+nomeVar+" não foi declarada");
        }//!!!
        TipoVariavel tipoVar = ts.determinaTipo(nomeVar);
        match(TipoToken.Var);
        if (tipoVar == TipoVariavel.INT && tipoExp == TipoVariavel.REAL){
            throw new RuntimeException("Erro Semântico: Variável "+nomeVar+" deve ser do tipo REAL!!");
        }
    }
    //comandoEntrada : 'LER' VARIAVEL;
    void comandoEntrada() {
        match(TipoToken.PCLer);
        match(TipoToken.Var);
    }

    //comandoSaida : 'IMPRIMIR'  (VARIAVEL | CADEIA);
    void comandoSaida() {
        match(TipoToken.PCImprimir);
        comandoSaidaSubRegra1();
    }

    void comandoSaidaSubRegra1() {
        if (lookahead(1).nome == TipoToken.Var) {
            match(TipoToken.Var);
        } else if (lookahead(1).nome == TipoToken.Cadeia) {
            match(TipoToken.Cadeia);
        } else {
            erroSintatico(TipoToken.Var.toString(),TipoToken.Cadeia.toString());
        }
    }

    //comandoCondicao : 'SE' expressaoRelacional 'ENTAO' comando | 'SE' expressaoRelacional 'ENTAO' comando 'SENAO' comando;
    void comandoCondicao() {
        match(TipoToken.PCSe);
        expressaoRelacional();
        match(TipoToken.PCEntao);
        comando();
        comandoCondicaoSubRegra1();
    }

    void comandoCondicaoSubRegra1() {
        if (lookahead(1).nome == TipoToken.PCSenao) {
            match(TipoToken.PCSenao);
            comando();
        } else {
            // vazio
        }
    }

    //comandoRepeticao : 'ENQUANTO' expressaoRelacional comando;
    void comandoRepeticao() {
        match(TipoToken.PCEnquanto);
        expressaoRelacional();
        comando();
    }

    //subAlgoritmo : 'INICIO' listaComandos 'FIM';
    void subAlgoritmo() {
        match(TipoToken.PCInicio);
        listaComandos();
        match(TipoToken.PCFim);
    }

}