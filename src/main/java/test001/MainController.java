package test001;


import excel.ExcelController;
import jxl.read.biff.BiffException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

import java.util.*;


@Controller
@RequestMapping("/lh")
public class MainController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/hello")
    public @ResponseBody
    String getHelloworld() {
        return "hello world";
    }

    @PostMapping("/upload")
    public static void uploadFile(@RequestParam byte[] file, @RequestParam String filePath, @RequestParam String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);

        out.write(file);
        out.flush();
        out.close();
    }



    @GetMapping("/download")
    public void downLoad(HttpServletResponse response) throws IOException {
        //获取输入流
        InputStream bis = new BufferedInputStream(new FileInputStream(new File("D:\\newFile\\url.txt")));
        //转码，免得文件名中文乱码
        String filename = URLEncoder.encode("测试","UTF-8");
        //设置文件下载头
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int len = 0;
        while((len = bis.read()) != -1){
            out.write(len);
            out.flush();
        }
        out.close();
    }

    //增加用户信息
    @PostMapping("/add")
    public @ResponseBody
    String addNewUser(@RequestParam("name") String name, @RequestParam("password") String password, @RequestParam("roleId") Integer roleId) {
        User n = new User();
        n.setName(name);
        n.setPassword(password);
        n.setRoleId(roleId);
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping("/excel")
    public @ResponseBody
    String  excelUser() throws IOException, BiffException {
        ExcelController excel = new ExcelController("C:\\Users\\12508\\Desktop\\用户表.xls");
        excel.readExcel();
        List list = excel.outData();
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
			String[] str = (String[]) list.get(i);
				User n = new User();
				n.setName(str[1]);
				n.setPassword(str[2]);
				n.setRoleId(Integer.parseInt(str[3]));
				userList.add(n);
		}
        userRepository.saveAll(userList);
        return "saved";
    }

    //迭代查询所有用户信息
    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<User> getAllUsers() {

        return userRepository.findAll(Sort.by("id"));

    }



    //查询账号密码
    @PostMapping(path = "/findByNameAndPassword")
    public @ResponseBody
    String getUserByNameAndPassword(@RequestBody User user) {

        if (userRepository.findByNameAndPassword(user.getName(), user.getPassword()) == null) {

            return "登录失败！";
        } else {

            return "登录成功！";
        }
    }

    //按照id查询用户信息
    @GetMapping(path = "/byId")
    public @ResponseBody
    //请求参数id映射绑定函数参数id,函数参数与数据库参数已在实体通过注解映射绑定
    User getUserById(@RequestParam("id") Integer id) {
        // This returns a JSON or XML with the users
        return userRepository.findById(id).get();

    }

    //按照id删除用户
    @PostMapping(path = "/deleteById")
    public @ResponseBody
    void delUser(@RequestParam("id") Integer id) {
        userRepository.deleteById(id);
    }


    @PostMapping("/update")
    @ResponseBody
    public String updateUser (@RequestParam("id") Integer id, @RequestParam("name") String name){
        User user = userRepository.findById(id).get();
        user.setName(name);
        userRepository.save(user);
        return "update";
    }

    @PostMapping("/find")
    @ResponseBody
    public  List<User>  test(@RequestBody User u ){
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Path<Object> password =  root.get("password");

                Path<Object> userName =  root.get("name");

                Predicate p1 =  null;

                if(u.getName() !=""){
                    Predicate p2 =  criteriaBuilder.equal( userName,u.getName());
                    if(p1!=null){
                        p1 = criteriaBuilder.and(p1, p2);
                    }else{
                        p1 = p2;
                    }
                }
                if(u.getPassword() !=""){
                    Predicate p2 =  criteriaBuilder.equal( password,u.getPassword());
                    if(p1!=null){
                        p1 = criteriaBuilder.and(p1, p2);
                    }else{
                        p1 = p2;
                    }
                }
                return p1;


            }
        };

        List<User> list =  userRepository.findAll(spec);
        return list;
    }




}
