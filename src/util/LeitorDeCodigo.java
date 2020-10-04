package util;

public class LeitorDeCodigo {
	
	String codigo;
	int ponteiroCodigo = 0;
	int ponteiroBuffer = 0;
	String lexema;
	public String buffer[] = new String [40];
	int estadoBuffer = 1;
	
	public void carregaCodigo(String codigo) {
		
		this.codigo = codigo;
	}
	
	public String proximoCaracter() {
		
		String c = "";
		
		if(buffer != null) {
			
			if(estadoBuffer == 1 && ponteiroBuffer < buffer.length/2) {
				c = buffer[ponteiroBuffer];
				ponteiroBuffer++;
				return c;
			
			}else if(estadoBuffer == 1 && ponteiroBuffer >= buffer.length/2) {
				carregaBuffer2();
				c = buffer[ponteiroBuffer];
				ponteiroBuffer++;
				return c;
				
			}else if(estadoBuffer == 2 && ponteiroBuffer < buffer.length) {
				c = buffer[ponteiroBuffer];
				ponteiroBuffer++;
				return c;

			}else if(estadoBuffer == 2 && ponteiroBuffer == buffer.length) {
				carregaBuffer1();
				ponteiroBuffer = 0;
				c = buffer[ponteiroBuffer];
				ponteiroBuffer++;
				return c;
				
			}
			
		}else {
			carregaBuffer1();
		}
		
	return null;
				
	}

	public void carregaBuffer1() {
		
		estadoBuffer = 1;
		
		for(int i=0; i<buffer.length/2;i++) {
			buffer[i] = codigo.substring(ponteiroCodigo);
			ponteiroCodigo++;
		}
		
	}
	
	public void carregaBuffer2() {
		
		estadoBuffer = 2;
		
		for(int i = buffer.length/2; i<buffer.length;i++) {
			buffer[i] = codigo.substring(ponteiroCodigo);
			ponteiroCodigo++;
		}
		
	}
	
	public void retroceder() {
		
		ponteiroBuffer--;
		
	}

}
