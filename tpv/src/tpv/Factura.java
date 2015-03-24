package tpv;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Factura {
	private String nFra;
	private GregorianCalendar dataEmisio;
	private double totalFra;
	private Client client;
	private ArrayList <Tiquet> tiquets = new ArrayList <Tiquet>();

	
	//constructor per defecte
	public Factura() {
		
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	

}
