package tpv;

import java.util.ArrayList;

public class Tiquet {
	private String id;
	private double total;
	private ArrayList <LiniaTiquet> productes = new ArrayList <LiniaTiquet>();

	//constructor per defecte
	public Tiquet() {
		
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

	
}
