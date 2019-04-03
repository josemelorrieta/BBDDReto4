package programa;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import BaseDatos.PoolConexiones;

public class Principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		PoolConexiones pool = new PoolConexiones();
		DataSource dataSource = pool.CrearConexiones();
		Connection con = null;
		ResultSet rs = null;
		

		try {
			con = dataSource.getConnection();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		rs = pool.generarConsulta(con, "SELECT Nombre FROM linea");
		
		try {
			while(rs.next()) {
				System.out.println(rs.getString("Nombre"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
