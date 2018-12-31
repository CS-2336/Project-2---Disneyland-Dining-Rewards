/*
 	Name: Naman Gangwani
 	NetID: nkg160030
 */

import Customer.*;

import java.io.*;
import java.util.Scanner;

public class Main {
	
	// Arrays to keep track of the customers and preferred customers, respectively
	public static Customer[] customers;
	public static PreferredCustomer[] preferredCustomers;
	
	public static void main(String[] args) throws IOException
	{
		// Keeps track of the number of customers and preferred customers
		int numCustomers = 0, numPreferredCustomers = 0;
		
		/** Reads in customer.dat file and adds all the customers line by line to the customers array **/
		Scanner input = new Scanner(new File("customer.dat"));
		while (input.hasNextLine())
		{
			numCustomers+=1; // Adds to the count of the total number of customers
			int customerID = input.nextInt();
			String firstName = input.next(), lastName = input.next();
			double amountSpent = input.nextDouble();
			// Adds the customer just read in to the customers array
			customers = addCustomer(new Customer(firstName, lastName, customerID, amountSpent), numCustomers);
		}
		
		/** Reads in preferred.dat file and adds all the preferred customers line by line to the preferred customers array **/
		if (new File("preferred.dat").isFile()) // If preferred.dat exists
		{
			input = new Scanner(new File("preferred.dat"));
			while (input.hasNextLine())
			{
				numPreferredCustomers+=1; // Adds to the count of the total number of preferred customers
				int customerID = input.nextInt();
				String firstName = input.next(), lastName = input.next();
				double amountSpent = input.nextDouble();
				String discount = input.next();
				int discountPercentage = Integer.parseInt(discount.substring(0, discount.length() - 1)); // Gets rid of percentage sign
				// Adds the preferred customer just read in to the customers array
				preferredCustomers = addPreferredCustomer(new PreferredCustomer(firstName, lastName, customerID, amountSpent, discountPercentage), numPreferredCustomers);
			}
		}
		
		/** Reads in orders.dat file and adds all the preferred customers line by line to the preferred customers array **/
		input = new Scanner(new File("orders.dat"));
		while (input.hasNextLine())
		{
			int customerID = input.nextInt();
			double containerRadius = input.nextDouble(), containerHeight = input.nextDouble();
			int ounces = input.nextInt();
			double ouncePrice = input.nextDouble(), squareInchPrice = input.nextDouble();
			int quantity = input.nextInt();
			
			boolean preferredStatus = isPreferredCustomer(customerID); // Checks whether the customer being read is preferred or not
			Customer currentCustomer;
			if (preferredStatus) // If the customer is a preferred customer
				currentCustomer = getPreferredCustomerFromID(customerID); // Get the preferred customer
			else
				currentCustomer = getCustomerFromID(customerID); // Get the regular customer
			
			// Calculates total price based on given radius, height, ounces, ounce price, square inch price, and quantity
			double totalPrice = calculateTotalPrice(currentCustomer, preferredStatus, quantity, ounces, ouncePrice, squareInchPrice, containerRadius, containerHeight);
			
			// Updates the amount spent by the customer
			currentCustomer.setAmountSpent(totalPrice + currentCustomer.getAmountSpent());
			
			// Calculates the new discount percentage (if any)
			int discount = 0;
			double amountSpent = currentCustomer.getAmountSpent();
			if (amountSpent >= 150 && amountSpent < 200)
				discount = 5;
			else if (amountSpent >= 200 && amountSpent < 350)
				discount = 7;
			else if (amountSpent >= 350)
				discount = 10;
			
			if (preferredStatus && discount != 0) // If the customer is already preferred 
				((PreferredCustomer) currentCustomer).setDiscountPercentage(discount); // Update the customer's discount percentage value
			else if (!preferredStatus && amountSpent >= 150) // If the customer isn't preferred and is now preferred after the transaction
			{
				customers = removeCustomer(currentCustomer.getGuestID()); // Remove the customer from the regular customers array
				// Update number of customers and preferred customers
				numCustomers-=1;
				numPreferredCustomers+=1;
				// Creates a new preferred customer based on the current customer's information and now the new discount percentage value
				PreferredCustomer newPreferredCustomer = new PreferredCustomer(currentCustomer.getFirstName(), 
						currentCustomer.getLastName(), currentCustomer.getGuestID(), currentCustomer.getAmountSpent(), discount);
				preferredCustomers = addPreferredCustomer(newPreferredCustomer, numPreferredCustomers); // Adds the new preferred customer to the preferred customer array
			}
		}
		
		/** Loops through all the customers in the customers array and then updates the customer.dat file with that information **/
		PrintWriter customerFile = new PrintWriter("customer.dat");
		customerFile.print("");
		if (numCustomers > 0)
		{
			for (int i = 0; i < customers.length; i++)
			{
				customerFile.print(customers[i].toString()); // Prints the customer's details
				if (i != customers.length - 1)
					customerFile.println(); // Creates a new line if it's not the last customer
			}
		}
		customerFile.close();
		
		/** Loops through all the preferred customers in the preferred customers array and then updates the preferred.dat file with that information **/
		if (numPreferredCustomers > 0)
		{
			PrintWriter preferredCustomerFile = new PrintWriter("preferred.dat");
			preferredCustomerFile.print("");
			for (int i = 0; i < preferredCustomers.length; i++)
			{
				preferredCustomerFile.print(preferredCustomers[i].toString()); // Prints the preferred customer's details
				if (i != preferredCustomers.length - 1)
					preferredCustomerFile.println(); // Creates a new line if it's not the last preferred customer
			}
			preferredCustomerFile.close();
		}
		
		input.close();
	}
	
