package com.zerobase.everycampingbackend.domain.auth.service;

import com.zerobase.everycampingbackend.domain.auth.dto.KakaoToken;
import com.zerobase.everycampingbackend.domain.auth.dto.OAuth2Attributes;
import com.zerobase.everycampingbackend.domain.user.form.SocialSignInForm;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserService {
    private final InMemoryClientRegistrationRepository clientRegistrationRepository;

    public SocialSignInForm signIn(String providerName, String code){
        ClientRegistration provider = clientRegistrationRepository.findByRegistrationId(providerName);
        KakaoToken tokens = getTokens(provider, code);
        return getFormFromUserProfile(provider, tokens.getAccess_token());
    }

    private SocialSignInForm getFormFromUserProfile(ClientRegistration provider, String token) {
        Map<String, Object> map = getUserAttributes(provider, token);
        OAuth2Attributes attributes = OAuth2Attributes.of(provider.getRegistrationId(), map);
        return new SocialSignInForm(attributes.getEmail(), attributes.getName());
    }

    private Map<String, Object> getUserAttributes(ClientRegistration provider, String token) {
        return WebClient.create()
            .get()
            .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
            .headers(header -> header.setBearerAuth(token))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();
    }

    private KakaoToken getTokens(ClientRegistration provider, String code) {
        return WebClient.create()
            .post()
            .uri(provider.getProviderDetails().getTokenUri())
            .headers(header -> header.setContentType(MediaType.APPLICATION_FORM_URLENCODED))
            .bodyValue(tokenRequest(provider, code))
            .retrieve()
            .bodyToMono(KakaoToken.class)
            .block();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(tokenRequest(provider, code), headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.postForEntity(provider.getProviderDetails().getTokenUri(),
//            request, String.class);
//        try {
//            return new ObjectMapper().readValue(response.getBody(), KakaoToken.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
    }

    private MultiValueMap<String, String> tokenRequest(ClientRegistration provider, String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("grant_type", provider.getAuthorizationGrantType().getValue());
        map.add("redirect_uri", provider.getRedirectUri());
        map.add("client_id", provider.getClientId());
        map.add("client_secret", provider.getClientSecret());
        return map;
    }
}
