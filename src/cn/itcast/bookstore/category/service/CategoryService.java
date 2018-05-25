package cn.itcast.bookstore.category.service;

import java.util.List;

import cn.itcast.bookstore.book.dao.BookDao;
import cn.itcast.bookstore.category.dao.CategoryDao;
import cn.itcast.bookstore.category.domain.Category;

public class CategoryService {
	private CategoryDao dao = new CategoryDao();
	private BookDao bookDao = new BookDao();
	/*
	 * 查询所有
	 */
	public List<Category> findAll() {
		return dao.findAll();
	}
	/*
	 * 添加图书
	 */
	public void add(Category category) {
		dao.add(category);
	}
	/*
	 * 删除分类
	 */
	public void delele(String cid) throws CategoryException{
		int count = bookDao.getCountByCid(cid);
		if(count > 0){
			throw new RuntimeException("该分类下有图书,不能删除");
		}
		//
		dao.delete(cid);
	}
	/*
	 * 加载分类
	 */
	public Category load(String cid) {
		return dao.load(cid);
	}
	/*
	 * 修改分类名称
	 */
	public void edit(Category category) {
		dao.edit(category);
	}
}
