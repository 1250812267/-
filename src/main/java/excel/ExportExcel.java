package excel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import test001.User;


public class ExportExcel {
    private String message = "";

    /**
     * 上传excel文件
     * @param excelFile
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/saveExcel", produces = { "application/json;charset=UTF-8" }, method = {
            RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String readXls(@RequestParam MultipartFile[] excelFile) throws IOException {
        long startTime=System.currentTimeMillis();
        String originalFilename;
        int r = 0;
        if (excelFile == null || excelFile.length == 0) {
            return JSONObject.toJSONString("文件上传失败，请重试");
        }
        try {
            for (MultipartFile myfile : excelFile) {
                if (myfile.isEmpty()) {
                    return JSONObject.toJSONString("文件为空");
                } else {
                    originalFilename = myfile.getOriginalFilename();
                    if (!(originalFilename.endsWith(".xls") || originalFilename.endsWith(".xlsx"))) {
                        return JSONObject.toJSONString("文件格式错误，请重试");
                    }

                    List<User> list = null;
                    /*根据文件格式解析文件*/
                    if (originalFilename.endsWith(".xls")) {
                        list = readXLS(myfile);
                    } else if (originalFilename.endsWith(".xlsx")) {
                        list = readXLSX(myfile);
                    }
                    /*批量插入数据*/
                    if(!CollectionUtils.isEmpty(list)){
                        batchInsertData(list);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return JSONObject.toJSONString("文件解析失败，请确认Excel表中的数据无误");
        }
        long hs=System.currentTimeMillis()-startTime;
        return JSONObject.toJSONString(message+"导入数据总耗时："+hs+"毫秒");
    }

    /**
     * 解析.xls格式的excel文件
     * @param file
     * @return
     * @throws IOException
     */
    public List<User> readXLS(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        User user;
        List<User> list = new ArrayList<>();
        // 循环工作表Sheet
        // 循环行Row
        int errorNum = 0;
        int okNum = 0;
        String errorMsg = "";
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
            HSSFRow hssfRow = hssfSheet.getRow(rowNum);
            if (hssfRow != null) {
                user = new User();
                HSSFCell txt = hssfRow.getCell(0);
                if (txt == null) {
                    errorNum += 1;
                    errorMsg += "第" + (rowNum + 1) + "行（数据）格式错误,不能为空&";
                    continue;
                }
                txt.setCellType(CellType.STRING);
                if(StringUtils.isBlank(txt.getStringCellValue())){
                    errorNum += 1;
                    errorMsg += "第" + (rowNum + 1) + "行（数据）格式错误,不能为空&";
                    continue;
                }
//                entity.setPhone(txt.getStringCellValue());
//                entity.setStatus(1);
//                entity.setCreate_time(new Date());
                list.add(user);
                okNum += 1;
            }

        }
        message = "导入结果:&成功导入" + okNum + "条数据 失败" + errorNum + "条&错误记录:&"
                + errorMsg;

        if(is!=null){
            is.close();
        }
        return list;
    }

    /**
     * 解析.xlsx格式的excel文件
     * @param file
     * @return
     * @throws IOException
     */
    public List<User> readXLSX(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        User user;
        List<User> list = new ArrayList<>();
        // 循环工作表Sheet
        // 循环行Row
        int errorNum = 0;
        int okNum = 0;
        String errorMsg = "";
        XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
        for (int rowNum = 0; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
            XSSFRow xssfRow = xssfSheet.getRow(rowNum);
            if (xssfRow != null) {
                user = new User();
                XSSFCell txt = xssfRow.getCell(0);
                if (txt == null) {
                    errorNum += 1;
                    errorMsg += "第" + (rowNum + 1) + "行（数据）格式错误,不能为空&";
                    continue;
                }
                txt.setCellType(CellType.STRING);
                if(StringUtils.isBlank(txt.getStringCellValue())){
                    errorNum += 1;
                    errorMsg += "第" + (rowNum + 1) + "行（数据）格式错误,不能为空&";
                    continue;
                }
//                entity.setPhone(txt.getStringCellValue());
//                entity.setStatus(1);
//                entity.setCreate_time(new Date());
                list.add(user);
                okNum += 1;
            }
        }
        message = "导入结果:&成功导入" + okNum + "条数据 失败" + errorNum + "条&错误记录:&"
                + errorMsg;

        if(null!=is){
            is.close();
        }
        return list;
    }

    /**
     * 批量插入数据到数据库
     *
     * @param list*/
    private int batchInsertData(List<User> list) {
        int result = 0;
        if (CollectionUtils.isEmpty(list)) {
            return result;
        }
        //每次最多插入1000条数据
        int beatchMaxSize = 1000;
        while (list.size() > 0) {
            if (list.size() < beatchMaxSize) {
                beatchMaxSize = list.size();
            }
            try {
                List<User> batchList = list.subList(0, beatchMaxSize);
                /*批量写入数据库的方法*/
//                result  = batchSave(batchList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.removeAll(list.subList(0, beatchMaxSize));
        }
        return result;
    }
}
