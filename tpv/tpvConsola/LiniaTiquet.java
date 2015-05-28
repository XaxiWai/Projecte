package tpvConsola;

import java.util.Scanner;

public class LiniaTiquet {
	private String codiLinia;
	private int quantitat;
	private Producte prod;
		
	private static Scanner teclat=new Scanner (System.in);
	private static int num=1;
	
	//constructor
	public LiniaTiquet(String tiquet) {
		int quantitat=0;
		int stock=0;
		Producte p=GestioStock.buscarProd();
		if(p == null){
			System.out.println("No existeix el producte.");
		}else{
			System.out.println("Quantitat de producte a comprar:");
			quantitat=Integer.parseInt(teclat.nextLine());
			quantitat=controlarStock(quantitat,p);
			
			if(quantitat == 0){
				System.out.println("La quantitat ha de ser superior a 0.");
			}else{
				//ompli variables d'instància
				this.codiLinia="L"+num;
				num++;
				this.quantitat=quantitat;
				stock=p.getstock()-this.quantitat;
				p.setstock(stock); 
				this.prod=p;
				//modificar stock en la base da dades
				String sql= " update producte set stock="+stock+" where codi='"+prod.getCodiBarres()+"'";
				BD.modificarBD(sql);
			}
		}	
		
	}
		
	public LiniaTiquet(String codiLinia, int quantitat, String prod) {
		this.codiLinia = codiLinia;
		this.prod = GestioStock.buscarProd(prod);
		this.quantitat = controlarStock(quantitat, this.prod);
	}



	//controlar stock
		public int controlarStock(int quantitat, Producte p){
			do {
				if(p.getstock()<quantitat){
					if(p.getstock()==0){
						System.out.println("Producte esgotat");
						System.out.println("Quantitat de producte a comprar:");
						quantitat=Integer.parseInt(teclat.nextLine());
					}else if(p.getstock()<quantitat){
						System.out.println("Quantitat insuficient en stock.");
						System.out.println("Hi ha "+p.getstock()+" en stock.");	
						System.out.println("Quantitat de producte a comprar:");
						quantitat=Integer.parseInt(teclat.nextLine());
					}
				}else if(quantitat <0){
					System.out.println("La quantitat no pot ser negativa");
					System.out.println("Quantitat de producte a comprar:");
					quantitat=Integer.parseInt(teclat.nextLine());
				}				
			} while (quantitat>p.getstock() || quantitat<0);
			
			return quantitat;
		}
	
	
		//preu total linia tiquet
		public double calcularTotal(){
			return quantitat*prod.getPreuSense();
		}

	//getters i setters
	public String getCodiLinia() {
		return codiLinia;
	}

	public void setCodiLinia(String nLinia) {
		this.codiLinia = nLinia;
	}

	public int getQuantitat() {
		return quantitat;
	}

	public void setQuantitat(int quantitat) {
		this.quantitat = quantitat;
	}

	public Producte getProd() {
		return prod;
	}

	public void setProd(Producte prod) {
		this.prod = prod;
	}

	public static int getNum() {
		return num;
	}

	public static void setNum(int num) {
		LiniaTiquet.num = num;
	}

	@Override
	public String toString() {
		return codiLinia + "\t"+quantitat +"\t\t"+prod.getDescripcio()+"\t\t\t"+prod.getPreuSense() ;
	}
	

	
}
