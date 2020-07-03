package fileTest;

import java.io.File;
import java.io.FileFilter;

public class MyFileFilter implements FileFilter {
	/**
	* @Description TODO
	* @Author liuh@huayei.com
	* @Date 2020/7/1 14:41
	* @Since 1.0
	* 后缀名过滤
	*/
	@Override
	public boolean accept(File pathname) {
		if(pathname.getName().endsWith("txt")) {
			System.out.println("成功");
			return true;
		}
		System.out.println("失败");
		return false;

	}//过滤器
}
