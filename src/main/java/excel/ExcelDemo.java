package excel;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ExcelDemo {
    /**
     * 导入Excel数据

     */
//    @RequestMapping(value = "import", method= RequestMethod.POST)
//    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
//        try {
//            int successNum = 0;
//            int failureNum = 0;
//            StringBuilder failureMsg = new StringBuilder();
//            ImportExcel ei = new ImportExcel(file, 1, 0);
//            List<TestPieClass> list = ei.getDataList(TestPieClass.class);
//            for (TestPieClass testPieClass : list){
//                try{
//                    testPieClassService.save(testPieClass);
//                    successNum++;
//                }catch(ConstraintViolationException ex){
//                    failureNum++;
//                }catch (Exception ex) {
//                    failureNum++;
//                }
//            }
//            if (failureNum>0){
//                failureMsg.insert(0, "，失败 "+failureNum+" 条学生记录。");
//            }
//            addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学生记录"+failureMsg);
//        } catch (Exception e) {
//            addMessage(redirectAttributes, "导入学生失败！失败信息："+e.getMessage());
//        }
//        return "redirect:"+Global.getAdminPath()+"/echarts/other/testPieClass/?repage";
//    }
}
