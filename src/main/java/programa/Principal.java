package programa;

import BaseDatos.ConsultaBD;

public class Principal {
	
	public static void main(String[] args) {
		
		ConsultaBD consulta = new ConsultaBD();
		
		String query = "SELECT * FROM `hotel` WHERE 1";

		System.out.println(consulta.consultarToGson(query));

	}

}
