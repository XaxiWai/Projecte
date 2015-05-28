package tpvConsola;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class GestioVentes {
	static Scanner teclat=new Scanner (System.in);
	
	//crear fitxer tiquet
		public static void fitxerTiquet(Tiquet tiquet){
			String fitxer="Tiquet_"+tiquet.getId();
			
			String ruta=fitxer+".txt";
			
			try {
				FileWriter escriure= new FileWriter(ruta);
				BufferedWriter escriptor = new BufferedWriter(escriure);
			
				
				escriptor.write("Id: "+tiquet.getId());
				escriptor.newLine();
				escriptor.write("Data: "+tiquet.getData());
				escriptor.newLine();
				escriptor.write("CLIENT:");
				escriptor.newLine();
				escriptor.write("DNI/CIF: "+tiquet.getClient().getCif());
				escriptor.newLine();
				escriptor.write("Nom/Raó social: "+tiquet.getClient().getRaoSocial());
				escriptor.newLine();
				escriptor.write("PRODUCTES:");
				escriptor.write("nº\tUnitats\t\tDescripció\tPreu/unitat");
				escriptor.newLine();
				for (int i = 0; i < tiquet.getProductes().size(); i++) {
					escriptor.write(tiquet.getProductes().get(i).toString());
					escriptor.newLine();
				}
				escriptor.write("--------------------------------------");
				escriptor.newLine();
				escriptor.write("TOTAL TIQUET");
				escriptor.newLine();
				escriptor.newLine();
				double suma=0;
				for (int i = 0; i < tiquet.getProductes().size(); i++) {
					LiniaTiquet p=tiquet.getProductes().get(i);
					suma=suma+p.calcularTotal();
				}
				
				double iva21=GestioVentes.SumarIva(tiquet.getProductes(), 21);
				double iva10=GestioVentes.SumarIva(tiquet.getProductes(), 10);
				double iva4= GestioVentes.SumarIva(tiquet.getProductes(), 4);
				double total=suma+iva21+iva10+iva4;
				escriptor.write("Base Imposable: \t"+suma);
				escriptor.newLine();
				escriptor.write("Iva general (21%): \t"+iva21);
				escriptor.newLine();
				escriptor.write("Iva reduït (10%): \t"+iva10);
				escriptor.newLine();
				escriptor.write("Iva superreduït (4%): \t"+ iva4);
				escriptor.newLine();
				escriptor.newLine();
				escriptor.write("TOTAL A PAGAR: \t"+total+"€");
				
				escriptor.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		//modificar linia tiquet
		public static void modificarLinia(Tiquet tiquet){
			LiniaTiquet lin = null;
			ResultSet rs;
			String sql;
			String codi=tiquet.getId();

			System.out.println("1. Afegir producte");
			System.out.println("2. Eliminar el producte");
			System.out.println("3. Canviar quantitat");
			System.out.println("0. Enrera");
			System.out.print("Tria una opció: ");
			int eleccio=Integer.parseInt(teclat.nextLine());
			
			switch (eleccio) {
				case 1:
					String numLinia = "";
					lin = new LiniaTiquet(codi);
					if( lin.getProd()!= null){
						
						try {
							//buscar quin número de linia va després
							sql="select n_linia from linia_tiquet where id_tiquet='"+codi+"'";
							rs=BD.executarConsulta(sql);
							while(rs.next()){
								numLinia=rs.getString("n_linia");
							}
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//Recuperar sols el numero de la linia
						int num=Integer.parseInt(numLinia.substring(1, numLinia.length()));
						num=num+1;
						numLinia="L"+num;
						//canviar el codi de linia de lin per a que no es repetixca
						lin.setCodiLinia(numLinia);
						
						//insertar a la bd
						sql="insert into linia_tiquet values ('"+lin.getCodiLinia()+"','"+tiquet.getId()+"',"+lin.getQuantitat()
								+","+(lin.getQuantitat()*lin.getProd().getPreuSense())+",'"+lin.getProd().getCodiBarres()+"')";
						BD.modificarBD(sql);
						
						//MODIFICAR EL TIQUET 
						
						//traure el total del tiquet (abans)
						sql="select total from tiquet where id='"+tiquet.getId()+"'";
						double totalTiquet = 0.0;
						try {
							rs=BD.executarConsulta(sql);
							while(rs.next()){
								totalTiquet = rs.getDouble("total");
							}
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//modificar el total del tiquet
						double total=totalTiquet+(lin.getProd().getPreuAmb()*lin.getQuantitat());
						total=Math.rint(total*100)/100; //arrodonir
						
						sql="UPDATE tiquet SET total="+total+" WHERE id='"+codi+"'";
						BD.modificarBD(sql);
					}
					break;
				case 2:
					//ELIMINAR LINIA
					double totalLinia = 0;
					lin=buscarLinia(tiquet);
					if(lin != null){
						//traure el total de la linia
						sql= "select preu_total from linia_tiquet where n_linia='"+lin.getCodiLinia()+"' and id_tiquet='"+codi+"'";
						rs=BD.executarConsulta(sql);
						try {
							while(rs.next()){
								totalLinia = rs.getDouble("preu_total");//sense iva
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						totalLinia= totalLinia*(1+(lin.getProd().getIva()/100)); //amb iva
						
						//Modificar bd
						sql="DELETE from linia_tiquet WHERE n_linia='"+lin.getCodiLinia()+"' and id_tiquet='"+codi+"'";
						BD.modificarBD(sql);
						
						//MODIFICAR EL TIQUET 
						
						//traure el total del tiquet (abans)
						sql="select total from tiquet where id='"+tiquet.getId()+"'";
						double totalTiquet = 0.0;
						try {
							rs=BD.executarConsulta(sql);
							while(rs.next()){
								totalTiquet = rs.getDouble("total"); //amb iva
							}
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//modificar el total del tiquet
						double total=totalTiquet-totalLinia;
						
						total=Math.rint(total*100)/100; //arrodonir
						
						sql="UPDATE tiquet SET total="+total+" WHERE id='"+codi+"'";
						BD.modificarBD(sql);
					}
					
					break;
				case 3:
					// CANVIAR QUANTITAT
					lin= buscarLinia(tiquet);
					if(lin != null){
						System.out.println("Inserta nova quantitat de producte: ");
						int quantitat=Integer.parseInt(teclat.nextLine());
						lin.setQuantitat(quantitat);
						quantitat=lin.getQuantitat();
						
						//traure el preu_total de la linia a modificar
						sql="select preu_total from linia_tiquet where n_linia='"+lin.getCodiLinia()+"' and id_tiquet='"+codi+"'";
						double total = 0;
						double totalTiquet = 0;
						try {
							rs= BD.executarConsulta(sql);
							while(rs.next()){
								total=rs.getDouble("preu_total");//sense iva
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						total=total*(1+(lin.getProd().getIva()/100)); //amb iva
						
						//Modificar linia
						totalLinia=lin.getProd().getPreuSense()*quantitat; //sense iva
						sql="UPDATE linia_tiquet SET quantitat="+quantitat+", preu_total="+(totalLinia)+
								" WHERE n_linia='"+lin.getCodiLinia()+"' and id_tiquet='"+codi+"'";
						BD.modificarBD(sql);
						
						totalLinia= totalLinia*(1+(lin.getProd().getIva()/100)); //amb iva
						
						//MODIFICAR TIQUET
						//traure el total del tiquet
						sql="select total from tiquet where id='"+tiquet.getId()+"'";
						try {
							rs= BD.executarConsulta(sql);
							while(rs.next()){
								totalTiquet=rs.getDouble("total");
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//modificar el total del tiquet
						total=(totalTiquet-total)+totalLinia;
						total=Math.rint(total*100)/100;
						sql="UPDATE tiquet SET total="+total+" WHERE id='"+codi+"'";
						BD.modificarBD(sql);
					}
					
					break;
				case 0:
					break;

				default:
					System.out.println("Opció no vàlida.");
					break;
			}
		}
		
		//preguntar linia
		public static LiniaTiquet buscarLinia(Tiquet tiquet){
			String linia;
			int quantitat;
			String prod;
			LiniaTiquet lin = null;
			ResultSet rs;
			String codi= tiquet.getId();
			
			System.out.println("CONTINGUT DEL TIQUET:");
			System.out.println("nº\tUnitats\t\tDescripció\tPreu/unitat");
			for (int i = 0; i <tiquet.getProductes().size() ; i++) {
				System.out.println(tiquet.getProductes().get(i).toString());
			}
			System.out.println("Id de la linia del tiquet a modificar: ");
			linia=teclat.nextLine();
			

			String sql="select * from linia_tiquet where n_linia='"+linia+"' and id_tiquet='"+codi+"'";
			rs=BD.executarConsulta(sql);
			try {
				rs.last();
				if(rs.getRow()!=0){
					rs.beforeFirst();
					while(rs.next()){
						linia = rs.getString(1);
						quantitat=rs.getInt(3);
						prod=rs.getString(5);
						lin=new LiniaTiquet(linia,quantitat,prod);
						return lin;
					}
				}else{
					System.out.println("La linia no existeix.");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		//modificar tiquet
		public static void modificarTiquet(){
			ResultSet rs;
			Tiquet tiquet=buscarTiquet();
			String codi;
			
			if(tiquet != null){
				 if (tiquet.isFacturat()){
					 System.out.println("No es poden modificar els tiquets facturats.");
				 }else{
					int opcio;
					String sql;
					String resp;
					LiniaTiquet lin = null;
					do {
						System.out.println("Que vols modificar?\n"
								+ "1. Client \n"
								+ "2. Productes \n"
								+ "0. Eixir");
						opcio=Integer.parseInt(teclat.nextLine());
						
						codi=tiquet.getId();
						switch (opcio) {
							case 1:
								System.out.println("CIF o DNI del client nou: ");
								String cif=teclat.nextLine();
								Client cli=GestioClients.buscarClient(cif);
								if(cli == null){
									System.out.println("Aquest client no existeix.");
								}else{
									//Modificar bd
									sql="UPDATE tiquet SET cif='"+cif+"' WHERE id='"+codi+"'";
									BD.modificarBD(sql);
								}
								break;
							case 2:
								modificarLinia(tiquet);
								break;
							case 0:
								break;
							default:
								System.out.println("Opció no vàlida");
								break;
						}
					System.out.println("Vols modificar alguna cosa més d'aquest tiquet? (si/no)");
					resp=teclat.nextLine();
				} while (Tpv.validarSiNo(resp));
			}
		}
	}
		
	//buscar tiquet amb el codi
	public static Tiquet buscarTiquet(){
		ResultSet rs;
		Tiquet t = null;
		String codi;
		double total;
		boolean facturat;
		String data;
		String client;
		String nFra;
		
		System.out.println("Id del Tiquet:");
		String id= teclat.nextLine();
		
		ArrayList <LiniaTiquet>  productes = buscarLinies(id);
		
		String sql="select * from tiquet where id='"+id+"'";
		rs=BD.executarConsulta(sql);
		try {
			rs.last();
			if(rs.getRow()==0){
				System.out.println("El tiquet no existeix.");
			}else{
				rs.beforeFirst();
				while(rs.next()){
					codi = rs.getString(1);
					total = rs.getDouble(2);
					facturat = rs.getBoolean(3);
					data = rs.getString(4);
					client = rs.getString(5);
					nFra = rs.getString(6);
					
					t=new Tiquet(codi,total,client,productes, data, facturat, nFra);
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return t;
	}
	
	//buscar linies d'un tiquet
	public static ArrayList <LiniaTiquet> buscarLinies(String tiquet){
		ResultSet rs;
		 ArrayList <LiniaTiquet> linies= new  ArrayList <LiniaTiquet>();
		 
		String sql="select * from linia_tiquet where id_tiquet='"+tiquet+"'";
		rs=BD.executarConsulta(sql);
		try {
			rs.last();
			if(rs.getRow()!=0){
				rs.beforeFirst();
				while(rs.next()){
					linies.add(new LiniaTiquet (rs.getString(1),rs.getInt(3),rs.getString(5)));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return linies;
	}
	
	//---------------------------------------------------------------------------------------------------

	//Tiquet suma per tipus d'iva. EL paràmetre iva se li passa el % d'iva
		public static double SumarIva(ArrayList <LiniaTiquet> linia,int tipusIva){
			double suma=0;
			double total=0;
			for (int i = 0; i < linia.size(); i++) {
				if(linia.get(i).getProd().getIva() == tipusIva){
					total=suma+(linia.get(i).getProd().getPreuSense()*(tipusIva/100.0));
				}
			}
			total=Math.rint(total*100)/100;
			return total;
		}
		
	
		//Nou tiquet
		public static void venta(){
			String resposta;
			Tiquet tiquet;
			ArrayList <Tiquet> tiquets = new ArrayList <Tiquet>();
			String sql="";
			String sql2="";
			do {
				tiquet=new Tiquet();
				if(!tiquet.getProductes().isEmpty()){//si no em introduit ninguna linia en l'array no farem un nou tiquet
					tiquets.add(tiquet);
					
					System.out.println("Vols guardar el tiquet en un fitxer?(si/no)");
					resposta=teclat.nextLine();
					if(Tpv.validarSiNo(resposta)){
						fitxerTiquet(tiquet);
					}
					System.out.println("Vols mostrar el tiquet?(si/no)");
					resposta=teclat.nextLine();
					if(Tpv.validarSiNo(resposta)){
						tiquet.mostrarTiquet();
					}
				}
				System.out.println("Vols fer una altra venta?(si/no)");
				resposta=teclat.nextLine();
				
				
			} while (Tpv.validarSiNo(resposta));
			
					
			if(!tiquets.isEmpty()){
				//guardar tiquets
				for (int i = 0; i <tiquets.size(); i++) {
					Tiquet t=tiquets.get(i);
					sql=sql+"('"+t.getId()+"',"+t.getTotal()+","+t.isFacturat()+",'"+t.getData()+"','"+t.getClient().getCif()+"',"+null+"),";
					
					for(int j=0;j<tiquets.get(i).getProductes().size();j++){
						LiniaTiquet linia=tiquets.get(i).getProductes().get(j);
						sql2=sql2+"('"+linia.getCodiLinia()+"','"+t.getId()+"',"+linia.getQuantitat()+","+linia.calcularTotal()+",'"
						+linia.getProd().getCodiBarres()+"'),";
						
					}
				}
		
				sql= "insert into tiquet values "+sql;
				sql= sql.substring(0, sql.length()-1);
				BD.modificarBD(sql);
				
				sql2= "insert into linia_tiquet values "+sql2;
				sql2= sql2.substring(0, sql2.length()-1);
				BD.modificarBD(sql2);
				
				
			}	
			
		}
		
	
	//menÃº gestiÃ³ de ventes
	public static void menuVentes() {
		int opcio;
		boolean valid;
		Tiquet tiquet = null;
		do{
			try{
				do {
					System.out.println("        MENÚ  \n"
						+ "---------------------------\n"
						+ "1. Nova venta/Nou tiquet \n"
						+ "2. Mostrar tiquet \n"
						+ "3. Modificar tiquet \n"
						+ "0. Anar al menú principal\n"
						+ "Elegeix una opció");
					opcio=Integer.parseInt(teclat.nextLine());
					
					switch (opcio) {
						case 1:
							venta();
							break;
						case 2:
							tiquet=buscarTiquet();
							if(tiquet != null){
								tiquet.mostrarTiquet();
							}
							
							break;
						case 3:
							modificarTiquet();
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
					e.printStackTrace();
					valid=false;
			}
		}while(valid==false);
	}

}
