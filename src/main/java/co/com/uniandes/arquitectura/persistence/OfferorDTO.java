package co.com.uniandes.arquitectura.persistence;

public class OfferorDTO {

	private int id;
	private String companyName;
	private int state;
	
	
	
	public OfferorDTO(int id, String companyName, int state) {
		super();
		this.id = id;
		this.companyName = companyName;
		this.state = state;
	}
	public OfferorDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
	
}
