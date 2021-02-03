# MyCarrot ( Dang-geun Market Clone App )
당근마켓 클론 앱 만들기 프로젝트 입니다.    



## Description
* 기간: 2020. 09. 01 ~ 2021. 02. 02
* 안드로이드 아키텍처 구조: MVVM   
![mvvm](https://user-images.githubusercontent.com/49981048/106762187-f0621300-6678-11eb-81f0-20f934f71733.png)   
   
      
      
* 사용기술
   * Language: Android(Kotlin)
   * Library: AAC ( Room,  Databinding,  ViewModel,  LiveData ),  RxJava2,  Retrofit2,  ViewPager2,  Lottie,  PlacePicker,  Glide,  Groupie 등
   * Service: Firebase Cloud Storage,  Cloud Firestore,  Firebase Realtime Database,  FCM,  Firebase Authentication   
   
   
   
## Design
* Wire Frame: 앱 UI 분석 및 Flow Chart 구체화 
   
![wireFrame](https://user-images.githubusercontent.com/49981048/106765320-032a1700-667c-11eb-83ff-256e01f592e2.png)   
   
    
    

 * 데이터베이스 구조 (Firebase Auth UID 및 Firestore Collection Reference 기반 String 타입 id 사용)
    

![ezgif com-gif-maker](https://user-images.githubusercontent.com/49981048/106774757-ccf19500-6685-11eb-9670-f529ebcb89ae.png)

   
    
* Commit Message
   * Feat    : 기능 (새로운 기능)
   * Fix     : 버그 (버그 수정)
   * Refactor: 리팩토링
   * Style   : 스타일 (코드 형식, 세미콜론 추가: 비즈니스 로직에 변경 없음)
   * Docs    : 문서 (문서 추가, 수정, 삭제)
   * Test    : 테스트 (테스트 코드 추가, 수정, 삭제: 비즈니스 로직에 변경 없음)
   * Chore   : 기타 변경사항 (빌드 스크립트 수정 등)
   
## Implementation
#### Login
* 동네 설정 및 구글 로그인 기능   
![login](https://user-images.githubusercontent.com/49981048/106771224-15a74f00-6682-11eb-9dc8-089c74cab861.gif)   
   
   
#### Home
* 동네 범위 설정 및 카테고리 선택 기능  
![home](https://user-images.githubusercontent.com/49981048/106770552-72563a00-6681-11eb-9f8d-0b39e62c3560.gif)   

#### Search
* 아이템 및 사용자 검색 기능  
![search](https://user-images.githubusercontent.com/49981048/106771454-4e472880-6682-11eb-9e10-cdaef8e8a83a.gif)   

#### Write
* 상품 올리기 기능  
![write](https://user-images.githubusercontent.com/49981048/106771575-7171d800-6682-11eb-9b11-e6bdbeb6eea8.gif)   

#### Chat
* 사용자와 채팅 기능  
![chat](https://user-images.githubusercontent.com/49981048/106772254-2b694400-6683-11eb-896f-834cb42be73e.gif)   


#### MyCarrot
* 프로필 변경, 동네설정, 동네인증, 모아보기, 당근마켓 공유, 공지사항, 자주 묻는 질문 기능   
![my1](https://user-images.githubusercontent.com/49981048/106772429-581d5b80-6683-11eb-9701-ca725719409a.gif)  |  ![my2](https://user-images.githubusercontent.com/49981048/106772452-5fdd0000-6683-11eb-9a87-3e449c5b499c.gif)
   


#### Item
* 아이템 보기, 판매자 프로필 보기, 추천상품 보기, 채팅으로 거래하기, 좋아요 기능   
![item](https://user-images.githubusercontent.com/49981048/106772754-ac284000-6683-11eb-956b-c8c6f3f37307.gif)   


#### Profile
* 모아보기, 판매상품 보기, 매너평가 보기 기능   
![profile](https://user-images.githubusercontent.com/49981048/106772893-ce21c280-6683-11eb-95fe-a8887c1f3b74.gif)  |  ![buyComplete](https://user-images.githubusercontent.com/49981048/106772866-c7934b00-6683-11eb-8fd8-201ac1fd3650.gif)
   

