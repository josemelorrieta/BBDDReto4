package BaseDatos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.sql.DataSource;

public class ConsultaBD {
	
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
	 
	public String consultarToGson(String consulta) {
		datasource = pool.CrearConexiones();
		String resultado = "[";
		try {
			con = datasource.getConnection();
		    Statement st = con.createStatement();
		    ResultSet rs = st.executeQuery(consulta);
		    int numColumnas = rs.getMetaData().getColumnCount();
		    if(rs.isBeforeFirst()) {
			    while (rs.next()) {
			    	resultado += "{\"";
			    	for (int i=1;i<=numColumnas;i++) {
			    		if (i!=1)
			    			resultado += ",\"";
			    		if (rs.getMetaData().getColumnTypeName(i).equals("VARCHAR")) {
			    			resultado += rs.getMetaData().getColumnName(i) + "\":\"";
			    			resultado += rs.getString(i) + "\"";
			    		} else {
			    			resultado += rs.getMetaData().getColumnName(i) + "\":";
			    			resultado += rs.getString(i);
			    		}
			    		if (i==numColumnas)
			    			resultado += "},";
			    	}
			    }
			    
			    return resultado.substring(0, resultado.length()-1) + "]";
		    } else {
		    	return "";
		    }
		    
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
