package com.example.demo.service;

import com.example.demo.Dto.LoginRequestDto;
import com.example.demo.Dto.MyPageResponseDto;
import com.example.demo.Dto.TokenDto;
import com.example.demo.model.BagEntity;
import com.example.demo.model.ItemEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.BagRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;
    private final BagRepository bagRepository;

    public UserService(AuthenticationManagerBuilder managerBuilder, TokenProvider tokenProvider, UserRepository userRepository, PasswordEncoder passwordEncoder, ItemRepository itemRepository, BagRepository bagRepository) {
        this.managerBuilder = managerBuilder;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.itemRepository = itemRepository;
        this.bagRepository = bagRepository;
    }

    //함수로 분리...하는편이 좋습니다.

    public UserEntity saveUser(String email, String password, String nickname) {

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

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setNickname(nickname);
        userEntity.setMoney(10000);
        userEntity.setAuthority("USER");
        BagEntity bagEntity = new BagEntity();
        ItemEntity itemEntity = itemRepository.findByItemCode("egg_001").orElseThrow();
        bagEntity.setUser(userEntity);
        bagEntity.setItem(itemEntity);
        bagEntity.setAmount(1);
        userRepository.save(userEntity);
        bagRepository.save(bagEntity);

        //여기서 BagEntity를 만들고.... 그 안에, 알과 .. 초반아이템들을 조금 넣어줘야한다...
        return userEntity;
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

    public MyPageResponseDto myPage () {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(email);

        UserEntity userEntity = userRepository.findByEmail(email); // userRepository에서 findByEmail함수를 통해서 (String)email를 userEntity에 저장하기.
        System.out.println(userEntity); // userEntity 출력

        MyPageResponseDto myPageResponseDto = new MyPageResponseDto(); // 객체 생성

        System.out.println(userEntity.getMoney()); // userEntity에서 돈을 가져와서 출력
        System.out.println(userEntity.getNickname()); // userEntity에서 닉네임을 가져와서 출력

        myPageResponseDto.setMoney(userEntity.getMoney());
        myPageResponseDto.setNickname(userEntity.getNickname());

        return myPageResponseDto;
    }

}
