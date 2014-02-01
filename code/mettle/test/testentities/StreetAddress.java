package testentities;
import org.mef.framework.entities.Entity;


public class StreetAddress extends Entity
{
	public String street;
	public int number;

	public StreetAddress(String street, Integer num)
	{
		this.street = street;
		this.number = num;
	}
}