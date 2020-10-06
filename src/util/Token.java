package util;

public class Token {
	
	public TipoToken nome;
	public String n;
	public String lexema;
	
	public Token(TipoToken nome, String lexema) {
		this.nome = nome;
		this.n = nome.toString();
		this.lexema = lexema;
	}
	
	@Override
	public String toString() {
		return "<" + nome +","+lexema+">";
	}

}
