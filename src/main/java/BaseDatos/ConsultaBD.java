package BaseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.sql.DataSource;
import javax.swing.JOptionPane;

public class ConsultaBD {

	PoolConexiones pool = new PoolConexiones();
	DataSource datasource;
	Connection con = null;

	public String consultarToGson(String consulta) {
		datasource = pool.CrearConexiones();
		String resultado = "[";
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(consulta);
			int numColumnas = rs.getMetaData().getColumnCount();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					resultado += "{\"";
					for (int i = 1; i <= numColumnas; i++) {
						if (i != 1)
							resultado += ",\"";
						if (rs.getMetaData().getColumnTypeName(i).equals("VARCHAR")) {
							resultado += rs.getMetaData().getColumnLabel(i) + "\":\"";
							resultado += rs.getString(i) + "\"";
						} else {
							resultado += rs.getMetaData().getColumnLabel(i) + "\":";
							resultado += rs.getString(i);
						}
						if (i == numColumnas)
							resultado += "},";
					}
				}

				return resultado.substring(0, resultado.length() - 1) + "]";
			} else {
				return "";
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * Insert generico a la base de datos
	 * @param objetos array de Object, si se quiere meter un objeto se debe transformar en un array de Object, no pueden meterse matrizes, utilizar un for para eso
	 * @param nombreTabla nombre de la tabla a la cual se desea realizar un insert
	 */
	public void insertGenerico(Object[] objetos, String nombreTabla) {
		datasource = pool.CrearConexiones();
		try {
			con = datasource.getConnection();
			Class[] clasesObj = new Class[objetos.length];
			String query = "insert into " + nombreTabla + " values(";

			for (int i = 0; i < objetos.length; i++) {
				clasesObj[i] = objetos[i].getClass();
				query += "?,";
			}

			query = (query.substring(0, query.length() - 1)) + ");";

			PreparedStatement statementGenerico = this.con.prepareStatement(query);
			for (int i = 0; i < objetos.length; i++) {
				if (clasesObj[i] == String.class) {
					statementGenerico.setString(i + 1, (String) objetos[i]);
				} else if (clasesObj[i] == Float.class) {
					statementGenerico.setFloat(i + 1, (Float) objetos[i]);
				} else if (clasesObj[i] == Double.class) {
					statementGenerico.setDouble(i + 1, (Double) objetos[i]);
				} else if (clasesObj[i] == Integer.class) {
					statementGenerico.setInt(i + 1, (int) objetos[i]);
				} else if (clasesObj[i] == java.util.Date.class) {
					statementGenerico.setDate(i + 1, new java.sql.Date(((java.util.Date) objetos[i]).getTime()));
				} else {
					statementGenerico.setString(i + 1, (String) objetos[i]);
				}
			}
			statementGenerico.executeUpdate();
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error", 0);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
