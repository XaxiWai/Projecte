package tpvConsola;

import java.util.Scanner;

public class Tpv {
	static Scanner teclat=new Scanner (System.in);
	
	
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
				
	//Menú del main
	public static void menuPrincipal() {
		int opcio;
		boolean valid;
		do{
			try{
				do {
					System.out.println("        MENú  \n"
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
							GestioStock.menuStock();
							break;
						case 2:
							GestioVentes.menuVentes();
							break;
						case 3:
							GestioClients.menuGestioClients();
							break;
						case 4:
							GestioFacturacio.menuGestioFacturacio();
							break;
						case 0:
							System.out.println("Has eixit del programa amb exit.");
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
	
	public static void main(String[] args) {
		menuPrincipal();
	}

}
