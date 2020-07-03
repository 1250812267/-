package excel;

import jxl.Cell;

import jxl.Sheet;

import jxl.Workbook;

import jxl.read.biff.BiffException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import test001.User;
import test001.UserRepository;

import java.io.FileInputStream;

import java.io.IOException;

import java.io.InputStream;

import java.util.ArrayList;

import java.util.List;


@Controller
public class ExcelController {

	UserRepository userRepository;

	private String filePath;

	private List list = new ArrayList();

	public ExcelController(String filePath) {

		this.filePath = filePath;

	}

	public void readExcel() throws IOException, BiffException {

		// 创建输入流

		InputStream stream = new FileInputStream(filePath);

		// 获取Excel文件对象

		Workbook rwb = Workbook.getWorkbook(stream);

		// 获取文件的指定工作表 默认的第一个

		Sheet sheet = rwb.getSheet(0);

		// 行数(表头的目录不需要，从1开始)

		for (int i = 1; i < sheet.getRows(); i++) {

			// 创建一个数组 用来存储每一列的值

			String[] str = new String[sheet.getColumns()];

			Cell cell = null;

			// 列数

			for (int j = 0; j < sheet.getColumns(); j++) {

				// 获取第i行，第j列的值

				cell = sheet.getCell(j, i);

				str[j] = cell.getContents();

			}

			// 把刚获取的列存入list

			list.add(str);

		}

	}

	public List outData() {

		return list;
//		for (int i = 0; i < list.size(); i++) {
//
//			String[] str = (String[]) list.get(i);
//
////			for (int j = 0; j < str.length; j++) {
//
//				User n = new User();
//				n.setName(str[1]);
//				n.setPassword(str[2]);
//				n.setRoleId(Integer.parseInt(str[3]));
//				userRepository.save(n);
////				System.out.print(str[j] + '\t');
//
////			}
//
//			System.out.println();
//
//		}
	}

	public static void main(String args[]) throws BiffException, IOException {

		ExcelController excel = new ExcelController("C:\\Users\\12508\\Desktop\\用户表.xls");

		excel.readExcel();

		excel.outData();

	}

	@RequestMapping("toExcel")

	public String toExcel() {

		return "admin/Excel";

	}

}
