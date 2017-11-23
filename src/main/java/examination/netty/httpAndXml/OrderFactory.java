package examination.netty.httpAndXml;

import examination.netty.httpAndXml.pojo.Address;
import examination.netty.httpAndXml.pojo.Customer;
import examination.netty.httpAndXml.pojo.Order;
import examination.netty.httpAndXml.pojo.Shipping;

public class OrderFactory {
	
	public static Order create(Long orderId) {
		Order order = new Order();
		order.setOrderNum(orderId);
		order.setTotal(999.999f);
		Address address = new Address();
		address.setCity("beijing");
		address.setCountry("zhongguo");
		address.setPostCode("6665656");
		address.setState("huanbaokejiyuan");
		address.setStreet1("jindailu");
		order.setBillTo(address);
		Customer cus = new Customer();
		cus.setCustomerNum(orderId);
		cus.setFirstName("li");
		cus.setLastName("qiang");
	    order.setCustomer(cus);
	    order.setShip(Shipping.INTERNATIONAL_MAIL);
	    order.setShipTo(address);
	    return order;
	}

}
