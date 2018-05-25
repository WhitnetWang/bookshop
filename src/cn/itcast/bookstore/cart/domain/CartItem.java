package cn.itcast.bookstore.cart.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import cn.itcast.bookstore.book.domain.Book;

public class CartItem implements Serializable{
	private Book book;
	private int count;

	public double getSubtotal() {
		return new BigDecimal(book.getPrice() + "").multiply(
				new BigDecimal(count + "")).doubleValue(); // 小计
	}

	public CartItem() {
		super();
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
