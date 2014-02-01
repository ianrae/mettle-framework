package testentities;
import org.mef.framework.entities.Entity;


public class Hotel extends Entity
{
	long id;
	public String flight;
	public String model;
	public Integer num;
	public int  nVal;
	public StreetAddress addr;

	public Hotel(long id, String flight, String model, Integer num)
	{
		this.id = id;
		this.flight = flight;
		this.model = model;
		this.num = num;
		this.nVal = num + 100;
	}
}