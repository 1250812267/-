package fileTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileCopy {
	public static void main(String[] args) {
		try {
			long  stime=System.currentTimeMillis();
			FileInputStream fi=new FileInputStream("C:\\Users\\12508\\Desktop\\雏鹰计划.xlsx");
			FileOutputStream fo=new FileOutputStream("D:\\newFile\\雏鹰计划.xlsx");
			byte data[]=new byte[1024];
			while(true) {
				int ret=fi.read(data);
				if(ret<0) {
					break;
				}
				
				fo.write(data,0, ret);
			}
			long etime=System.currentTimeMillis();
			System.out.println("时间"+(etime-stime));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
