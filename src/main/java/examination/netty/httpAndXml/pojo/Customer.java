package examination.netty.httpAndXml.pojo;

import java.util.List;

public class Customer {
	private Long customerNum;
	
	private String firstName;
	
	private String lastName;
	
	private List<String> midleNames;

	public Long getCustomerNum() {
		return customerNum;
	}

	public void setCustomerNum(Long customerNum) {
		this.customerNum = customerNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<String> getMidleNames() {
		return midleNames;
	}

	public void setMidleNames(List<String> midleNames) {
		this.midleNames = midleNames;
	}

	@Override
	public String toString() {
		return "Customer [customerNum=" + customerNum + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", midleNames=" + midleNames + "]";
	}
	
	

}
