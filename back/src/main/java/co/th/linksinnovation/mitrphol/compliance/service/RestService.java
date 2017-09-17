/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.service;

import co.th.linksinnovation.mitrphol.compliance.model.authen.Authenticate;
import co.th.linksinnovation.mitrphol.compliance.model.authen.Data;
import co.th.linksinnovation.mitrphol.compliance.model.authen.Fullname;
import co.th.linksinnovation.mitrphol.compliance.model.authen.Success;
import co.th.linksinnovation.mitrphol.compliance.model.authen.UserInfo;
import java.util.Arrays;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jirawong
 */
@Service
public class RestService {

    public Authenticate findUserInfo(String id) {
        if(Arrays.asList(new String[] {"99999999","99999998","99999997"}).contains(id)){
            Authenticate authenticate = new Authenticate();
            Success success = new Success();
            success.setCode(200L);
            Data data = new Data();
            UserInfo userInfo = new UserInfo();
            Fullname fullname = new Fullname();
            if("99999999".equals(id)){
                userInfo.setId(id);
                userInfo.setUsername("wijayak");
                userInfo.setEmail("wijayak@ust.co.th");
                fullname.setEn("Wijaya Klinkesorn");
                fullname.setTh("วิจะยะ กลิ่นเกษร");
            }else if("99999998".equals(id)){
                userInfo.setId(id);
                userInfo.setUsername("kittiyakl");
                userInfo.setEmail("kittiyakl@ust.co.th");
                fullname.setEn("Kittiya KLYSANG");
                fullname.setTh("กิตติยา คล้ายสังข์");
            }else if("99999997".equals(id)){
                userInfo.setId(id);
                userInfo.setUsername("achiravichs");
                userInfo.setEmail("achiravichs@ust.co.th");
                fullname.setEn("Achiravich SUVARNAROJ");
                fullname.setTh("อชิรวิชย์ สุวรรณโรจน์");
            }
            userInfo.setFullname(fullname);
            data.setUserInfo(userInfo);
            success.setData(data);
            authenticate.setSuccess(success);
            return authenticate;
        }
        
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Api-Key", "$2y$10$Pc0lTscxUAlq9O5V8Arwau6VpgLlMEj9xLAPymFqbay2mbM3qJJee");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("id", id);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Authenticate> exchange = restTemplate.exchange("https://api.mitrphol.com:3001/employee",HttpMethod.POST, request, Authenticate.class);
        
        return exchange.getBody();
    }
}
