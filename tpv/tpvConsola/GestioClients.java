package tpvConsola;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class GestioClients {
	
	static Scanner teclat=new Scanner (System.in);
	static ArrayList <Client> clients = new ArrayList <Client> ();

	
	//buscar client pel codi i retorna un String
	public static Client buscarClient(String codi){
		ResultSet rs; 
		Client cli;
		String sql="select * from clients where cif='"+codi+"'";
		rs=BD.executarConsulta(sql);
		try {
			rs.last();
			if(rs.getRow()==1){
				cli= new Client(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getBoolean(5));
				return cli;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;  //entre cometes perquÃ¨ retorna string.
	}
			
		//Registrar client
			public static void registrarClient(){
				String resposta;
				Client get;
				String insert="";
				
				do{
					clients.add(new Client());
					System.out.println("Vols registrar altre client?");
					resposta=teclat.nextLine();
				}while(Tpv.validarSiNo(resposta));
				
				//Insertar a base de dades
				for(int i=0;i<clients.size();i++){
					get=clients.get(i);
					insert=insert+"('"+get.getCif()+"','"+get.getRaoSocial()+"','"+get.getDomicili()+"','"+get.getDataAlta()+"',"+get.isBaixa()+"),";
				}
				
				String sql="INSERT INTO clients VALUES"+insert;
				sql= sql.substring(0, sql.length()-1);
				BD.modificarBD(sql);
				clients.clear();
			}
			
			//modificar client
			public static void modificarClient(){
				System.out.println("Introdueix CIF:");
				String buscarCif=teclat.nextLine();
				Client client=buscarClient(buscarCif);
				String codiRetornat;
				if(client == null){
					System.out.println("No s'ha trobat el client.");
				}else{
					int opcio;
					String sql;
					String resp;
					do {
						System.out.println("Que vols modificar?\n"
								+ "1. CIF \n"
								+ "2. Raó social/nom \n"
								+ "3. Domicili \n"
								+ "0. Eixir");
						opcio=Integer.parseInt(teclat.nextLine());
						
						codiRetornat=client.getCif();
						switch (opcio) {
						case 1:
							System.out.println("Nou CIF: ");
							String cif=teclat.nextLine();
							//Modificar bd
							sql="UPDATE clients SET cif='"+cif+"' WHERE cif='"+codiRetornat+"'";
							BD.modificarBD(sql);
							break;
						case 2:
							System.out.println("Nova raó social/nom: ");
							String nom=teclat.nextLine();
							//Modificar bd
							sql="UPDATE clients SET rao_social='"+nom+"' WHERE cif='"+codiRetornat+"'";
							BD.modificarBD(sql);
							break;
						case 3:
							System.out.println("Nou domicili: ");
							String domicili=teclat.nextLine();
							//Modificar bd
							sql="UPDATE clients SET domicili='"+domicili+"' WHERE cif='"+codiRetornat+"'";
							BD.modificarBD(sql);
							break;
						case 0:
							break;
						default:
							System.out.println("Opció no vàlida");
							break;
						}
						System.out.println("Vols modificar alguna cosa més d'aquest producte? (si/no)");
						resp=teclat.nextLine();
					} while (Tpv.validarSiNo(resp));
				}
			}
			
			//_----------------------------------------------------------------------------------------------------
			
			//mostrar client
			public static void mostrarClient(){
				System.out.println("Introdueix CIF:");
				String buscarCif=teclat.nextLine();
				String codiRetornat=buscarClient(buscarCif).getCif();
				
				if(codiRetornat.equalsIgnoreCase("-1")){
					System.out.println("No s'ha trobat el client.");
				}
				else{
					String sql="SELECT * FROM clients WHERE cif='"+codiRetornat+"'";
					ResultSet rs=BD.executarConsulta(sql);
				
					try {
						while(rs.next()){
							System.out.println("Client:\n"
												+"\nCIF: "+rs.getString(1)
												+"\nRaó social/nom: "+rs.getString(2)
												+"\nDomicili: "+rs.getString(3)
												+"\nData d'alta: "+rs.getString(4));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
					
			}
			
			//donar de baixa client
			
					public static void baixaClient(){
						String resposta;
						System.out.println("Introdueix CIF:");
						String buscarCif=teclat.nextLine();
						String codiRetornat=buscarClient(buscarCif).getCif();
						
						if(codiRetornat.equalsIgnoreCase("-1")){
							System.out.println("No s'ha trobat el client.");
						}
						else{
							String sql="SELECT * FROM clients WHERE cif='"+codiRetornat+"'";
							ResultSet rs=BD.executarConsulta(sql);
						
							try {
								while(rs.next()){
									System.out.println("Client:\n"
														+"\nCIF: "+rs.getString(1)
														+"\nRaÃ³ social/nom: "+rs.getString(2)
														+"\nDomicili: "+rs.getString(3)
														+"\nData d'alta: "+rs.getString(4));
								}
								
								System.out.println("Esta segur que vol donar de baixa al client?(si/no)");
								resposta=teclat.nextLine();
									if(Tpv.validarSiNo(resposta)){
										sql="UPDATE clients SET baixa="+true+" WHERE cif='"+codiRetornat+"'";
										BD.modificarBD(sql);
										System.out.println("Client donat de baixa");
									}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
							
					}
					
					//MenÃº gestiÃ³ de clients
					public static void menuGestioClients() {
						int opcio;
						boolean valid;
						do{
							try{
								do {
									System.out.println("        MENÚ  \n"
										+ "---------------------------\n"
										+ "1. Registrar client \n"
										+ "2. Modificar client\n"
										+ "3. Mostrar client\n"
										+ "4. Baixa client\n"
										+ "0. Anar al menú principal\n"
										+ "Elegeix una opció");
									opcio=Integer.parseInt(teclat.nextLine());
									
									switch (opcio) {
										case 1:
											registrarClient();
											break;
										case 2:
											modificarClient();
											break;
										case 3:
											mostrarClient();
											break;
										case 4:
											baixaClient();
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
