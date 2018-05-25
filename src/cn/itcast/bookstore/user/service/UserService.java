package cn.itcast.bookstore.user.service;

import cn.itcast.bookstore.user.dao.UserDao;
import cn.itcast.bookstore.user.domain.User;

public class UserService {
	private UserDao dao = new UserDao();

	/**
	 * 注册
	 * 
	 * @param form
	 * @throws UserException
	 */
	public void regist(User form) throws UserException {
		User user = dao.findByUsername(form.getUsername());
		// 校验用户名
		if (user != null) {
			throw new UserException("该用户名已存在");
		}

		user = dao.findByEmail(form.getEmail());
		// 校验邮箱
		if (user != null) {
			throw new UserException("该邮箱已存在");
		}
		// 添加用户
		dao.add(form);
	}

	/*
	 * 激活用户
	 */
	public void active(String code) throws UserException {
		User user = dao.findByCode(code);

		if (user == null) {
			throw new UserException("激活码无效");
		}

		if (user.isState()) {
			throw new UserException("您已经激活过了");
		}

		dao.updateState(user.getUid(), true);
	}

	/*
	 * 登录
	 */
	public User login(User form) throws UserException {
		User user = dao.findByUsername(form.getUsername());
		
		if (user == null) {
			throw new UserException("用户不存在");
		}

		if (!user.getPassword().equals(form.getPassword())) {
			throw new UserException("密码错误");
		}

		if (!user.isState()) {
			throw new UserException("尚未激活");
		}
		
		return user;

	}
	/*
	 * 异步校验用户名是否存在
	 */
	public boolean validate(String username) {
		User user = dao.findByUsername(username);
		
		if(user != null){
			return true;
		}
		
		return false;
	}
}
