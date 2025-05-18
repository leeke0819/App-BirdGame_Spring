package com.example.demo.service;

import com.example.demo.Dto.response.BirdFeedResponseDto;
import com.example.demo.Dto.response.MyPageResponseDto;
import com.example.demo.model.BagEntity;
import com.example.demo.model.BirdEntity;
import com.example.demo.model.ItemEntity;
import com.example.demo.repository.BagRepository;
import com.example.demo.repository.BirdRepository;
import com.example.demo.repository.ItemRepository;
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
    final private BirdRepository birdRepository;
    final private ItemRepository itemRepository;

    public BirdService(UserRepository userRepository, BagRepository bagRepository, BirdRepository birdRepository, ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.bagRepository = bagRepository;
        this.birdRepository = birdRepository;
        this.itemRepository = itemRepository;
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
    public MyPageResponseDto birdGiveFood(String itemCode) throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //현재 로그인 한 사용자 이메일 가져오기
        // user 가방 안에 있는 아이템 가져오기 (user 가방 가져오기)
        Optional<BagEntity> bagEntity = bagRepository.findByUserEmailAndItemCode(email,itemCode);
        if(bagEntity.isPresent()) {
            MyPageResponseDto myPageResponseDto = new MyPageResponseDto();
            BagEntity bag = bagEntity.get();
            if (bag.getAmount() - 1 < 0){
                System.out.println("새에게 줄 아이템이 부족합니다.");
                deleteUserBagItem(bag);
            } else{
                int updateAmount = bag.getAmount() - 1;
                System.out.println("새에게 먹이를 줬습니다!");
                bag.setAmount(updateAmount);

                if (updateAmount <= 0) {
                    bagRepository.delete(bag);
                } else {
                    bagRepository.save(bag);
                }

                Optional<BirdEntity> birdObject = birdRepository.findByUserEmail(email);
                if(birdObject.isPresent()) {
                    BirdFeedResponseDto birdFeedResponseDto = new BirdFeedResponseDto();
                    ItemEntity itemEntity = itemRepository.findByItemCode(itemCode).orElseThrow();
                    BirdEntity bird = birdObject.get();
                    if(itemEntity.getItemType() == 1){
                        bird.setHungry(bird.getHungry() + itemEntity.getFeed());
                    }else if(itemEntity.getItemType() == 2){
                        bird.setHungry(bird.getThirst() + itemEntity.getFeed());
                    }else{
                        throw new Exception("먹일 수 없는 아이템 입니다.");
                    }
                    //저장
                    birdRepository.save(bird);
                }

                //TODO :: 다음주까지
                //1. 여기서 이제 개수 줄여주고
                //2. DB에 다시 저장하는 로직
                //3. user가 갖고있는 bird Entity 불러오기
                //4. bird의 EXP라던가 기타 수정사항들 수정하고...
                //5. 다시저장 .. ㅋㅋ
            
            }
        }
        return null;
    }
    @Transactional
    public void deleteUserBagItem(BagEntity bagEntity){
        //여기 bagEntity를 delete하는 로직
        bagRepository.delete(bagEntity);
    }
}
