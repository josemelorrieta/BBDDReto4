package BaseDatos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.sql.DataSource;

public class ConsultaBBD {
	
	PoolConexiones pool = new PoolConexiones();
	DataSource datasource;
	Connection con = null;

	public Object[] consultarToArray(String consulta) {
		
		datasource = pool.CrearConexiones();
		try {
			con = datasource.getConnection();
		    Statement st = con.createStatement();
		    ResultSet rs = st.executeQuery(consulta);
		    ArrayList<Object[]> datosRs = new ArrayList<Object[]>();
		    while (rs.next()) {
				int numColumnas = rs.getMetaData().getColumnCount();
				Object[] arr = new Object[numColumnas];
				for (int i = 0; i < numColumnas; i++) {
				    arr[i] = rs.getObject(i + 1);
			}
			datosRs.add(arr);
		    }
		    Object[] resultado = datosRs.toArray(new Object[datosRs.size()]);
		    return resultado;

		} catch (SQLException e) {
			e.printStackTrace();
		    return null;
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
