package util;

public class LeitorDeCodigo {
	
	
	int bufferAtual = 1;
	
	String codigo;
	int ponteiroCodigo;
	int ponteiroBuffer = 0;
	int iniciolexema;
	String lexema;
//	public String buffer[] = new String [40];
	public String buffer[];
	int estadoBuffer = 2;
	
	int tCod;
	
	public void carregaCodigo(String codigo) {
		
		this.codigo = codigo;
		tCod = codigo.length()-1;
		
		inicializarBuffer();
	}
	
//	public String proximoCaracter() {
//		
//		String c = "";
//		
//		if(buffer != null) {
//			
//			if(estadoBuffer == 1 && ponteiroBuffer < buffer.length/2) {
//				c = buffer[ponteiroBuffer];
//				ponteiroBuffer++;
//				return c;
//			
//			}else if(estadoBuffer == 1 && ponteiroBuffer >= buffer.length/2) {
//				carregaBuffer2();
//				c = buffer[ponteiroBuffer];
//				ponteiroBuffer++;
//				return c;
//				
//			}else if(estadoBuffer == 2 && ponteiroBuffer < buffer.length) {
//				c = buffer[ponteiroBuffer];
//				ponteiroBuffer++;
//				return c;
//
//			}else if(estadoBuffer == 2 && ponteiroBuffer == buffer.length) {
//				carregaBuffer1();
//				ponteiroBuffer = 0;
//				c = buffer[ponteiroBuffer];
//				ponteiroBuffer++;
//				return c;
//				
//			}
//			
//		}else {
//			carregaBuffer1();
//		}
//		
//		return c;
//	
//	}

//	public void carregaBuffer1() {
//		
//		estadoBuffer = 1;
//		
//		for(int i=0; i<buffer.length/2;i++) {
//			buffer[i] = codigo.substring(ponteiroCodigo);
//			ponteiroCodigo++;
//		}
//		
//	}
//	
//	public void carregaBuffer2() {
//		
//		estadoBuffer = 2;
//		
//		for(int i = buffer.length/2; i<buffer.length;i++) {
//			buffer[i] = codigo.substring(ponteiroCodigo);
//			ponteiroCodigo++;
//		}
//		
//	}
	
	public void carregaBuffer1() {
		if(bufferAtual == 2) {
			bufferAtual = 1;
			
			for(int i = 0; i < buffer.length/2; i++) {
				
				if(ponteiroCodigo <= tCod) {
					buffer[i] = Character.toString(codigo.charAt(ponteiroCodigo));
					ponteiroCodigo++;
					
				}else {
					break;
				}
			}
			
		}
	}
	
	public void carregaBuffer2() {
		if(bufferAtual == 1) {
			bufferAtual = 2;
			
			for(int i = buffer.length/2; i <= buffer.length;i++) {
				
				if(ponteiroCodigo <= tCod) {
				buffer[i] = Character.toString(codigo.charAt(ponteiroCodigo));
				ponteiroCodigo++;
				}else{
					break;
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
	}
	
	public String lerCaracterDoBuffer() {
		
		String c = buffer[ponteiroBuffer];
		ponteiroBuffer++;
		
		if(ponteiroBuffer == buffer.length/2) {
			carregaBuffer2();
		}else if (ponteiroBuffer == buffer.length) {
			carregaBuffer1();
		}
		
//		if(c == null) {
//			return c = "-1";
//		}else {
			return c;
//		}
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
		lexema = "";
	}
	
	public String Lexema() {
		return lexema;
	}
	
	

}
