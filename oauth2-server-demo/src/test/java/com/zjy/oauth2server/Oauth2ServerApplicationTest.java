package com.zjy.oauth2server;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjy.oauth2server.pojo.entity.oauth2.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.KeyPair;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * oauth2 的测试用例需要 resourceConfig 开启 stateless(ture) 配置，生产环境必须关闭
 *
 * @author liugenlai
 * @since 2021/8/18 16:41
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Oauth2ServerApplicationTest {
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KeyPair keyPair;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    /**
     * 生成想要的加密密码或者clientSecret
     * 验证密码和原始密码是否一致
     */
    @Test
    public void testPasswordEncoder() {
        System.out.println(passwordEncoder.encode("ssoj-secret"));
        System.out.println(passwordEncoder.encode("1234"));
        System.out.println(passwordEncoder.matches("123456", "$2a$10$N.k6Q38FB0tlqyb3Af..se98ENWUnl3OB6b0RNtj8C6KH75Jo5V6a"));
        System.out.println(passwordEncoder.matches("1234", "$2a$10$1/.d9VrwDf4bRMK9rTsw/.qS2L1pZw6r8nxoP4yF1wLl3DmVB5l6C"));
    }

    /**
     * 测试 RSA 非对称加密算法
     */
    @Test
    public void testRSA() {
        RSA rsa = new RSA(keyPair.getPrivate(), keyPair.getPublic());
        System.out.println(rsa.encryptBase64("1234", KeyType.PublicKey));
        System.out.println(rsa.decryptStr("Y6SwUILHu5eze8/bAwYKGEvv0fFy1bvfVf8JHYTra6K67Yo8H3vrEgc7dnD12Mi4VB6wod17ENMbKPiftloHCg3OLjMKazIhp9t+ELACesLC+7Ma9iYlX3ZwWu3F0hP+3IvUlIBN0I8wqw08EoW2Y7a9Z3+DN3Epusbv1MHpkxsGRKAUVjf8DeTCcu50mG8zrNyCDWJt4U1pzaTsRrJd3+fTtl+TQyKddP5y7yOgtJDywTC9LVXrn4kEstEbYEkneLB9jujW64xUXvOzdiiDWfe+6jiPhp0+rKw+/ae9ykMuMDMR0X++flj+CGHv2H9j9gKx8GX67EPV9D4fyxXUjQ==", KeyType.PrivateKey));
    }

//    @WithMockUser
//    @Test
//    public void givenAuthRequest_shouldSucceedWith200() throws Exception {
//        ResponseEntity<User> result = template
//                .getForEntity("/api/me", User.class);
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//    }

    /**
     * 客户端列表测试
     */
    @WithMockUser
    @Test
    public void clientListRequest_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/client/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        // assertTrue(optionalUser.isPresent());
    }

    /**
     * 客户端新增测试
     */
    @WithMockUser
    @Test
    public void clientAddRequest_shouldSuccessWith200() throws Exception {
        Client client = Client.builder()
                .clientId("web-test").resourceIds("product").clientSecret(passwordEncoder.encode("123456"))
                .scope("API").authorizedGrantTypes("client_credentials")
                .accessTokenValidity(300).refreshTokenValidity(300).autoapprove("ture").build();
        mvc.perform(post("/client/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /**
     * 客户端查询测试
     */
    @WithMockUser
    @Test
    public void clientGetRequest_shouldSuccessWith200() throws Exception {
        mvc.perform(get("/client/get/web-test"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.clientId").value("web-test"));
    }

    /**
     * 客户端修改测试
     */
    @WithMockUser
    @Test
    public void clientUpdateRequest_shouldSuccessWith200() throws Exception {
        Client client = Client.builder()
                .clientId("web-test").resourceIds("product").clientSecret(passwordEncoder.encode("1234"))
                .scope("API").authorizedGrantTypes("client_credentials")
                .accessTokenValidity(300).refreshTokenValidity(300).autoapprove("ture").build();
        mvc.perform(post("/client/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(client)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
