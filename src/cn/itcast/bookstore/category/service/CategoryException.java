package cn.itcast.bookstore.category.service;

public class CategoryException extends Exception {

	public CategoryException() {
		super();
	}

	public CategoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public CategoryException(String message) {
		super(message);
	}

	public CategoryException(Throwable cause) {
		super(cause);
	}

}
