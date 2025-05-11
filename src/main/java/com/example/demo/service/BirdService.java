package com.example.demo.service;

import com.example.demo.model.BagEntity;
import com.example.demo.repository.BagRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BirdService {
    // 알 추가하는 로직 ..
    // ShopService의 buyItem처럼..
    final private UserRepository userRepository;
    final private BagRepository bagRepository;

    public BirdService(UserRepository userRepository, BagRepository bagRepository) {
        this.userRepository = userRepository;
        this.bagRepository = bagRepository;
    }

//    // 미완성 코드 ..
//    public boolean addEgg(String itemCode, String birdCode, int amount) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        UserEntity userEntity = userRepository.findByEmail(email);
//
//
//        Optional<BagEntity> bagEntity = bagRepository.findByUserEmailAndItemCode(email,itemCode);
//        if(bagEntity.isPresent()) {
//            BagEntity bag = bagEntity.get();
//            bagRepository.save(bag);
//        }
//        userRepository.save(userEntity);
//        return true;
//    }
    public boolean birdGiveFood(String itemCode, String amount) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //현재 로그인 한 사용자 이메일 가져오기
        // user 가방 안에 있는 아이템 가져오기 (user 가방 가져오기)
        Optional<BagEntity> bagEntity = bagRepository.findByUserEmailAndItemCode(email,itemCode);
        if(bagEntity.isPresent()) {
            BagEntity bag = bagEntity.get();
            if (bag.getAmount() - 1 < 0){
                deleteUserBagItem(bag);
            }else{
                //TODO :: 다음주까지
                //1. 여기서 이제 개수 줄여주고
                //2. DB에 다시 저장하는 로직
                //3. user가 갖고있는 bird Entity 불러오기
                //4. bird의 EXP라던가 기타 수정사항들 수정하고...
                //5. 다시저장 .. ㅋㅋ
            
            }
        }
    }
    @Transactional
    public void deleteUserBagItem(BagEntity bagEntity){
        //여기 bagEntity를 delete하는 로직
        bagRepository.delete(bagEntity);
    }
}
