package testentities;
import org.mef.framework.entities.Entity;


public class StreetAddress extends Entity
{
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

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