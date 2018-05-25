package test;

import org.junit.Test;

public class Demo {
	// https://www.yeepay.com/app-merchant-proxy/node?p0_Cmd=Buy&p1_MerId=10001126856&p2_Order=123456&p3_Amt=0.1&p4_Cur=CNY&p5_Pid=&p6_Pcat=&p7_Pdesc=&p8_Url=http://localhost:8080/bookstore/OrderServlet?method=back&p9_SAF=&pa_MP=&pd_FrpId=CCB-NET-B2C&pr_NeedResponse=1&hmac=5b042c0428052c5f8cb2f641fa00c95e
	@Test
	public void fun() {
		String hmac = PaymentUtil.buildHmac("Buy", "10001126856", "123456",
				"0.1", "CNY", "", "", "",
				"http://localhost:8080/bookstore/OrderServlet?method=back", "",
				"", "CCB-NET-B2C", "1",
				"69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl");
		System.out.println(hmac);
	}
}
