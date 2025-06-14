package com.example.demo.Dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class MyPageResponseDto {
    // user 관련
    String nickname;
    Integer userLevel;
    Integer userExp;
    Integer minExp;
    Integer maxExp;
    Integer gold;
    Integer starCoin;
    //String userProfile;

    // bird 관련
    String birdName;
    Integer birdLevel;
    Integer birdExp;
    String status;

    int BirdHungry;
    int BirdThirst;
    // 다른 거는.. 없음 ..




    //여기에, 이제 메인페이지가 로드 될 때, 보여주고싶은 새 혹은 알의 상태가 같이 나가야 한다.
    //그래서 fe에서 처리를 할 수 있겠죠.?
    //어쨋든 메인화면이 로드될 때 서버에서 내려줘야하는 값에 대해서 다 정의해두세요.

}
