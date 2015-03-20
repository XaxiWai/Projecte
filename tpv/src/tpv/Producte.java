package tpv;

import java.util.Scanner;

public class Producte {
	static Scanner teclat=new Scanner(System.in);
	private int codiBarres;
	private String descripcio;
	private double preuSense;
	private double iva;
	private double preuAmb;
	private int quantitat;
	
	public Producte(){
		
		for(int i=0;i>=0;i++){
			try{
				 System.out.println("Introdueix codi de barres:");
				 this.codiBarres=Integer.parseInt(teclat.nextLine());
				 break;
				}catch(NumberFormatException e){
					System.out.println("Codi inv�lid. Has d'introduir n�meros.");
				}
		}
		
		 System.out.println("Introdueix descripci�:");
		 this.descripcio=teclat.nextLine();
			 for(int i=0;i>=0;i++){
			 	if(descripcio.equalsIgnoreCase("")){
			 		System.out.println("Has d'introduir una descripci�.");
			 		System.out.println("Introdueix descripci�:");
			 		this.descripcio=teclat.nextLine();
			 	}
			 	else{
			 		break;
			 	}
			 }
		 
		for(int i=0;i>=0;i++){
			 try{
				 System.out.println("Introdueix preu base:");
				 this.preuSense=Double.parseDouble(teclat.nextLine());
				 break;
				}catch(NumberFormatException e){
					System.out.println("Preu inv�lid. Has d'introduir n�meros.");
				}
		}
		
		for(int i=0;i>=0;i++){
		 try{
			 System.out.println("Iva producte:");
			 this.iva=Double.parseDouble(teclat.nextLine());
			 break;
			}catch(NumberFormatException e){
				System.out.println("IVA inv�lid. Has d'introduir n�meros.");
			}
		}
		
		for(int i=0;i>=0;i++){
		 try{
			 System.out.println("Introdueix quantitat:");
			 this.quantitat=Integer.parseInt(teclat.nextLine());
			 break;
		 	}catch(NumberFormatException e){
				System.out.println("Quantitat inv�lida. Has d'introduir n�meros.");
			}
		}
		 this.preuAmb=this.preuSense*(1+this.iva/100);
	}
	
	//getters i setters
	public int getCodiBarres() {
		return codiBarres;
	}

	public void setCodiBarres(int codiBarres) {
		this.codiBarres = codiBarres;
	}

	public String getDescripcio() {
		return descripcio;
	}

	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}

	public double getPreuSense() {
		return preuSense;
	}

	public void setPreuSense(double preuSense) {
		this.preuSense = preuSense;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	public double getPreuAmb() {
		return preuAmb;
	}

	public void setPreuAmb(double preuAmb) {
		this.preuAmb = preuAmb;
	}

	public int getQuantitat() {
		return quantitat;
	}

	public void setQuantitat(int quantitat) {
		this.quantitat = quantitat;
	}
	
}
