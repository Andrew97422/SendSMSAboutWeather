package Project.config;

import Project.model.ModelSMS;
import Project.service.ServiceSMS;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ConfigSMS {
    /*Подключается API для отправки СМС и отправляется*/
    ServiceSMS serviceSMS = new ServiceSMS();
    ModelSMS createSMS() throws IOException {
        return serviceSMS.create();
    }
    @Value("${NUMBER}")
    private String number;
    @Value("${email}")
    private String email;

    @Value("${api}")
    private String api;

    @Value("${scheme}")
    private String scheme;

    @Value("${host}")
    private String host;

    @Value("${URLway}")
    private String URLway;

    @Value("${sign}")
    private String sign;

    @Scheduled(cron = "${cron}")
    void sendSMS() throws IOException, URISyntaxException {
        ModelSMS sms = createSMS();

        URL url1 = new URL(scheme + "://" + email + ":" + api + "@" + host + URLway + "?number=" + number + "&text="
                + URLEncoder.encode(sms.toString(), StandardCharsets.UTF_8) + "&sign=" + sign);

        String authStr = email + ":" + api;
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> response = new RestTemplate().exchange(url1.toURI(), HttpMethod.GET, request, String.class);
        System.out.println(response.getBody());
    }
}