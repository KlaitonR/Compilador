package util;

import java.util.ArrayList;
import java.util.List;

public class TabelaDeSimbolos {

    private List<EntradaTabelaDeSimbolos> tabelaDeSimbolos;

    public TabelaDeSimbolos() {
        tabelaDeSimbolos = new ArrayList<>();
    }

    public int inserirNaTabela(String nome, TipoVariavel tipo) {
    	
        if (jaFoiDeclarado(nome)) {
            throw new RuntimeException("Erro semântico: A variável " + nome + " já foi declarada anteriormente!!");
        }
        
        EntradaTabelaDeSimbolos etds = new EntradaTabelaDeSimbolos();
        etds.nome = nome;
        etds.tipo = tipo;
        tabelaDeSimbolos.add(etds);
//*        for (EntradaTabelaDeSimbolos etds2 : tabelaDeSimbolos) {
//*            System.out.println("Tabela de Simbolos= "+etds2.nome);    
//*        } 
        System.out.println("Tabela de Simbolos= "+nome+" Tipo= "+tipo);
        return tabelaDeSimbolos.size()-1; // Retorna o índice do último elemento inserido
    }

 // Faz uma busca pela tabelaDeSimbolos na tentativa de encontrar o nome na tabela
 // e retornar o seu tipo
    public TipoVariavel determinaTipo(String nome) {
        for (EntradaTabelaDeSimbolos etds : tabelaDeSimbolos) {
            if (etds.nome.equals(nome)) {
                return etds.tipo;
            }
        }
        return null;
    }

    // Verifica se a variável já existe na tabela de simbolos
    public boolean jaFoiDeclarado(String nome) {
    //*    System.out.println("TabelaSimbolos= "+tabelaDeSimbolos.size());//!!!!
        for (EntradaTabelaDeSimbolos etds:tabelaDeSimbolos) {
    //*        System.out.println(nome);//!!!!
            if (etds.nome.equals(nome)) {
                return true;
            }
        }
        return false;
    }
}
