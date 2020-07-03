import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import test001.DemoApplication;
import test001.UserRepository;
@SpringBootTest(classes = {DemoApplication.class})
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestDemo {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void login(){
        if (userRepository.findByNameAndPassword("刘恒", "123456") == null) {
            System.out.println("------登录失败------");
        } else {
            System.out.println("------登录成功------");
        }
    }

    @Test
    public void test(){
        System.out.println("急啊急啊急啊就");
    }

//    @BeforeAll
//    public void beforeAll(){
//        System.out.println("------测试环境搭建完成------");
//    }
//
//    @AfterAll
//    public void afterAll(){
//        System.out.println("------测试环境清理完成------");
//    }
//
//    @BeforeEach
//    public void beforeEach(){
//        System.out.println("------测试前置方法执行------");
//    }
//
//    @AfterEach
//    public void afterEach(){
//        System.out.println("------测试后置方法执行------");
//    }
//
//    @Test
//    @DisplayName("test 1")
//    public void myTest1(){
//        System.out.println("------测试方法1执行------");
//    }
//
//    @Test
//    @DisplayName("test 2")
//    public void myTest2() {
//        System.out.println("------测试方法2执行------");
//    }

}
