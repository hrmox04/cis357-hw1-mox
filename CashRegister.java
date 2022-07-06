package hw1;
//Homework 1: Sales Register Program
//Course: CIS357
//Due date: 07/05/22
//Name: Hannah Mox
//GitHub: xxx FIXXX
//Instructor: Il-Hyung Cho
//Program description: This program prompts users to enter data about products.
//The end product is a receipt that gives price amounts, change, and total sale amounts  

import java.util.InputMismatchException;

//To-do

/* Fix the format for prices (ex. $3.00)
 * DONE Finish the try catch message 
 * Price should be right aligned 
 * Fix comments 
 * Make sure formats are correct 
 * DONE Items should print in alph. order
 * Run in git
 * */

//Import scanner and file
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Class CashRegister.java which runs code to create a receipt after user is
 * prompted for item information
 */
public class CashRegister {
	// Global variables
	// Array of items
	public static Item[] itemArray = new Item[10];

	// Array of all purchased items
	public static int[] purchasedItems = new int[10];

	/**
	 * Main function that prompts user and prints information
	 * 
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// Import Scanner
		Scanner input = new Scanner(System.in);

		// Header
		System.out.println("Welcome to MyLastName cash register system!");

		// Variables for prices
		// make an array for all of the prices
		double[] overallPrice = new double[50];
		double[] overallDay = new double[50];
		double salePrice = 0;
		double overallSale = 0;
		int code = 0;
		int quantity = 0;
		String number = "";
		char userAnswer;
		double price = 0;

		// Counters for price arrays
		int counter1 = 0;
		int counter2 = 0;

		// Read in file with items
		readIn();

		// While loop for new sale end when input is not y
		do {
			// Prompt user
			System.out.print("\nBeginning a new sale (Y/N) ");
			userAnswer = input.next().charAt(0);

			// If input is is not y or Y
			if (userAnswer == 'n' || userAnswer == 'N') {
				break;
			}

			// Visible break
			System.out.println("--------------------");

			do {
				// Prompt user for item code
				System.out.print("Enter product code: ");
				String codeString = input.next();

				// Try catch for code entry
				try {
					code = Integer.parseInt(codeString);

				} catch (NumberFormatException e) {
					System.out.println("!!! Invalid product code\n");
					continue;
				}

				// If code is -1 break loop
				if (code == -1) {
					break;
				}

				// Try catch to make sure that there are no inputs that are out of bounds
				try {
					// Display item name
					System.out.print("         item name: " + itemArray[code - 1].getName() + "\n");

				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("!!! Invalid product code\n");
					continue;
				}

				// Loop and try catch for the quantity
				do {
					try {
						// Prompt user for quantity
						System.out.print("Enter quantity:     ");
						number = input.next();
						quantity = Integer.parseInt(number);

					} catch (Exception e) {
						System.out.println("!!! Invalid quantity\n");
					}
				} while (!(Character.isDigit(number.charAt(0))));

				// Display item total after you calculate it
				price = calculateItemTotal(itemArray[code - 1].getPrice(), quantity);
				System.out.printf("        item total: $%7.2f\n\n", price);

				// Add quantity and price to their arrays
				purchasedItems[code - 1] += quantity;
				overallPrice[counter1] = price;

				// Increase counter
				counter1++;
				
			} while (code != -1);

			// Reset salePrice to zero so that the subtotal adds up correctly
			salePrice = 0;

			// Visible break
			System.out.println("--------------------");
			System.out.println("Items list: ");

			// Create a new array that will act as the sorted array
			// define an array copyArray to copy contents of intArray
			Item copyArray[] = new Item[itemArray.length];

			// Copy contents of intArray to copyArray
			for (int i = 0; i < itemArray.length; i++)
				copyArray[i] = itemArray[i];

			// Sort both arrays to be in ascending order
			for (int i = 0; i < copyArray.length; i++) {
				for (int j = i + 1; j < copyArray.length; j++) {
					if (copyArray[i].getName().compareTo(copyArray[j].getName()) > 0) {
						Item temp = copyArray[i];
						int tempNum = purchasedItems[i];

						copyArray[i] = copyArray[j];
						purchasedItems[i] = purchasedItems[j];

						copyArray[j] = temp;
						purchasedItems[j] = tempNum;
					}
				}
			}

			// Display the sorted items
			for (int k = 0; k < purchasedItems.length; k++) {
				if (purchasedItems[k] != 0) {
					String itemListing = purchasedItems[k] + " " + copyArray[k].getName();
					double itemsCost = copyArray[k].getPrice() * purchasedItems[k];
					System.out.printf("   %-16s $   %2.2f \n", itemListing, itemsCost);
				}
			}
			
			// Clear purchased items 
			for (int k = 0; k < purchasedItems.length; k++) {
				purchasedItems[k] = 0;
				//itemArray[k] = new Item(0,"",0);
			}

			// Add up subtotal and print
			for (int j = 0; j < overallPrice.length; j++) {
				salePrice += overallPrice[j];
			}
			System.out.printf("%-19s $   %2.2f \n", "Subtotal", salePrice);

			// Add salePrice to overallDay and increase counter2
			overallDay[counter2] = salePrice;
			counter2++;
			
			// Reset the overallPrice array to that the values are all zero 
			// This prevents sales from previous days from being added too
			for (int j = 0; j < overallPrice.length; j++) {
				overallPrice[j] = 0;
			}

			// Get tax amount
			System.out.printf("%-15s $   %2.2f\n", "Total with Tax (6%)", (totalWithTax(salePrice)));

			// Tendered amount
			System.out.printf("Tendered amount     $   ");
			double userPayment = input.nextInt();

			// Display change
			System.out.printf("%-19s $   %2.2f\n", "Change", (userPayment - totalWithTax(salePrice)));

			// Visible break
			System.out.println("--------------------");
			counter1 = 0;

		} while (userAnswer == 'y' || userAnswer == 'Y');

		// Display end output (total sale and message)
		for (int i = 0; i < overallDay.length; i++) {
			overallSale += overallDay[i];
		}
		
		System.out.printf("The total sale for the day is $%2.2f \n", overallSale);

		System.out.println("Thanks for using POST system. Goodbye.");
	}

	/**
	 * Method that will read in inventory from a file
	 * 
	 * @throws FileNotFoundException
	 */
	public static void readIn() throws FileNotFoundException {
		File inventory = new File("/Users/hannahmox/Desktop/OneDrive - Saginaw Valley State University/CIS357/hw1/inventory.txt");
		Scanner items = new Scanner(inventory);

		//Counter var i 
		int i = 0;

		// Read items in
		while (items.hasNext()) {
			String itemLine = items.nextLine();
			String[] item = itemLine.split(",");

			// Get individual items
			int itemCode = Integer.parseInt(item[0]);
			String itemName = item[1];
			double itemPrice = Double.parseDouble(item[2]);

			// Make item
			Item newItem = new Item(itemCode, itemName, itemPrice);

			// Add item to array
			itemArray[i] = newItem;
			i++;
		}

		//Close file 
		items.close();
	}

	/**
	 * Method to get items total price
	 * 
	 * @param price and quantity
	 * @return price * quantity
	 */
	public static double calculateItemTotal(double price, int quantity) {
		return price * quantity;
	}

	/**
	 * Method that searches through items and returns the amount of the item
	 * 
	 * @param key and itemArray
	 * @return itemCounter
	 */
	public static int searchItems(String key, String[] itemArray) {
		int itemCounter = 0;
		for (int i = 0; i < itemArray.length; i++) {
			if (itemArray[i] == key) {
				itemCounter++;
			}
		}
		return itemCounter;
	}

	/**
	 * Method to return total price including tax
	 * 
	 * @param price
	 * @return price * 1.06
	 */
	public static double totalWithTax(double price) {
		return price * 1.06;
	}

}
