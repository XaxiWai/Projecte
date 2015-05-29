package tpvConsola;

import java.util.GregorianCalendar;
import java.util.Scanner;

public class Client {
	private String cif;
	private String raoSocial;
	private String domicili;
	private String dataAlta;
	private boolean baixa=false;
	

	static Scanner teclat=new Scanner(System.in);
	// constructor per defecte
	public Client() {
		System.out.println("Introdueix cif:");
		this.cif=teclat.nextLine();
	
		System.out.println("Introdueix raó social/nom:");
		this.raoSocial=teclat.nextLine();
		
		System.out.println("Introdueix domicili:");
		this.domicili=teclat.nextLine();
		
		GregorianCalendar data=new GregorianCalendar();
		this.dataAlta=data.get(GregorianCalendar.YEAR)+"/"+(data.get(GregorianCalendar.MONTH)+1)+"/"+data.get(GregorianCalendar.DAY_OF_MONTH);
	}

	
	

	public Client(String cif, String raoSocial, String domicili,
			String dataAlta, boolean baixa) {
		this.cif = cif;
		this.raoSocial = raoSocial;
		this.domicili = domicili;
		this.dataAlta = dataAlta;
		this.baixa = baixa;
	}




	//getters i setters
	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getRaoSocial() {
		return raoSocial;
	}

	public void setRaoSocial(String raoSocial) {
		this.raoSocial = raoSocial;
	}

	public String getDomicili() {
		return domicili;
	}

	public void setDomicili(String domicili) {
		this.domicili = domicili;
	}

	public boolean isBaixa() {
		return baixa;
	}

	public void setBaixa(boolean baixa) {
		this.baixa = baixa;
	}

	public void setDataAlta(String dataAlta) {
		this.dataAlta = dataAlta;
	}

	public String getDataAlta() {
		return dataAlta;
	}	
	
	
}
