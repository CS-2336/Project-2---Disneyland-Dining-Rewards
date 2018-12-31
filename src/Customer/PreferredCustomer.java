/*
 	Name: Naman Gangwani
 	NetID: nkg160030
 */

package Customer;

public class PreferredCustomer extends Customer {
	private int discountPercentage; // Keeps track of the preferred customer's discount percentage
	
	/** Constructor creating new Preferred Customer object with given customer information, including discount percentage **/
	public PreferredCustomer(String firstName, String lastName, int guestID, double amountSpent, int discountPercentage)
	{
		super(firstName, lastName, guestID, amountSpent); // Sends everything but the discount percentage to the base class
		this.discountPercentage = discountPercentage;
	}
	
	// Accessor method for the preferred customer's discount percentage
	public int getDiscountPercentage()
	{
		return discountPercentage;
	}
	
	// Mutator method to change the preferred customer's discount percentage
	public void setDiscountPercentage(int discountPercentage)
	{
		this.discountPercentage = discountPercentage;
	}
	
	// Returns a String containing all of the preferred customer's information, including the discount percentage
	@Override
	public String toString()
	{
		return super.toString()+" "+discountPercentage+"%";
	}
}
