package app.se329.project2.model;

import java.io.Serializable;

public class InventoryItem implements Serializable {

	private String name;
	private String descr;
	private int quantity;
	private double unitWeight;
	private String weightUnits;
	private String totalWeight;

	public InventoryItem(String itemName, String description, int quantity, double weight, String weightU){
		setName(itemName);
		setDescripton(description);
		setQuantity(quantity);
		setUnitWeight(weight);
		weightUnits = weightU;
		setTotalWeight();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescripton(String descr) {
		this.descr = descr;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quan) {
		quantity = quan;
	}

	public double getUnitWeight() {
		return unitWeight;
	}

	public void setUnitWeight(double weight) {
		this.unitWeight = weight;
	}
	
	public String getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight() {
		this.totalWeight = ""+quantity*unitWeight+weightUnits;
	}
	
}