	/** Returns true if the customer from the given customer ID is a preferred customer **/
	public static boolean isPreferredCustomer(int customerID)
	{
		// Uses a try-catch in case preferredCustomer had never been initialized
		try {
			for (PreferredCustomer preferredCustomer: preferredCustomers)
				if (preferredCustomer.getGuestID() == customerID) // If the customer's ID matches up with a preferred customer's one
					return true; // Then it is a preferred customer
		} catch (NullPointerException e) {
			return false; // If it results an error then there are no preferred customers anyway
		}
		return false;
	}
	
	/** Returns the preferred customer found from the given customer ID **/
	public static PreferredCustomer getPreferredCustomerFromID(int customerID)
	{
		for (PreferredCustomer preferredCustomer: preferredCustomers)
			if (preferredCustomer.getGuestID() == customerID)
				return preferredCustomer; // Returns matching preferred customer with given customer ID
		return new PreferredCustomer("", "", 0, 0, 0);
	}
	
	/** Returns the customer found from the given customer ID **/
	public static Customer getCustomerFromID(int customerID)
	{
		for (Customer customer: customers)
			if (customer.getGuestID() == customerID)
				return customer; // Returns matching customer with given customer ID
		return new Customer("", "", 0, 0);
	}
	
	/** Returns a copy of the customer array that doesn't contain the customer with the given customer ID **/
	public static Customer[] removeCustomer(int customerID)
	{
		// New array that is one less than the current size
		Customer[] customers2 = new Customer[customers.length - 1];
		
		int pos = 0;
		for (int i = 0; i < customers.length; i++)
			if (customers[i].getGuestID() != customerID) // Adds all customers except for the given customer ID
			{
				customers2[pos] = customers[i];
				pos+=1;
			}
		
		return customers2;
	}
	
	/** Returns a copy of the customer array that has an additional customer with the given customer object **/
	public static Customer[] addCustomer(Customer customer, int newSize)
	{
		Customer[] customers2 = new Customer[newSize]; // Creates a new customer array with a bigger size
		
		// Adds all the customers from the customer array to the new customer array
		for (int i = 0; i < newSize - 1; i++)
				customers2[i] = customers[i];
		
		customers2[newSize - 1] = customer; // Adds the new customer to the new customer array
		
		return customers2;
	}
	
	/** Returns a copy of the preferred customer array that has an additional preferred customer with the given preferred customer object **/
	public static PreferredCustomer[] addPreferredCustomer(PreferredCustomer preferredCustomer, int newSize)
	{
		PreferredCustomer[] preferredCustomers2 = new PreferredCustomer[newSize]; // Creates a new preferred customer array with a bigger size
		
		// Adds all the preferred customers from the preferred customer array to the new preferred customer array
		for (int i = 0; i < newSize - 1; i++)
			preferredCustomers2[i] = preferredCustomers[i];
		
		preferredCustomers2[newSize - 1] = preferredCustomer; // Adds the new preferred customer to the new preferred customer array
		
		return preferredCustomers2;
	}
	
	/** Calculates and returns the total price with the given dimensions of the cup and the volume of the drink **/
	public static double calculateTotalPrice(Customer currentCustomer, boolean preferredStatus, int quantity, int ounces, double ouncePrice, double squareInchPrice, double containerRadius, double containerHeight)
	{
		// Formula: quantity * (ounces * ounces price + squareIncePrice * surface area around + squareInchPrice * surface area of the circles above and below the drink)
		double totalPrice = quantity * ((ounces * ouncePrice) + (squareInchPrice * 2 * Math.PI * (containerRadius * containerHeight + Math.pow(containerRadius, 2))));
		if (preferredStatus) // If the customer is preferred
			totalPrice = totalPrice - (totalPrice * ((PreferredCustomer) currentCustomer).getDiscountPercentage()/100); // Applies discount to the total price
		return totalPrice;
	}
}
