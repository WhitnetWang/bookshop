package cn.itcast.bookstore.cart.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart implements Serializable{
	private Map<String, CartItem> map = new LinkedHashMap<String, CartItem>();

	public double getTotal(){
		BigDecimal total = new BigDecimal("0");
		
		for(CartItem cartitem : map.values()){
			total = total.add(new BigDecimal(cartitem.getSubtotal() + ""));
		}
		
		return total.doubleValue();
	}
	
	public void add(CartItem cartItem) {
		if (map.containsKey(cartItem.getBook().getBid())) {
			CartItem _CartItem = map.get(cartItem.getBook().getBid());
			_CartItem.setCount(_CartItem.getCount() + cartItem.getCount());
			map.put(_CartItem.getBook().getBid(), _CartItem);
		} else {
			map.put(cartItem.getBook().getBid(), cartItem);
		}
	}

	public void clear() {
		map.clear();
	}

	public void delete(String bid) {
		map.remove(bid);
	}

	public Collection<CartItem> getCartItems() {
		return map.values();
	}
	
}
