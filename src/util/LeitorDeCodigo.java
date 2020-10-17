package util;

public class LeitorDeCodigo {
	
	String codigo;
	String lexema;
	int bufferAtual = 1;
	int ponteiroCodigo;
	int iniciolexema;
	int tCod;
	int ponteiroBuffer;
	
	public String buffer[];
	public int contadorLinha;
	
	public void carregaCodigo(String codigo) {
		
		this.codigo = codigo;
		tCod = codigo.length()-1;
		
		inicializarBuffer();
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
	
	public void inicializarBuffer() {
		bufferAtual = 2;
		iniciolexema = 0;
		lexema = "";
		buffer = new String [40];
		ponteiroBuffer = 0;
		carregaBuffer1();
		contadorLinha = 1;
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
		
		String c = lerCaracterDoBuffer();
		
		if(c != null)
			lexema += c;
		
		return c;
	}
	
	public void zerar() {
		ponteiroBuffer = iniciolexema;
		lexema = "";
	}
	
	public void confirmar() {
		iniciolexema = ponteiroBuffer;
		
//		if(lexema.equals("\n")) {
//			contLinha++;
//			contadorLinha = contLinha;
//		}
		
		lexema = "";
	}
	
	public String Lexema() {
		return lexema;
	}

}
