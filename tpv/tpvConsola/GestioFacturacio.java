package tpvConsola;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class GestioFacturacio {

	static Scanner teclat=new Scanner (System.in);
	static ArrayList<Factura> factures=new ArrayList<Factura>();
	//Generar factura
	public static void generarFactura(){
		Factura f=new Factura();
		factures.add(f);
	}
	
	//mostrar factura
	public static void mostrarFactura(){
		System.out.println("Introdueix dni:");
		String dni=teclat.nextLine();
		buscarFactura(dni);
		for (int i = 0; i < factures.size(); i++) {
			factures.get(i).mostrarFactura(dni);
			System.out.println();
		}
	}
	
	//buscar factura
			public static ArrayList<Factura> buscarFactura(String dni){
				factures.clear();
				String sql="SELECT distinct f.n_factura,f.data_emisio,f.total_factura FROM factura f,tiquet t WHERE t.cif='"+dni+"' and t.n_factura=f.n_factura";
				ResultSet rs=BD.executarConsulta(sql);
				try {
					while(rs.next()){
						factures.add(new Factura(rs.getString(1),rs.getString(2) , rs.getDouble(3)));
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return factures;
			}
		
	//ventes en un temps agrupats per clients
			public static void mostrarFacturesClientsPerTemps(){
				System.out.println("Introdueix data inici(AAAA-MM-DD):");
				String dataInici=teclat.nextLine();
				System.out.println("Introdueix data final(AAAA-MM-DD):");
				String dataFinal=teclat.nextLine();
				String sql="select f.data_emisio,t.cif,c.rao_social,f.n_factura,f.total_factura from factura f,tiquet t,clients c where f.n_factura=t.n_factura and t.cif=c.cif and data_emisio between '"+dataInici+"' and '"+dataFinal+"' order by 2,1";
			
				ResultSet rs=BD.executarConsulta(sql);
				try {
					rs.last();
					if(rs.getRow()==0){
						System.out.println("No s'han trobat ventes");
					}
					else{
						System.out.println("Data emisio\t"+"Dni/cif Nom/Raó social\t\t"+"Numero factura\t\t"+"Total");
						rs.beforeFirst();
						while(rs.next()){
							System.out.println(rs.getString("data_emisio")+"\t"+rs.getString("cif")+"\t"+rs.getString("rao_social")+"\t\t\t"+rs.getString("n_factura")+"\t\t\t"+rs.getDouble("total_factura"));
							System.out.println();
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	
		//ventes en un temps agrupats d'un client
			public static void mostrarFacturesDunClientPerTemps(){
				System.out.println("Introduiex dni/cif:");
				String dni=teclat.nextLine();
				if(GestioClients.buscarClient(dni)==null){
					System.out.println("No s'ha trobat client");
				}
				else{
					System.out.println("Introdueix data inici(AAAA-MM-DD):");
					String dataInici=teclat.nextLine();
					System.out.println("Introdueix data final(AAAA-MM-DD):");
					String dataFinal=teclat.nextLine();
					String sql="select f.data_emisio,t.cif,c.rao_social,f.n_factura,f.total_factura from factura f,tiquet t,clients c where f.n_factura=t.n_factura and c.cif='"+dni+"' and t.cif=c.cif and data_emisio between '"+dataInici+"' and '"+dataFinal+"' order by 2,1";
				
					ResultSet rs=BD.executarConsulta(sql);
					try {
						rs.last();
						if(rs.getRow()==0){
							System.out.println("No s'han trobat factures");
						}
						else{
							System.out.println("Data emisio\t"+"Dni/cif Nom/Raó social\t\t"+"Numero factura\t\t"+"Total");
							rs.beforeFirst();
							while(rs.next()){
								System.out.println(rs.getString("data_emisio")+"\t"+rs.getString("cif")+"\t"+rs.getString("rao_social")+"\t\t\t"+rs.getString("n_factura")+"\t\t\t"+rs.getDouble("total_factura"));
								System.out.println();
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		//top ventes
			public static void mostrarTopVentes(){
				System.out.println("Introdueix data inici(AAAA-MM-DD):");
				String dataInici=teclat.nextLine();
				System.out.println("Introdueix data final(AAAA-MM-DD):");
				String dataFinal=teclat.nextLine();
				String sql="select sum(li.quantitat), p.descripcio from linia_tiquet li,producte p,tiquet t where li.codi_prod=p.codi and li.id_tiquet=t.id and t.data between '"+dataInici+"' and '"+dataFinal+"' group by 2 order by 1 desc";
			
				ResultSet rs=BD.executarConsulta(sql);
				try {
					rs.last();
						if(rs.getRow()==0){
							System.out.println("No s'han trobat productes en eixe interval de temps");
						}
						else{
							System.out.println("Quantitat\t"+"Productes");
							rs.beforeFirst();
							while(rs.next()){
								System.out.println(rs.getString("sum(li.quantitat)")+"\t\t"+rs.getString("descripcio"));
								System.out.println();
							}
						}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	//menu llistats factures
	public static void menuLlistatsFactura(){
		int opcio;
		boolean valid;
		do{
			try{
				do {
					System.out.println("        MENÚ  \n"
							+ "---------------------------\n"
							+ "1. Ventes en interval agrupats per clients\n"
							+ "2. Ventes realitzades per 1 client en un interval del temps\n"
							+ "3. Productes més venuts en un interval de temps\n"
							+ "0.Exir");
						opcio=Integer.parseInt(teclat.nextLine());
					
					switch (opcio) {
						case 1:
							mostrarFacturesClientsPerTemps();
							break;
						case 2:
							mostrarFacturesDunClientPerTemps();
							break;
						case 3:
							mostrarTopVentes();
							break;
						case 0:
							break;
						default:
							System.out.println("ERROR. Elegeix una opció vàlida.");
							break;
					}
				}while (opcio!=0);
				valid=true;
			}catch(NumberFormatException e){
					System.out.println("ERROR. Codi invàlid. S'ha de posar un número. ");
					valid=false;
			}catch (Exception e){
					valid=false;
			}
		}while(valid==false);
		}
	
	
	//menu gestio facturacio
		public static void menuGestioFacturacio(){
			int opcio;
			boolean valid;
			do{
				try{
					do {
						System.out.println("        MENÚ  \n"
							+ "---------------------------\n"
							+ "1. Generar factura \n"
							+ "2. Mostrar factures dels clients \n"
							+ "3. Llistats factura\n"
							+ "0.Exir");
						opcio=Integer.parseInt(teclat.nextLine());
						
						switch (opcio) {
							case 1:
								generarFactura();
								break;
							case 2:
								mostrarFactura();
								break;
							case 3:
								menuLlistatsFactura();
								break;
							case 0:
								break;
							default:
								System.out.println("ERROR. Elegeix una opció vàlida.");
								break;
						}
					}while (opcio!=0);
					valid=true;
				}catch(NumberFormatException e){
						System.out.println("ERROR. Codi invàlid. S'ha de posar un número. ");
						valid=false;
				}catch (Exception e){
						valid=false;
				}
			}while(valid==false);
		}
	}

