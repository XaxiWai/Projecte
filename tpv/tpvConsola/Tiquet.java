package tpvConsola;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;


public class Tiquet {
	private String id;
	private double total;
	private Client client; //guardar el client 
	private ArrayList <LiniaTiquet> productes = new ArrayList <LiniaTiquet>();
	private String data;
	private boolean facturat;
	private String nFra;
	
	private static Scanner teclat=new Scanner(System.in);
	private static int num;
	private static int row=BD.comptarRegistresBD("select * from tiquet");

	//constructor per defecte
		public Tiquet() {
			String resposta;
			LiniaTiquet linia;
			
			System.out.println("Codi del client: ");
			String codi=teclat.nextLine();
			
			Client client=GestioClients.buscarClient(codi); //canviar a tipus Client
			if(client == null){
				System.out.println("El client no existeix.");
			}else{
				do {
					linia=new LiniaTiquet(this.id);
					if(linia.getCodiLinia()!=null){//si em introduit el producte en la'array fara la linia, si no ix
						productes.add(linia);
						System.out.println("\nProducte afegit correctament\n");
					}
					System.out.println("Vols insertar un altre producte?(si/no)");
					resposta=teclat.nextLine();
				} while (Tpv.validarSiNo(resposta));
				
				LiniaTiquet.setNum(1); //reiniciem el valor per a que en cada execució vaja de 1 en 1 els codis.

				
				this.client=client;
				this.id="T-"+(num+row);
				num++;
				//total del tiquet amb iva
				this.total=calcularTotal();
				this.facturat=false;
				//data actual del sistema
				GregorianCalendar d=new GregorianCalendar();
				this.data=d.get(GregorianCalendar.YEAR)+"/"+(d.get(GregorianCalendar.MONTH)+1)+"/"+d.get(GregorianCalendar.DAY_OF_MONTH);
				
			}	
		}
		
	
	
	public Tiquet(String id, double total, String client,
			ArrayList<LiniaTiquet> productes, String data, boolean facturat,
			String nFra) {
		this.id = id;
		this.total = total;
		this.client = GestioClients.buscarClient(client);
		this.productes = productes;
		this.data = data;
		this.facturat = facturat;
		this.nFra = nFra;
	}


	//mostrar tiquet
		public void mostrarTiquet() {
			System.out.println("Id:"+id+"\n"
					+"Data: "+ data+"\n"
					+"CLIENT:\nDNI/CIF:"+client.getCif()+"\nNom/Raó Social: "+client.getRaoSocial()+"\n");
			if(facturat){
				System.out.println("Número de factura: "+nFra);
			}
			
			System.out.println("PRODUCTES:");
			System.out.println("nº\tUnitats\t\tDescripció\tPreu/unitat");
			for (int i = 0; i < productes.size(); i++) {
				System.out.println(productes.get(i).toString());
			}
			System.out.println("-------------------------------");
			System.out.println("TOTAL:");
			mostrarTotal();
			System.out.println("-------------------------------");
		}


		//suma total tiquet
		public double calcularTotal(){
			double suma=0;
			for (int i = 0; i < productes.size(); i++) {
				LiniaTiquet p=productes.get(i);
				suma=suma+p.calcularTotal();
				
			}
			double iva21=GestioVentes.SumarIva(productes, 21);
			double iva10=GestioVentes.SumarIva(productes, 10);
			double iva4= GestioVentes.SumarIva(productes, 4);
			return suma+iva21+iva10+iva4;
		}
		
		//mostra els totals de base imponible, iva i total del tiquet
		public void mostrarTotal(){
			double suma=0;
			for (int i = 0; i < productes.size(); i++) {
				LiniaTiquet p=productes.get(i);
				suma=suma+p.calcularTotal();
				
			}
			double iva21=GestioVentes.SumarIva(productes, 21);
			double iva10=GestioVentes.SumarIva(productes, 10);
			double iva4= GestioVentes.SumarIva(productes, 4);
			double total=suma+iva21+iva10+iva4;
			System.out.println("Base Imposable: \t"+suma);
			System.out.println("Iva general (21%): \t"+iva21);
			System.out.println("Iva reduït (10%): \t"+iva10);
			System.out.println("Iva superreduït (4%): \t"+ iva4);
			System.out.println("TOTAL A PAGAR: \t"+total+"€");

		}
	
	//getters i setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public ArrayList<LiniaTiquet> getProductes() {
		return productes;
	}

	public void setProductes(ArrayList<LiniaTiquet> productes) {
		this.productes = productes;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public boolean isFacturat() {
		return facturat;
	}

	public void setFacturat(boolean facturat) {
		this.facturat = facturat;
	}

	public static int getNum() {
		return num;
	}

	public static void setNum(int num) {
		Tiquet.num = num;
	}

	public String getnFra() {
		return nFra;
	}

	public void setnFra(String nFra) {
		this.nFra = nFra;
	}


	@Override
	public String toString() {
		return "TIQUET: "+id+"\t\tTOTAL:"+total;
	}
	public String mostrarTiquetsFactura(){
		String prod="";
		for (int i = 0; i < productes.size(); i++) {
			prod=prod+productes.get(i).toString()+"\n";
		}
		return prod; 
	}

	

	
}
