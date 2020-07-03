package fileTest;


import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

public class FileUtil {

    /**
     * 上传文件
     *
     * @param file
     * @param filePath
     * @param fileName
     * @throws Exception
     */
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

    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @throws Exception
     */
    public static boolean exists(String filePath) {
        File targetFile = new File(filePath);
        return targetFile.exists();
    }

    /**
     * 删除文件
     *
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
    * 改名
    * @Description TODO
    * @Author liuh@huayei.com
    * @Date 2020/6/30 10:12
    * @Since 1.0
    */
    public static String renameToUUID(String fileName) {
        return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static void download(HttpServletResponse res, String path, String fileName) {
        downloadLocal(res, path, fileName);
    }

    /**
     * 下载本地文件
     * @param response
     * @param path
     * @param fileName
     */
    public static void downloadLocal(HttpServletResponse response, String path, String fileName) {
        FileInputStream fileIn = null;
        ServletOutputStream out = null;
        try {
            //String fileName = new String(fileNameString.getBytes("ISO8859-1"), "UTF-8");
            response.setContentType("application/octet-stream");
            // URLEncoder.encode(fileNameString, "UTF-8") 下载文件名为中文的，文件名需要经过url编码
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            File file;
            String filePathString = path + fileName;
            file = new File(filePathString);
            fileIn = new FileInputStream(file);
            out = response.getOutputStream();

            byte[] outputByte = new byte[1024];
            int readTmp = 0;
            while ((readTmp = fileIn.read(outputByte)) != -1) {
                out.write(outputByte, 0, readTmp); //并不是每次都能读到1024个字节，所有用readTmp作为每次读取数据的长度，否则会出现文件损坏的错误
            }
        } catch (Exception e) {
            //log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                fileIn.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //执行上传文件操作

    @RequestMapping(value="/shangChuan",method= RequestMethod.POST)

    public String Chuan(HttpServletRequest req, @RequestParam(value="file") MultipartFile file)throws Exception{

        if(!file.isEmpty()){

            String path=req.getServletContext().getRealPath("//base//upload");//此处是获取上传文件的地址，其实就是在获取该项目的路径

            System.out.print(path);//这里可以看到req.getServletContext().getRealPath("//base//upload")到底获取的是什么路径，当时我懵逼了好长时间。

            String  filename=file.getOriginalFilename();

            File filepath=new File(path,filename);

//判断要存储的路径是否为空，为空就直接创建一个。

            if(!filepath.exists()){

                filepath.mkdirs();

            }

            file.transferTo(filepath);   //最核心的就是这个transferTo(),直接将前台上传的文件上床到制定文件路径下。

            return "/pages/systemMa/zuZhi.jsp";

        } return"/pages/systemMa/zuZhi.jsp";

    }

    @PostMapping("upload.lh")
    public JsonMessage upload(MultipartFile file) {
        File file1 = new File("D:/newFile/url.txt");
        System.out.println("file.getName():"+file.getName()); // 上传的文件类型 file
        System.out.println("file.getOriginalFilename():"+file.getOriginalFilename()); // 文件名称
        System.out.println("file.isEmpty():"+file.isEmpty()); // 文件是否为空
        System.out.println("file.getContentType():"+file.getContentType()); // 文件类型 image/gif
        String path="D:/newFile/" + file.getOriginalFilename();
        // 将文件保存到d盘
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(),
                    new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonMessage msg = new JsonMessage();
        msg.getDatas().put("path", path);
        msg.setMsg("success");
        return msg;
    }

    /**
     * 下载网络文件
     * @param fileUrl
     * @param fileLocal
     * @return
     * @throws Exception
     */
    public static boolean downloadNet(String fileUrl, String fileLocal) throws Exception {
        boolean flag = false;
        URL url = new URL(fileUrl);
        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
        urlCon.setConnectTimeout(6000);
        urlCon.setReadTimeout(6000);
        int code = urlCon.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new Exception("文件读取失败");
        }
        //读文件流
        DataInputStream in = new DataInputStream(urlCon.getInputStream());
        DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal));
        byte[] buffer = new byte[2048];
        int count = 0;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        flag = true;
        return flag;
    }
    }