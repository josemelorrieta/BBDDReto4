package programa;

import BaseDatos.ConsultaBD;

public class Principal {
	
	public static void main(String[] args) {
		
		ConsultaBD consulta = new ConsultaBD();
		
		String query = "SELECT * FROM `hotel` WHERE 1";
		
		Object[] misDatos = consulta.consultarToArray(query);
		
		for(int i=0;i<misDatos.length;i++) {
			System.out.println(misDatos[i].toString());
		}

	}

}
