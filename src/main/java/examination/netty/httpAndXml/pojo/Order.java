package examination.netty.httpAndXml.pojo;

public class Order {

	private Long orderNum;

	private Customer customer;

	private Address billTo;

	private Shipping ship;

	private Address shipTo;

	private float total;

	public Long getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Long orderNum) {
		this.orderNum = orderNum;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Address getBillTo() {
		return billTo;
	}

	public void setBillTo(Address billTo) {
		this.billTo = billTo;
	}

	public Shipping getShip() {
		return ship;
	}

	public void setShip(Shipping ship) {
		this.ship = ship;
	}

	public Address getShipTo() {
		return shipTo;
	}

	public void setShipTo(Address shipTo) {
		this.shipTo = shipTo;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "Order [orderNum=" + orderNum + ", customer=" + customer + ", billTo=" + billTo + ", ship=" + ship
				+ ", shipTo=" + shipTo + ", total=" + total + "]";
	}

}
