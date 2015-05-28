package tpvConsola;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

	public class GestioStock {
		
		static Scanner teclat=new Scanner (System.in);
		
		
		
		//buscar producte amb el codi de barres passant-li el codi
		public static Producte buscarProd(String codi){
			Producte prod;
			try {
						
				String sql="SELECT * from producte WHERE codi='"+codi+"'";
				
				ResultSet rs=BD.executarConsulta(sql);
				
				rs.last();
				int files=rs.getRow();
				if(files==1){
					rs.beforeFirst();
					while(rs.next()){
						prod=new Producte(rs.getString("codi"),rs.getString("descripcio"),rs.getDouble("preu_sense"),
								rs.getDouble("preu_amb"),rs.getDouble("iva"),rs.getInt("stock"));
						return prod;
						}
					
				}
			} catch (NumberFormatException e) {
				System.out.println("Codi invàlid. Has d'introduir números.");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		//buscar producte amb la descripciÃ³
		public static Producte buscarProdNom(){
			Producte [] prod=new Producte [1];
			try {
				System.out.println("Introdueix la descripció del producte: ");
				String nom=teclat.nextLine();
			
				String sql="SELECT * from producte WHERE descripcio='"+nom+"'";
				
				ResultSet rs=BD.executarConsulta(sql);
				
				rs.last();
				int files=rs.getRow();
				if(files==1){
					rs.beforeFirst();
					while(rs.next()){
						prod [0]=new Producte(rs.getString("codi"),rs.getString("descripcio"),rs.getDouble("preu_sense"),
								rs.getDouble("preu_amb"),rs.getDouble("iva"),rs.getInt("stock"));
						return prod[0];
						}
					
				}
			} catch (NumberFormatException e) {
				System.out.println("Codi invàlid. Has d'introduir números.");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
			

	//---------------------------------------------------------------------------------------------------------------				

		
		//buscar producte amb el codi de barres
		public static Producte buscarProd(){
			Producte [] prod=new Producte [1];
			try {
				System.out.println("Introdueix el codi de barres del producte: ");
				String codi=teclat.nextLine();
			
				String sql="SELECT * from producte WHERE codi='"+codi+"'";
				
				ResultSet rs=BD.executarConsulta(sql);
				
				rs.last();
				int files=rs.getRow();
				if(files==1){
					rs.beforeFirst();
					while(rs.next()){
						prod [0]=new Producte(rs.getString("codi"),rs.getString("descripcio"),rs.getDouble("preu_sense"),
								rs.getDouble("preu_amb"),rs.getDouble("iva"),rs.getInt("stock"));
						return prod[0];
						}
					
				}
			} catch (NumberFormatException e) {
				System.out.println("Codi invàlid. Has d'introduir números.");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
			
			//Afegir producte
			static public void afegirProd(){
				ArrayList <Producte> productes = new ArrayList <Producte> ();
				int num;
				do {
					System.out.println("Quants productes vols indtroduir?");
					num=Integer.parseInt(teclat.nextLine());
					if(num<=0){
						System.out.println("S'ha d'introduir una quantitat major a 0.");
					}
				} while (num<=0);
				
				for (int i = 0; i < num; i++) {
					Producte prod = new Producte();
					if(!prod.getCodiBarres().equalsIgnoreCase("")){
						productes.add(prod);
					}
				}
				//Insertar a base de dades
				Producte get;
				String insert="";
				for(int i=0;i<productes.size();i++){
					get=productes.get(i);
					insert=insert+"('"+get.getCodiBarres()+"','"+get.getDescripcio()+"','"+get.getPreuSense()+"','"+get.getPreuAmb()+"','"+get.getIva()+"','"+get.getstock()+"'),";
				}
				
				String sql="INSERT INTO producte VALUES"+insert;
				sql= sql.substring(0, sql.length()-1);
				BD.modificarBD(sql);
			}
			
			//modificar dades producte
			public static void modificarProd(){
				Producte prod=buscarProd();
				if(prod==null){
					System.out.println("No s'ha trobat el producte.");
				}else{
					int opcio;
					String sql;
					String resp;
					do {
						String insert="";
						System.out.println("Que vols modificar?\n"
								+ "1. Codi de barres \n"
								+ "2. Descripció \n"
								+ "3. Preu base \n"
								+ "4. IVA\n"
								+ "0. Enrera");
						opcio=Integer.parseInt(teclat.nextLine());
						switch (opcio) {
						case 1:
							System.out.println("Nou codi de barres: ");
							String codi=teclat.nextLine();
							//Modificar bd
							sql="UPDATE producte SET codi='"+codi+"' WHERE codi='"+prod.getCodiBarres()+"'";
							BD.modificarBD(sql);
							break;
						case 2:
							System.out.println("Nova descripció: ");
							String descripcio=teclat.nextLine();
							//Modificar bd
							sql="UPDATE producte SET descripcio='"+descripcio+"' WHERE codi='"+prod.getCodiBarres()+"'";
							BD.modificarBD(sql);
							break;
						case 3:
							System.out.println("Nou preu base: ");
							double preu=Double.parseDouble(teclat.nextLine());
							//Modificar bd
							sql="UPDATE producte SET preu_sense="+preu+" WHERE codi='"+prod.getCodiBarres()+"'";
							BD.modificarBD(sql);
							break;
						case 4: 
							boolean noValid;
							int iva;
							do {
								System.out.println("Introdueix nou IVA:");
								System.out.println("1. tipus general (21%)");
								System.out.println("2. Tipus reduït (10%)");
								System.out.println("3. Tipus superreduït (4%)");
								iva=Integer.parseInt(teclat.nextLine());
								noValid=false;
								switch (iva) {
								case 1:
									iva=21;
									break;
								case 2:
									iva=10;
									break;
								case 3:
									iva=4;
									break;
						
								default:
									System.out.println("Opció no vàlida");
									noValid=true;
									break;
								}
							} while (noValid);
							//Modificar bd
							sql="UPDATE producte SET iva="+iva+" WHERE codi='"+prod.getCodiBarres()+"'";
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
			
			//Eliminar producte
			public static void eliminarProd(){
				String resposta;
				do {
					Producte codiBarres=buscarProd();
						if(codiBarres==null){
							System.out.println("No s'ha trobat el producte.");
						}
						else{
							String sql="DELETE FROM producte WHERE codi='"+codiBarres.getCodiBarres()+"'";
							BD.modificarBD(sql);
							System.out.println("Producte eliminat!!");
						}
					
					System.out.println("Vols eliminar algún producte més?");
					resposta=teclat.nextLine();
				} while (Tpv.validarSiNo(resposta));
			}
			
			//Aumentar quantitat producte
			public static void augmentarStock(){
				String resposta;
				int stock;
				do {
					Producte codiBarres=buscarProd();
					if(codiBarres==null){
						System.out.println("No s'ha trobat el producte.");
					}
					else{
						do{
							System.out.println("Introdueix quantitat:");
							stock=Integer.parseInt(teclat.nextLine());
							if(stock<=0){
								System.out.println("S'ha d'introduir una quantitat major a 0.");
							}
						}while(stock<=0);
						String sql="UPDATE producte SET stock="+stock+" WHERE codi='"+codiBarres.getCodiBarres()+"'";
						BD.modificarBD(sql);
						System.out.println("Stock afegit!");
					}
					System.out.println("Vols modificar stock d'algún producte més?");
					resposta=teclat.nextLine();
				} while (Tpv.validarSiNo(resposta));
			}
			
			//MenÃº gestiÃ³ de stocks
			public static void menuStock() {
				int opcio;
				boolean valid;
				do{
					try{
						do {
							System.out.println("        MENÚ  \n"
								+ "---------------------------\n"
								+ "1. Afegir nou producte \n"
								+ "2. Modificar producte\n"
								+ "3. Eliminar producte\n"
								+ "4. Augmentar quantitat de producte\n"
								+ "0. Anar al menú principal\n"
								+ "Elegeix una opció");
							opcio=Integer.parseInt(teclat.nextLine());
							
							switch (opcio) {
								case 1:
									GestioStock.afegirProd();
									break;
								case 2:
									GestioStock.modificarProd();
									break;
								case 3:
									GestioStock.eliminarProd();
									break;
								case 4:
									GestioStock.augmentarStock();
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
							System.out.println("ERROR GENERAL. ");
							valid=false;
					}
				}while(valid==false);
			}
}
