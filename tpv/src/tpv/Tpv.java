package tpv;

import java.util.ArrayList;//
import java.util.Scanner;

public class Tpv {
	static Scanner teclat=new Scanner (System.in);
	static ArrayList <Producte> productes = new ArrayList <Producte> ();
	
	//buscar producte amb el codi de barres
	public static int buscarProd(){
		try {
			System.out.println("Introdueix el codi de barres del producte: ");
			String codi=teclat.nextLine();
		
			for (int i = 0; i < productes.size(); i++) {
				if (productes.get(i).getCodiBarres().equalsIgnoreCase(codi)) {
					return i;
				}
			}
		} catch (NumberFormatException e) {
			System.out.println("Codi invàlid. Has d'introduir números.");
		}
		return -1;
	}
	
	//resposta si/no
	public static boolean validarSiNo(String resposta){
		if(resposta.equalsIgnoreCase("si") || resposta.equalsIgnoreCase("s")){
			return true;
		}else if(resposta.equalsIgnoreCase("no") || resposta.equalsIgnoreCase("n")) {
			return false;
		}
		System.out.println("Entrada invàlida. Escriu si o no");
		return validarSiNo(teclat.nextLine());
	}
	//Afegir producte
	static public void afegirProd(){
		int num;
		do {
			System.out.println("Quants productes vols indtroduir?");
			num=Integer.parseInt(teclat.nextLine());
			if(num<=0){
				System.out.println("S'ha d'introduir una quantitat major a 0.");
			}
		} while (num<=0);
		
		for (int i = 0; i < num; i++) {
			productes.add(new Producte());
		}
	}
	
	//modificar dades producte
	public static void modificarProd(){
		int posicio=buscarProd();
		if(posicio==-1){
			System.out.println("No s'ha trobat el producte.");
		}else{
			int opcio;
		String resp;
			do {
				System.out.println("Que vols modificar?\n"
						+ "1. Codi de barres \n"
						+ "2. Descripció \n"
						+ "3. Preu base \n"
						+ "4. IVA\n");
				opcio=Integer.parseInt(teclat.nextLine());
				switch (opcio) {
				case 1:
					System.out.println("Nou codi de barres: ");
					String codi=teclat.nextLine();
					productes.get(posicio).setCodiBarres(codi);
					break;
				case 2:
					System.out.println("Nova descripció: ");
					String descripcio=teclat.nextLine();
					productes.get(posicio).setDescripcio(descripcio);
					break;
				case 3:
					System.out.println("Nou preu base: ");
					double preu=Double.parseDouble(teclat.nextLine());
					productes.get(posicio).setPreuSense(preu);
					break;
				case 4: 
					System.out.println("Nou IVA(%): ");
					double iva=Double.parseDouble(teclat.nextLine());
					productes.get(posicio).setIva(iva);
					break;
				default:
					System.out.println("Opció no vàlida");
					break;
				}
				System.out.println("Vols modificar alguna cosa més d'aquest producte? (si/no)");
				resp=teclat.nextLine();
			} while (validarSiNo(resp));
		}
	}
	
	//Eliminar producte
	public static void eliminarProd(){
		String resposta;
		do {
			int pos=buscarProd();
			productes.remove(pos);
			System.out.println("Vols eliminar algún producte més?");
			resposta=teclat.nextLine();
		} while (validarSiNo(resposta));
	}
	
	//Aumentar quantitat producte
	public static void augmentarStock(){
		String resposta;
		int quantitat;
		do {
			int pos=buscarProd();
			do{
				System.out.println("Introdueix quantitat:");
				quantitat=Integer.parseInt(teclat.nextLine());
				if(quantitat<=0){
					System.out.println("S'ha d'introduir una quantitat major a 0.");
				}
			}while(quantitat<=0);
			productes.get(pos).setQuantitat(quantitat);
			System.out.println("Vols modificar stock d'algún producte més?");
			resposta=teclat.nextLine();
		} while (validarSiNo(resposta));
	}
	//Menú gestió de stocks
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
							+ "5. \n"
							+ "6. \n"
							+ "0. Eixir\n"
							+ "Elegeix una opció");
						opcio=Integer.parseInt(teclat.nextLine());
						
						switch (opcio) {
							case 1:
								afegirProd();
								break;
							case 2:
								modificarProd();
								break;
							case 3:
								eliminarProd();
								break;
							case 4:
								augmentarStock();
								break;
							case 5:
								
								break;
							case 6:
								
								break;
							case 0:
								System.out.println("Has eixit del programa amb èxit.");
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
		
	//Menú del main
	public static void menuPrincipal() {
		int opcio;
		boolean valid;
		do{
			try{
				do {
					System.out.println("        MENÚ  \n"
						+ "---------------------------\n"
						+ "1. Gestió de stock \n"
						+ "2. Gestió de ventes\n"
						+ "3. Gestió de clients\n"
						+ "4. Gestió de facturació\n"
						+ "0. Eixir\n"
						+ "Elegeix una opció");
					opcio=Integer.parseInt(teclat.nextLine());
					
					switch (opcio) {
						case 1:
							menuStock();
							break;
						case 2:
							
							break;
						case 3:
							
							break;
						case 4:
							
							break;
						case 0:
							System.out.println("Has eixit del programa amb èxit.");
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
	
	public static void main(String[] args) {
		menuPrincipal();
	}

}
