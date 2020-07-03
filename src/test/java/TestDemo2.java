
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import test001.DemoApplication;

@SpringBootTest(classes = {DemoApplication.class})
public class TestDemo2 {

    private MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    @BeforeEach
    void begin(){
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void login() throws Exception {

        String requestBody = "{\"name\" : \"刘恒\", \"password\" : \"123456\" }";

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/lh/findByNameAndPassword")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);
        ResultActions perform = mvc.perform(request);

        MvcResult mvcResult = perform.andReturn();

        int status = mvcResult.getResponse().getStatus();

        System.out.println(mvcResult.getResponse().getContentAsString());

        System.out.println(status);
    }

    @Test
    void findById() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/lh/byId")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id","1");

        ResultActions perform = mvc.perform(request);

        MvcResult mvcResult = perform.andReturn();

        System.out.println(mvcResult.getResponse().getStatus());





    }

}
