/*
 	Name: Naman Gangwani
 	NetID: nkg160030
 */

package Customer;

public class Customer {
	
	/** Variables to keep track of customer information **/
	private String firstName, lastName;
	private int guestID;
	private double amountSpent;
	
	/** Constructor creating new Customer object with given customer information **/
	public Customer(String firstName, String lastName, int guestID, double amountSpent)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.guestID = guestID;
		this.amountSpent = amountSpent;
	}
	
	// Accessor method for the customer's first name
	public String getFirstName()
	{
		return firstName;
	}
	
	// Mutator method to change the customer's first name
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	// Accessor method for the customer's last name
	public String getLastName()
	{
		return lastName;
	}
	
	// Mutator method to change the customer's last name
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}
	
	// Accessor method for the customer's guest ID
	public int getGuestID()
	{
		return guestID;
	}
	
	// Mutator method to change the customer's guest ID
	public void setGuestID(int guestID)
	{
		this.guestID = guestID;
	}
	
	// Accessor method for the customer's total amount of money spent
	public double getAmountSpent()
	{
		return amountSpent;
	}
	
	// Mutator method to change the customer's total amount of money spent
	public void setAmountSpent(double amountSpent)
	{
		this.amountSpent = amountSpent;
	}
	
	// Returns a String containing all of the customer's information
	@Override
	public String toString()
	{
		return String.format(guestID+" "+firstName+" "+lastName+" %.2f", amountSpent);
	}
}
