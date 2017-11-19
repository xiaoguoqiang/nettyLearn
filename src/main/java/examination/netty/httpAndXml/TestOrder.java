package examination.netty.httpAndXml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import examination.netty.httpAndXml.pojo.Order;

public class TestOrder {
	private IBindingFactory factory = null;
	private StringWriter writer = null;
	private StringReader reader = null;
	private final static String CHARSET_NAME = "UTF-8";
	
	private String encode2Xml(Order order) throws JiBXException, IOException {
		factory = BindingDirectory.getFactory(Order.class);
		writer = new StringWriter();
		IMarshallingContext ctx = factory.createMarshallingContext();
		ctx.setIndent(2);
		ctx.marshalDocument(order, CHARSET_NAME, null,writer);
		String xmlStr = writer.toString();
		writer.close();
		System.out.println(xmlStr);
		return xmlStr;
	}
	
	private Order decoder2Order(String xmlBody) throws JiBXException {
		reader = new StringReader(xmlBody);
		IUnmarshallingContext ctx = factory.createUnmarshallingContext();
		Order order = (Order)ctx.unmarshalDocument(reader);
		return order;
	}
	
	public static void main(String[] args) throws JiBXException, IOException {
		TestOrder test = new TestOrder();
		Order order = new Order();
		order.setOrderNum(Long.valueOf("123"));
		String xml = test.encode2Xml(order);
		Order order2 = test.decoder2Order(xml);
		System.out.println(order2.toString());
	}

}
