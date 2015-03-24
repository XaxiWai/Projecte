package tpv;

import java.util.GregorianCalendar;

public class Client {
	private String cif;
	private String raoSocial;
	private String domicili;
	private GregorianCalendar dataAlta;

	// constructor per defecte
	public Client() {

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

	public GregorianCalendar getDataAlta() {
		return dataAlta;
	}

	public void setDataAlta(GregorianCalendar dataAlta) {
		this.dataAlta = dataAlta;
	}

	
}
