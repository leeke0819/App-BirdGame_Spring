package com.example.demo.service.Impl;

import com.example.demo.Dto.request.LoginRequestDto;
import com.example.demo.Dto.request.UpdateUserInfoRequestDto;
import com.example.demo.Dto.response.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.TokenProvider;
import com.example.demo.service.UserExperienceLevel;
import com.example.demo.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;
    private final BagRepository bagRepository;
    private final BirdRepository birdRepository;
    private final BookRepository bookRepository;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();


    public UserServiceImpl(AuthenticationManagerBuilder managerBuilder, TokenProvider tokenProvider, UserRepository userRepository, PasswordEncoder passwordEncoder, ItemRepository itemRepository, BagRepository bagRepository, BirdRepository birdRepository, BookRepository bookRepository) {
        this.managerBuilder = managerBuilder;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.itemRepository = itemRepository;
        this.bagRepository = bagRepository;
        this.birdRepository = birdRepository;
        this.bookRepository = bookRepository;
    }

    //함수로 분리...하는편이 좋습니다.

    public UserEntity saveUser(String email, String password, String nickname) {
        //전처리, 입력검사
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistsException("이미 존재하는 이메일입니다.");
        }
        if (userRepository.existsByNickname(nickname)) {
            throw new EntityExistsException("이미 존재하는 닉네임입니다.");
        }

        if (password.length() < 8 || password.length() > 12) {
            throw new IllegalArgumentException("비밀번호는 8자 이상, 12자 이하로 설정해야 합니다.");
        }

        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$")) {
            throw new IllegalArgumentException("비밀번호는 대문자, 소문자, 숫자를 최소 하나씩 포함해야 합니다.");
        }
        if (nickname.isEmpty() || nickname.length() > 5) {
            throw new IllegalArgumentException("닉네임은 1자 이상 5자 이하로 설정해야 합니다.");
        }

        //여기서 BagEntity를 만들고.... 그 안에, 알과 .. 초반아이템들을 조금 넣어줘야한다...
        return signUp(email, password, nickname);
    }

    @Override
    public TokenDto kakaoLogin(String authCode){
        //소셜로그인 1단계. acceessToken가져오기
        String accessToken = getAccessTokenFromAuthCode(authCode);
        //소셜로그인 2단계. 사용자 정보 가져오기.
        KakaoMeDto.KakaoMe kakaoUserInfo = getKakaoUserInfo(accessToken);
        String email = kakaoUserInfo.kakao_account().email();
        String nickname = kakaoUserInfo.kakao_account().profile().nickname();
        // 1) 소셜 전용 “원문 비밀번호”를 결정 (항상 동일한 규칙)
        String rawSocialPw = assemblePw(email);

        // 2) 최초 가입 시에만 DB에는 "인코딩된" 값 저장
        if(!userRepository.existsByEmail(email)) {
            String encoded = rawSocialPw;
            signUp(email, encoded, nickname); // signUp 내부에서 또 encode 하지 않도록 주의!
            log.info(encoded);
        }
        log.info(nickname);
        log.info(email);
        // 3) 인증할 때는 "원문"을 넣어야 함
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, rawSocialPw);

        log.info(String.valueOf(authenticationToken));
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        log.info(String.valueOf(authentication));
        return tokenProvider.generateTokenDto(authentication);

    }

    public String assemblePw(String email) {
        return String.format("%s%s", "KAKAO", email);
    }

    public UserEntity signUp(String email, String password, String nickname) {
        //유저 생성
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setNickname(nickname);
        userEntity.setGold(50000);
        userEntity.setStarCoin(20);
        userEntity.setAuthority("USER");
        userEntity.setExp(0);
        userEntity.setLevel(1);

        // 가방 생성
        BagEntity bagEntity = new BagEntity();
        ItemEntity itemEntity = itemRepository.findByItemCode("egg_001")
                .orElseThrow(() -> new EntityNotFoundException("기본 알 아이템(egg_001)을 찾을 수 없습니다."));
        //TODO:: 디버그 용 예외 던질 필요있음

        // egg 도감 등록
        BookEntity bookEntity = new BookEntity();
        bookEntity.setUserEntity(userEntity);
        bookEntity.setItemEntity(itemEntity);

        BirdEntity birdEntity = new BirdEntity();
        birdEntity.setName("이쁜 새 ");
        birdEntity.setUser(userEntity);
        birdEntity.setCreatedAt(new Date());
        birdEntity.setLastFedAt(new Date());
        birdEntity.setLastThirstAt(new Date());
        bagEntity.setUser(userEntity);
        bagEntity.setItem(itemEntity);
        bagEntity.setAmount(1);
        userRepository.save(userEntity);
        bagRepository.save(bagEntity);
        birdRepository.save(birdEntity);
        return userEntity;
    }

    public String getAccessTokenFromAuthCode(String authCode){
        final String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", authCode);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<KakaoOauthTokenDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    KakaoOauthTokenDto.class
            );
            String accessToken = response.getBody().getAccessToken();
            return accessToken;
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "카카오에서 토큰으로 코드를 교환하지 못함 : " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "카카오 토큰 요청 중 예기치 않은 오류 발생", e);
        }
    }

    @Override
    public KakaoMeDto.KakaoMe getKakaoUserInfo(String accessToken) {
        final String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Authorization: Bearer {token}

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoMeDto.KakaoMe> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    KakaoMeDto.KakaoMe.class
            );
            return Objects.requireNonNull(response.getBody(), "empty user/me response");
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "카카오에서 사용자 정보를 가져오지 못함 : " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "카카오 사용자 정보 요청 중 예기치 않은 오류 발생", e);
        }
    }

    @Override
    public KakaoUserMinimalResponseDto fetchKakaoMinimal(String accessToken) {
        KakaoMeDto.KakaoMe me = getKakaoUserInfo(accessToken);

        String email = null;
        Boolean emailVerified = false;
        String profileImageUrl = null;

        KakaoMeDto.KakaoAccount acc = (me != null ? me.kakao_account() : null);
        if (acc != null) {

            if (Boolean.TRUE.equals(acc.is_email_valid())
                    && Boolean.TRUE.equals(acc.is_email_verified())
                    && acc.email() != null) {
                email = acc.email();
                emailVerified = true;
            }

            KakaoMeDto.KakaoProfile profile = acc.profile();
            if (profile != null && !Boolean.TRUE.equals(profile.is_default_image())) {
                profileImageUrl = profile.profile_image_url();
            }
        }

        return KakaoUserMinimalResponseDto.builder()
                .kakaoUserId(me != null ? me.id() : null)
                .email(email)
                .emailVerified(emailVerified)
                .profileImageUrl(profileImageUrl)
                .build();
    }


    public TokenDto loginUser(LoginRequestDto loginRequestDto) {
        UserEntity userEntity = userRepository.findByEmail(loginRequestDto.getEmail());
        System.out.println(loginRequestDto.getEmail() + loginRequestDto.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        System.out.println(authenticationToken);
        Authentication authentication
                = managerBuilder.getObject().authenticate(authenticationToken);
        System.out.println(authentication);
        return tokenProvider.generateTokenDto(authentication);
    }

    public MyPageResponseDto myPage() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);

        UserEntity userEntity = userRepository.findByEmail(email); // userRepository에서 findByEmail함수를 통해서 (String)email를 userEntity에 저장하기.
        System.out.println(userEntity); // userEntity 출력

        MyPageResponseDto myPageResponseDto = new MyPageResponseDto(); // 객체 생성

        System.out.println(userEntity.getGold()); // userEntity에서 돈을 가져와서 출력
        System.out.println(userEntity.getNickname()); // userEntity에서 닉네임을 가져와서 출력

        UserExperienceLevel level = UserExperienceLevel.findByExp(userEntity.getExp());

        myPageResponseDto.setGold(userEntity.getGold());
        myPageResponseDto.setNickname(userEntity.getNickname());
        myPageResponseDto.setUserExp(userEntity.getExp());
        myPageResponseDto.setStarCoin(userEntity.getStarCoin());
        myPageResponseDto.setUserLevel(level.getLevel());
        myPageResponseDto.setMaxExp(level.getMaxExp());
        myPageResponseDto.setMinExp(level.getMinExp());

        Optional<BirdEntity> bird = birdRepository.findByUserEmail(email);
        if (bird.isPresent()){
            BirdEntity birdEntity = bird.get();
            Date lastFedAt = birdEntity.getLastFedAt();
            Date lastThirstAt = birdEntity.getLastThirstAt();

            int lastHungry =  birdEntity.getHungry();
            int lastThirst = birdEntity.getThirst();

            Instant now = Instant.now();
            Instant lastFedInstant = lastFedAt.toInstant();
            Instant lastThirstInstant = lastThirstAt.toInstant();

            long minutesFedElapsed = Duration.between(lastFedInstant, now).toMinutes();
            long minutesThirstElapsed = Duration.between(lastThirstInstant, now).toMinutes();
            long decreaseFedAmount = minutesFedElapsed; // 30나누기 추가하기..
            long decreaseThirstAmount = minutesThirstElapsed; // 30나누기 추가하기..
            int birdHungry = Math.max(0, lastHungry - (int) decreaseFedAmount);
            int birdThirst = Math.max(0, lastThirst - (int) decreaseThirstAmount);
            log.info(String.valueOf(decreaseFedAmount));
            log.info(String.valueOf(birdHungry));
            myPageResponseDto.setBirdHungry(birdHungry);
            myPageResponseDto.setBirdThirst(birdThirst);
        }else{
            throw new RuntimeException("새를 보유하고 있지 않습니다.");
        }
        //TODO:여기서 레벨 별 경험치 총량 불러와서 myPageResponseDto에 삽입, Level별 경험치 총량은 enum형태로 관리하거나, Static변수를 모아놓은 Public 클래스로 관리하는게 좋을듯.
        return myPageResponseDto;
    }

    @Transactional
    public boolean deleteUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userRepository.existsByEmail(email)) {
            bagRepository.deleteByUserEmail(email);
            userRepository.deleteByEmail(email);
            return true;
        }else{
            throw new EntityNotFoundException("email에 해당하는 유저가 존재하지않거나 이미 제거되었습니다.");
        }
    }


    public String updateUserInfo(UpdateUserInfoRequestDto updateUserInfoRequestDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setGold(updateUserInfoRequestDto.getGold());
        userEntity.setNickname(updateUserInfoRequestDto.getNickname());
        userRepository.save(userEntity);
        return "성공";
    }

    public Integer getGoldByEmail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findGoldByEmail(email);
    }

    public String getNicknameByEmail() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).getNickname();
    }
}
