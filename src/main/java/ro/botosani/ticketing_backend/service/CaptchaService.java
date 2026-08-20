package ro.botosani.ticketing_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ro.botosani.ticketing_backend.dto.CaptchaResponse;

@Service
public class CaptchaService {

    private static final String GOOGLE_RECAPTCHA_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify";
    // Cheia secretă de test oferită de Google (nu o expune pe frontend)
    private final String RECAPTCHA_SECRET = "6LeozYwtAAAAABpqYyyyYc6t1I0aRxMxEq9V6OJY";

    public boolean validateCaptcha(String captchaResponse) {
        if (captchaResponse == null || captchaResponse.isEmpty()) {
            return false;
        }

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", RECAPTCHA_SECRET);
        requestMap.add("response", captchaResponse);

        CaptchaResponse apiResponse = restTemplate.postForObject(
                GOOGLE_RECAPTCHA_ENDPOINT,
                requestMap,
                CaptchaResponse.class
        );

        return apiResponse != null && apiResponse.isSuccess();
    }
}