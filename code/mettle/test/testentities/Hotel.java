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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public int getnVal() {
		return nVal;
	}

	public void setnVal(int nVal) {
		this.nVal = nVal;
	}

	public StreetAddress getAddr() {
		return addr;
	}

	public void setAddr(StreetAddress addr) {
		this.addr = addr;
	}
	
	
}