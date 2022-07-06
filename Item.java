package hw1;

/**
 * Class Item.java which creates Item objects */
public class Item {
	/** Member data for item attributes*/
	private int itemCode;
	private String itemName;
	private double unitPrice;
	
	/** Constructor for Item object
	 * @param code, name, and price*/
	public Item(int code, String name, double price) {
		itemCode = code;
		itemName = name;
		unitPrice = price;
	}
	
	/** Method to get item code
	 * @return itemCode*/
	public int getCode() {
		return itemCode;
	}
	
	/** Method to get item name
	 * @return itemName*/
	public String getName() {
		return itemName;
	}
	
	/** Method to get item code
	 * @return itemPrice*/  
	public double getPrice() {
		return unitPrice;
	}

}
