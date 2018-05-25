package cn.itcast.bookstore.book.service;

import java.util.List;

import cn.itcast.bookstore.book.dao.BookDao;
import cn.itcast.bookstore.book.domain.Book;

public class BookService {
	private BookDao dao = new BookDao();

	/*
	 * 查询所有图书
	 */
	public List<Book> findAll() {
		return dao.findAll();
	}

	/*
	 * 按分类查找
	 */
	public List<Book> findByCategory(String cid) {
		return dao.findByCategory(cid);
	}

	public Book load(String bid) {
		return dao.load(bid);
	}

	/*
	 * 添加图书
	 */
	public void add(Book book) {
		dao.add(book);
	}

	/*
	 * 删除图书
	 */
	public void delete(String bid) {
		dao.delete(bid);
	}
	/*
	 * 修改
	 */
	public void edit(Book book) {
		dao.edit(book);
	}
	
}
