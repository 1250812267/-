package fileTest;

import java.io.File;

public class GetFilesDemo {
public static void main(String[] args) {
	//获取某个文件下的文件
	File dir=new File("D:\\newFile");//过滤器

	File[] subFile=dir.listFiles();
	for(int i=0;i<subFile.length;i++) {
		File file=subFile[i];
		System.out.println(file.getAbsolutePath()+"----"+file.length());
	}
}
}
