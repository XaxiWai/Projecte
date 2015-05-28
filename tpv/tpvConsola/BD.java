package tpvConsola;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BD {
	//dades de la base de dades
	private static String usuari = "root";
	private static String contrasenya="";
	private static String url="jdbc:mysql://localhost/tpv";
	
	//Objectes que necessitem per interactuar amb la base de dades
	static Connection con;
	static Statement st;
	
	//Obri la connexió
	public static Connection connexioBD(){
		
		try {
			Class.forName("com.mysql.jdbc.Driver"); //carrega driver
			con=DriverManager.getConnection(url, usuari, contrasenya); //obri connexi�
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR. No troba la classe");
		} catch (SQLException e) {
			System.out.println("Falla la connexió");
		}
		
		return con;
	}
	
	//Métode per a realitzar consultes
	public static ResultSet executarConsulta(String sql){
		Connection con =connexioBD();
		Statement st;
		ResultSet resultat=null;
		try {
			st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			resultat=st.executeQuery(sql);
			
		} catch (SQLException e) {
			System.out.println("Problemes en la creació del Statement(consulta)");
		}
		return resultat;
	}
	
	//M�tode per a modificacions (eliminar, insertar i actualitzar)
	public static void modificarBD(String sql){
		Connection con =connexioBD();
		Statement st =null;
		try {
			st=con.createStatement();
			st.executeUpdate(sql);
			
		} catch (SQLException e) {
			System.out.println("Error al crear el Statement (modificar)");
			e.printStackTrace();
		}
	}
	//mètode per traure el número de registres insertats en la BD.
			public static int comptarRegistresBD(String sql){
				Connection con=connexioBD();
				Statement st;
				ResultSet resultat=null;
				int num=0;
				try {
					st=con.createStatement();
					resultat= st.executeQuery(sql);
					resultat.last();
					num=resultat.getRow();
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return num;
			}

}
