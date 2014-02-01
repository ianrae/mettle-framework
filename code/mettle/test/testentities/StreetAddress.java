package testentities;
import org.mef.framework.entities.Entity;


public class StreetAddress extends Entity
{
	public long id;
	public String street;
	public int number;

	public StreetAddress(long id, String street, Integer num)
	{
		this.id = id;
		this.street = street;
		this.number = num;
	}
}