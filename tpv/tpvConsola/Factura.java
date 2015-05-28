package tpvConsola;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;


public class Factura {
	private String nFra;
	private GregorianCalendar dataEmisio;
	private String data;
	private double totalFra;
	private ArrayList <Tiquet> tiquets = new ArrayList <Tiquet>();
	private static int num;
	private static int row=BD.comptarRegistresBD("select * from factura");
	static Scanner teclat=new Scanner(System.in);
	
	//constructor per defecte
	public Factura() {
		Client client;
		this.nFra="F"+(num+row);
		num++;
		String resposta=null;
		GregorianCalendar data=new GregorianCalendar();
		this.data=data.get(GregorianCalendar.YEAR)+"/"+(data.get(GregorianCalendar.MONTH)+1)+"/"+data.get(GregorianCalendar.DAY_OF_MONTH);
		
		
		System.out.println("Dni client:");
		String dni=teclat.nextLine();
		client=GestioClients.buscarClient(dni);
		buscarTiquet(client.getCif());
		this.totalFra=calcularTotal();
		boolean ticketFacturat=false;
		do{
			if(mostrarTiquets()){//si troba un tiquet fara facturar tiquet
				facturarTiquet();//Selecionem un tiquet a facturar, ho factura,guarda la factura en la BD i el tiquet facturat
				ticketFacturat=true;
			}
			System.out.println("Vols facturar altre tiquet?");
			resposta=teclat.nextLine();
		}while(Tpv.validarSiNo(resposta));
		
		if(ticketFacturat){
			System.out.println("Vols guardar la factura en un fitxer?");
			resposta=teclat.nextLine();
				if(Tpv.validarSiNo(resposta)){
					fitxerFactura(client);
				}
			
			System.out.println("Vols vore factura creada?");
			resposta=teclat.nextLine();
				if(Tpv.validarSiNo(resposta)){
					mostrarFactura(dni);
				}
		}
			
		tiquets.clear();
	}
	public Factura(String nFra,String data,double totalFactura){
		this.nFra=nFra;
		this.data=data;
		this.totalFra=totalFactura;
		String sql="SELECT * from tiquet WHERE n_factura='"+nFra+"'";
		ResultSet rs=BD.executarConsulta(sql);
		try {
			while(rs.next()){
				ArrayList <LiniaTiquet>  productes = buscarLinies(rs.getString("id"));
				tiquets.add(new Tiquet(rs.getString("id"), rs.getDouble("total"), rs.getString("cif"),productes,rs.getString("data"), rs.getBoolean("facturat"), rs.getString("n_factura")));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//buscar tiquet amb el codi
		public ArrayList<Tiquet> buscarTiquet(String dni){
			
			ResultSet rs;
			Tiquet t = null;
			String codi;
			double total;
			boolean facturat;
			String data;
			String client;
			
			Client cli=GestioClients.buscarClient(dni);
			
			if(cli==null){
				System.out.println("No s'ha trobat client.");
			}
			else{
			
				String sql="select * from tiquet where cif='"+dni+"'";
				rs=BD.executarConsulta(sql);
				try {
					rs.last();
					if(rs.getRow()==0){
						System.out.println("Client sense tiquets.");
					}else{
						rs.beforeFirst();
						while(rs.next()){
							
							codi = rs.getString(1);
							total = rs.getDouble(2);
							data = rs.getString(4);
							facturat=rs.getBoolean(3);
							client = rs.getString(5);
							ArrayList <LiniaTiquet>  productes = buscarLinies(codi);
							
							t=new Tiquet(codi,total,client,productes, data, facturat, null);
							tiquets.add(t);
						}
					}
				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return tiquets;
		}
		
		//buscar linies d'un tiquet
		public static ArrayList <LiniaTiquet> buscarLinies(String tiquet){
			ResultSet rs;
			 ArrayList <LiniaTiquet> linies= new  ArrayList <LiniaTiquet>();
			 
			String sql="select * from linia_tiquet where id_tiquet='"+tiquet+"'";
			rs=BD.executarConsulta(sql);
			try {
				rs.last();
				if(rs.getRow()==0){
					System.out.println("No hi ha linies en aquest tiquet.");
				}else{
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
		
		//mostrar tiquets
		public boolean mostrarTiquets(){
			boolean tiquetTrobat=false;
			System.out.println("Tiquets sense facturar: ");
			for (int i = 0; i < tiquets.size(); i++) {
				if(tiquets.get(i).isFacturat()==false){
					System.out.println(tiquets.get(i).toString());
					tiquetTrobat=true;
				}
			}
			if(tiquetTrobat==false){
				System.out.println("No hi han tiquets per a facturar.");
			}
			return tiquetTrobat;
		}
		
		//Facturar tiquet
		public boolean facturarTiquet(){
			boolean tiquetTrobat=false;
			System.out.println("Introduiex id del tiquet a facturar:");
			String id=teclat.nextLine();
			for (int i = 0; i < tiquets.size(); i++) {
				if(tiquets.get(i).getId().equalsIgnoreCase(id)){
					tiquetTrobat=true;
					tiquets.get(i).setFacturat(true);
					tiquets.get(i).setnFra(nFra);
					String sql="UPDATE tiquet SET facturat="+tiquets.get(i).isFacturat()+" WHERE id='"+tiquets.get(i).getId()+"'";
					BD.modificarBD(sql);
					
					if(!buscarFactura(nFra)){
						guardarFactura();
					}
					
					String sql2="UPDATE tiquet SET n_factura='"+nFra+"' WHERE id='"+tiquets.get(i).getId()+"'";
					BD.modificarBD(sql2);
				}
			}
			if(tiquetTrobat==false){
				System.out.println("No s'ha trobat tiquet.");
			}
				
			return tiquetTrobat;
		}
		//buscar factura
		public boolean buscarFactura(String nFra){
			String sql="SELECT n_factura FROM factura WHERE n_factura='"+nFra+"'";
			ResultSet rs=BD.executarConsulta(sql);
			try {
				rs.last();
				if(rs.getRow()==1){
					return true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return false;
		}
		//Guardar factura
		public void guardarFactura(){
			String sql= "insert into factura values ('"+nFra+"','"+data+"',"+totalFra+")";
			BD.modificarBD(sql);
		}
		//mostrar factura
		public void mostrarFactura(String dni){
			Double suma=0.0;
			Client client=GestioClients.buscarClient(dni);
			System.out.println("VENEDOR: ");
			System.out.println("CIF: B-1234567");
			System.out.println("VeneJuAL, S.L.\n");
			System.out.print("FACTURA: "+nFra+
					"\n Data: "+this.data+
					"\nCLIENT: "+
					"\nCIF/DNI: "+client.getCif()+
					"\nNom/Raó social: "+client.getRaoSocial()+
					"\nnº\tUnitats\t\tDescripció\tPreu/unitat"+"\n");
			for (int i = 0; i < tiquets.size(); i++) {
				String numF=tiquets.get(i).getnFra();
				if(numF!=null){
					System.out.print(tiquets.get(i).mostrarTiquetsFactura());
					suma=suma+tiquets.get(i).getTotal();
				}
				
			}
			System.out.println("TOTAL: "+suma);
		}
		//crear fitxer factura
		public void fitxerFactura(Client client){
			double suma=0.0;
			String fitxer="factura_"+nFra;
			
			String ruta=fitxer+".txt";
			
			try {
				FileWriter escriure= new FileWriter(ruta);
				BufferedWriter escriptor = new BufferedWriter(escriure);
			
				escriptor.write("VENDEDOR:");
				escriptor.newLine();
				escriptor.write("CIF: B-1234567");
				escriptor.newLine();
				escriptor.write("VeneJuAl,S.L.");
				escriptor.newLine();
				escriptor.newLine();
				escriptor.write("Factura: "+nFra);
				escriptor.newLine();
				escriptor.write("Data: "+data);
				escriptor.newLine();
				escriptor.newLine();
				escriptor.write("CLIENT:");
				escriptor.newLine();
				escriptor.write("DNI/CIF: "+client.getCif());
				escriptor.newLine();
				escriptor.write("Nom/Raó social: "+client.getRaoSocial());
				escriptor.newLine();
				escriptor.newLine();
				escriptor.write("PRODUCTES:");
				escriptor.write("nº\tUnitats\t\tDescripció\tPreu/unitat");
				escriptor.newLine();
				for (int i = 0; i < tiquets.size(); i++) {
					String numF=tiquets.get(i).getnFra();
					if(numF!=null){
						suma=suma+tiquets.get(i).getTotal();
						escriptor.write("\n"+tiquets.get(i).mostrarTiquetsFactura().toString());
						escriptor.newLine();
						escriptor.newLine();
					}
					
				}
				escriptor.write("--------------------------------------");
				escriptor.newLine();
				escriptor.write("TOTAL Factura: "+suma);
				escriptor.newLine();
				
				escriptor.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//---------------------------------------------------------------------------------------------------
		
		//Calcular total factura
		public double calcularTotal(){
			Double suma=0.0;
			for (int i = 0; i < tiquets.size(); i++) {
				suma=suma+tiquets.get(i).getTotal();
			}
			return suma;
		}
	//getters i setters
	public String getnFra() {
		return nFra;
	}

	public void setnFra(String nFra) {
		this.nFra = nFra;
	}

	public GregorianCalendar getDataEmisio() {
		return dataEmisio;
	}

	public void setDataEmisio(GregorianCalendar dataEmisio) {
		this.dataEmisio = dataEmisio;
	}

	public double getTotalFra() {
		return totalFra;
	}

	public void setTotalFra(double totalFra) {
		this.totalFra = totalFra;
	}

	public ArrayList<Tiquet> getTiquets() {
		return tiquets;
	}

	public void setTiquets(ArrayList<Tiquet> tiquets) {
		this.tiquets = tiquets;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	

}
