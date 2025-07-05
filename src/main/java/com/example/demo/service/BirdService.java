package com.example.demo.service;

import com.example.demo.Dto.response.BirdFeedResponseDto;
import com.example.demo.model.BagEntity;
import com.example.demo.model.BirdEntity;
import com.example.demo.model.ItemEntity;
import com.example.demo.repository.BagRepository;
import com.example.demo.repository.BirdRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
@Slf4j
@Service
public class BirdService {
    // 알 추가하는 로직 ..
    // ShopService의 buyItem처럼..
    final private UserRepository userRepository;
    final private BagRepository bagRepository;
    final private BirdRepository birdRepository;
    final private ItemRepository itemRepository;
    private static int BIRD_MAX_HUNGER = 100;
    private static int BIRD_MAX_THIRST = 100;

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

    public BirdFeedResponseDto birdGiveFood(String itemCode, int amount) throws Exception {
        String email = SecurityContextHolder.getContext().getAuthentication().getName(); //현재 로그인 한 사용자 이메일 가져오기

        // 새 상태 갱신
        updateBirdState(email);

        // user 가방 안에 있는 아이템 가져오기 (user 가방 가져오기)
        Optional<BagEntity> bagEntity = bagRepository.findByUserEmailAndItemCode(email,itemCode);
        ItemEntity itemEntity = itemRepository.findByItemCode(itemCode).orElseThrow();
        System.out.println(bagEntity);
        System.out.println(email);
        System.out.println(itemCode);
        log.debug("amount{}", amount);

        if(bagEntity.isEmpty()) {
            throw new RuntimeException("가방에 아이템이 충분하지 않습니다.");
        }
        Optional<BirdEntity> birdObject = birdRepository.findByUserEmail(email);
        if(birdObject.isEmpty()){
            throw new RuntimeException("해당하는 새를 찾을 수 없습니다.");
        }
        BagEntity bag = bagEntity.get();
        if (bag.getAmount() - amount < 0){ //수량 충분 검사
            log.debug(String.valueOf(bag.getAmount()));
            throw new RuntimeException("새에게 줄 아이템이 부족합니다.");
        }
        if(itemEntity.getItemType() == 100) {
            throw new RuntimeException("먹일 수 없는 아이템입니다.");
        }

        BirdFeedResponseDto birdFeedResponseDto = new BirdFeedResponseDto();
        BirdEntity bird = birdObject.get();

        if(!(itemEntity.getItemType() == 1 || itemEntity.getItemType() == 2)){
            throw new RuntimeException("먹일 수 없는 아이템 입니다.");
        }
        log.info(String.valueOf(bird.getHungry()));
        if (bird.getHungry() >= BIRD_MAX_HUNGER && itemEntity.getFeed() > 0) {
            throw new RuntimeException("새가 배불러서 먹지 못합니다.");
        }
        if(bird.getThirst() >= BIRD_MAX_THIRST && itemEntity.getThirst() > 0) {
            throw new RuntimeException("새가 더는 목마르지 않습니다.");
        }

        int updateAmount = bag.getAmount() - amount;
        bag.setAmount(bag.getAmount() - amount);
        if (updateAmount <= 0) {
            deleteUserBagItem(bag);
        } else {
            bag.setAmount(updateAmount);
            bagRepository.save(bag);
        }

        int birdHunger = bird.getHungry() + (itemEntity.getFeed() * amount);
        int birdThirst = bird.getThirst() + (itemEntity.getThirst() * amount);

        // 최대값 제한 (0-BIRD_MAX_* 범위)
        birdHunger = Math.min(BIRD_MAX_HUNGER, Math.max(0, birdHunger));
        birdThirst = Math.min(BIRD_MAX_THIRST, Math.max(0, birdThirst));

        birdFeedResponseDto.setBirdHungry(birdHunger);
        birdFeedResponseDto.setBirdThirst(birdThirst);

        bird.setHungry(birdHunger);
        bird.setThirst(birdThirst);
        birdRepository.save(bird);
        return birdFeedResponseDto;
    }

    @Transactional
    public void deleteUserBagItem(BagEntity bagEntity){
        //여기 bagEntity를 delete하는 로직
        bagRepository.delete(bagEntity);
    }

    public BirdEntity updateBirdState(String email) {
        Optional<BirdEntity> birdObject = birdRepository.findByUserEmail(email);
        if(birdObject.isEmpty()) {
            throw new RuntimeException("새를 찾을 수 없습니다.");
        }
        BirdEntity bird = birdObject.get();
        Date birdCreatedAt = bird.getCreatedAt();

        Date now = new Date();

        // 새가 생성된 이후 총합 시간 (몇 시간이 지났는지) -> 현재 시간 - 생성된 시간
        long totalTimeAfterbirdCreate = now.getTime() - birdCreatedAt.getTime();

        // 경과 시간 (몇 분 지났는지)
        long elapsedTime = totalTimeAfterbirdCreate / (1000 * 60);

        // 몇 분마다 바꿀 건지 -> 현재는 15분
        int change = (int)(elapsedTime / 15);

        // 목마름, 배고픔 최솟값 0으로 설정 -> 0 이하로는 안줄어들게끔.
        int newHungry = Math.max(bird.getHungry() - change, 0);
        int newThirst = Math.max(bird.getThirst() - change, 0);

        bird.setHungry(newHungry);
        bird.setThirst(newThirst);

        birdRepository.save(bird);

        return bird;
    }
}
