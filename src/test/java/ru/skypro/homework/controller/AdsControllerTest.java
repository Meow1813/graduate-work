package ru.skypro.homework.controller;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skypro.homework.TestContainersInit;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import javax.transaction.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdsControllerTest{ //extends TestContainersInit {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdRepository adRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @InjectMocks
    private AdsController adsController;

    @BeforeEach
    public void setup() {
        UserEntity userEntity = new UserEntity(1L, "testUsername@mail.com", encoder.encode("password"),"Name", "Family Name", "+7(123)1232323", Role.USER, null, null, null);
        userRepository.save(userEntity);
        AdEntity adEntity = new AdEntity();
        adEntity.setAuthor(userEntity);
        adEntity.setImage("imageTest");
        adEntity.setPrice(111);
        adEntity.setTitle("title Test");
        adEntity.setDescription("description Test");
        adRepository.save(adEntity);
    }

    @Test
    @Disabled
    void addAd() throws Exception {

        JSONObject adJson = new JSONObject();
        adJson.put("price", 111);
        adJson.put("title", "test Title1");
        adJson.put("description", "test Description1");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/ads")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("testUsername@mail.com", "password", StandardCharsets.UTF_8))
                        .content(adJson.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test Title1"));
    }

    @Test
    void getAllAds() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/ads")
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
//    @Disabled
    void getAdInfo() throws Exception {
        AdEntity adEntity = adRepository.findAll().stream().findAny().get();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads/" + adEntity.getPk())
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("testUsername@mail.com", "password", StandardCharsets.UTF_8))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
//    @Disabled
    void deleteAd() throws Exception {

        AdEntity adEntity = adRepository.findAll().stream().findAny().get();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/ads/" + adEntity.getPk())
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("testUsername@mail.com", "password", StandardCharsets.UTF_8))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Disabled
    void patchAd() {
    }

    @Test
//    @Disabled
    void getAdsMe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ads")
                        .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("testUsername@mail.com", "password", StandardCharsets.UTF_8))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.count").value(1));
    }

    @Test
    @Disabled
    void updateImage() {
    }

    @Test
    @Disabled
    void getImage() {
    }
}