package testentities;
import org.mef.framework.entities.Entity;


public class Hotel extends Entity
{
	private long id;
	private String flight;
	private String model;
	private Integer num;
	private int  nVal;
	private StreetAddress addr;
	private boolean flag;

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

	public int getNVal() {
		return nVal;
	}

	public void setNVal(int nVal) {
		this.nVal = nVal;
	}

	public StreetAddress getAddr() {
		return addr;
	}

	public void setAddr(StreetAddress addr) {
		this.addr = addr;
	}
	
	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean val) {
		this.flag = val;
	}
	
}