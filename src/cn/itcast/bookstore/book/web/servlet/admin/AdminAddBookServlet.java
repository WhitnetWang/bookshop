package cn.itcast.bookstore.book.web.servlet.admin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.bookstore.book.domain.Book;
import cn.itcast.bookstore.book.service.BookService;
import cn.itcast.bookstore.category.domain.Category;
import cn.itcast.bookstore.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;

public class AdminAddBookServlet extends HttpServlet {
	private BookService service = new BookService();
	private CategoryService categoryService = new CategoryService();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		/*
		 * 上传三步
		 */
		// 创建工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 创建解析器
		ServletFileUpload upload = new ServletFileUpload(factory);
		// 设置单个表单项最大为15kb
		upload.setFileSizeMax(15 * 1024);
		try {
			// 得到表单项
			List<FileItem> fileItemList = upload.parseRequest(request);
			Map<String, String> map = new HashMap<String, String>();
			/*
			 * 封装到map中
			 */
			for (FileItem fileItem : fileItemList) {
				if (fileItem.isFormField()) {
					map.put(fileItem.getFieldName(),
							fileItem.getString("utf-8"));
				}
			}

			Book book = CommonUtils.toBean(map, Book.class);
			book.setBid(CommonUtils.uuid());
			// 得到文件保存路径
			String savePath = this.getServletContext().getRealPath("/book_img");
			// 得到文件名
			String fileName = CommonUtils.uuid() + "_"
					+ fileItemList.get(1).getName();
			//校验图片的扩张名
			if(!fileName.toLowerCase().endsWith("jpg")){
				request.setAttribute("msg", "您上传的图片不是jpg格式的");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(
						request, response);
				return;
			}
			// 得到目标文件
			File destFile = new File(savePath, fileName);
			// 保存
			fileItemList.get(1).write(destFile);
			
			book.setImage("book_img/" + fileName);
			
			Category category = CommonUtils.toBean(map, Category.class);
			book.setCategory(category);
			book.setDel(false);

			// 添加到数据库中
			service.add(book);
			request.setAttribute("msg", "添加成功");
			request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request,
					response);

		} catch (Exception e) {
			if (e instanceof FileSizeLimitExceededException) {
				request.setAttribute("msg", "图片最大允许为15KB");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(
						request, response);
			}
		}
	}
}
