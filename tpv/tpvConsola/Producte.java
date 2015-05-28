package tpvConsola;

import java.util.Scanner;


public class Producte {
	static Scanner teclat=new Scanner(System.in);
	private String codiBarres;
	private String descripcio;
	private double preuSense;
	private Double iva;
	private double preuAmb;
	private int stock;
	
	public Producte(){
		boolean noValid;
		System.out.println("Codi barres");
		String codiBarres=teclat.nextLine();
		if(GestioStock.buscarProd(codiBarres) != null){
			System.out.println("Aquest codi de barres ja pertany a un producte.");
		}else{
			this.codiBarres=codiBarres;
			System.out.println("Descripció/Nom");
			this.descripcio=teclat.nextLine();
			System.out.println("Preu sense iva");
			this.preuSense=Double.parseDouble(teclat.nextLine());
			do {
				System.out.println("Introdueix tipus IVA:");
				System.out.println("1. tipus general (21%)");
				System.out.println("2. Tipus reduït (10%)");
				System.out.println("3. Tipus superreduït (4%)");
				int iva=Integer.parseInt(teclat.nextLine());
				noValid=false;
				switch (iva) {
				case 1:
					this.iva=21.0;
					break;
				case 2:
					this.iva=10.0;
					break;
				case 3:
					this.iva=4.0;
					break;
		
				default:
					System.out.println("Opció no vàlida");
					noValid=true;
					break;
				}
			} while (noValid);
			
			System.out.println("Introdueix stock");
			this.stock=Integer.parseInt(teclat.nextLine());
			this.preuAmb=calculIva();
		}
		
	}
	
	
	public Producte(String codiBarres, String descripcio, double preuSense,
			double preuAmb, double iva,  int stock) {
	
		this.codiBarres = codiBarres;
		this.descripcio = descripcio;
		this.preuSense = preuSense;
		this.iva = iva;
		this.preuAmb = preuAmb;
		this.stock = stock;
	}


	//getters i setters
	public String getCodiBarres() {
		return codiBarres;
	}

	public void setCodiBarres(String codiBarres) {
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

	public Double getIva() {
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

	public int getstock() {
		return stock;
	}

	public void setstock(int stock) {
		this.stock = stock;
	}
	
	public double calculIva(){
		double preuIva=(preuSense*(1+(iva/100.0)));
		return preuIva;
	}


	@Override
	public String toString() {
		return "Producte [codiBarres=" + codiBarres + ", descripcio="
				+ descripcio + ", preuSense=" + preuSense + ", iva=" + iva
				+ ", preuAmb=" + preuAmb + ", stock=" + stock + "]";
	}
	
}

