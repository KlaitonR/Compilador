package util;

public class LeitorDeCodigo {
	
	public String buffer[];
	String codigo;
	String lexema;
	public int bufferAtual = 1;
	public int ponteiroCodigo;
	public int iniciolexema;
	public int tCod;
	public int ponteiroBuffer;
	
	public String analise = "";
	public String terminal = "";
	public int contadorLinha;
	
	public void carregaCodigo(String codigo) {
		
		this.codigo = codigo;
		tCod = codigo.length()-1;
		bufferAtual = 2;
		iniciolexema = 0;
		lexema = "";
		buffer = new String [40];
		ponteiroBuffer = 0;
		carregaBuffer1();
		contadorLinha = 1;
	}
	
	public void carregaBuffer1() {
		
		if(bufferAtual == 2) {
			bufferAtual = 1;
			
			for(int i = 0; i < buffer.length/2; i++) {
				
				if(ponteiroCodigo <= tCod) {
					buffer[i] = Character.toString(codigo.charAt(ponteiroCodigo));
					ponteiroCodigo++;
				}else {
					buffer[i] = null;
				}
			}
		}
	}
	
	public void carregaBuffer2() {
		if(bufferAtual == 1) {
			bufferAtual = 2;
			
			for(int i = buffer.length/2; i < buffer.length ;i++) {
				
				if(ponteiroCodigo <= tCod) {
				buffer[i] = Character.toString(codigo.charAt(ponteiroCodigo));
				ponteiroCodigo++;
				}else{
					buffer[i] = null;
				}
			}
		}
	}
	
	public void retroceder() {
		
		ponteiroBuffer--;
		lexema = lexema.substring(0, lexema.length() -1);
		
		if(ponteiroBuffer < 0) {
			ponteiroBuffer = buffer.length -1;
		}
	}
	
	public String lerCaracterDoBuffer() {
		
		String c = buffer[ponteiroBuffer];
  		ponteiroBuffer++;
	
		if(ponteiroBuffer == (buffer.length/2)) {
			carregaBuffer2();
		}else if (ponteiroBuffer == buffer.length) {
			carregaBuffer1();
			ponteiroBuffer = 0;
		}
		
			return c;
			
	}
	
	public String proximoCaracter() {
		
		if(buffer != null) { // Fase do Analisador l�xico
		
			exibeBuffer();
			String c = lerCaracterDoBuffer();
			if(c != null)
				lexema += c;
			
			return c;
			
		}else { // Fase do analisador sintatico
			
			buffer = new String [40];
			carregaBuffer1();
			exibeBuffer();
			String c = lerCaracterDoBuffer();
			if(c != null)
				lexema += c;
			
			return c;
		}
	}
	
	public void zerar() {
		ponteiroBuffer = iniciolexema;
		lexema = "";
	}
	
	public void confirmar() {
		iniciolexema = ponteiroBuffer;
		lexema = "";
	}
	
	public String Lexema() {
		return lexema;
	}
	
	public void exibeBuffer() {
		
		String buffer = "";
		buffer += "\n" + analise + "\n";
	
		for(int x = 0; x<this.buffer.length; x++) {
			
			if(x == iniciolexema && x != ponteiroBuffer) {
				buffer += "  *";
			}else if(x == ponteiroBuffer && x != iniciolexema){
				buffer += "   v";
			}else if (x == iniciolexema && x == ponteiroBuffer){
				buffer += "  $";
			}else {
				buffer += "    ";
			}
		}
		
		buffer += "\n| ";
		
		for(int x = 0; x<this.buffer.length; x++) {
			
			if(this.buffer[x] == null)
				buffer += "- | ";
			else if (this.buffer[x].equals("\t"))
				buffer += "  | ";
			else if (this.buffer[x].equals("\n"))
				buffer += "  | ";
			else
				buffer += this.buffer[x] + " | ";
		}
		
		terminal += buffer + "\n";
	}
}
