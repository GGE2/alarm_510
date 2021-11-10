## 다양한 학습 문제들을 제공해주는 알람앱(Android with JAVA)
### 오일공(오늘도 일어나서 공부하자)
------------------------------------
### 프로젝트 설계
> 대부분 학생들은 평일은 대부분 같은 시간에 알람을 맞춰놓는다  
> 알람이 울릴때 그냥 일어나는 것이 아닌 전날 자신이 설정해 놓은 카테고리별 문제들을 풀수 있다면?  
> 처음에는 풀지 못할수 있지만 비슷한 문제들을 같은 시간에 주기적으로 본다면?  
> 1분씩을 투자해 문제 1문제를 풀 수 있게 된다면?  
> 틀린 문제들은 오답노트로 따로 관리해준다면?  
> 학생들에게 도움을 줄 수 있는 앱을 만들어보자!
------------------------------------
### DB 및 서버
Web Server : AWS EC2 (Node.js)  
사용자 DB : Firbase Realtimebase(NoSQL)  
문제 DB : MySQL  

### 로그인 인증
일반로그인 : Firebase Authenfication  
카카오계정 로그인 : Kakao Access Token -> Firebase  Auth Token 으로 변환 후 인증  

--------------------------------------------------------------
### 기능
------------------------------------

+ #### [로그인] 
< 이전 로그인 후 로그아웃 버튼 클릭없이 앱 종료시 토큰값을 그대로 저장하고 있어 자동으로 메인 페이지로 이동>  
< 앱 처음 실행 or 로그아웃 버튼 클릭시 로그인 페이지로 이동>
1. 회원가입을 통한 일반로그인 지원  
<img src="https://user-images.githubusercontent.com/68943993/141063292-22406498-332f-40ba-a779-1b7472bf2374.png" width="220" height="400">      <img src="https://user-images.githubusercontent.com/68943993/141063508-2cd756bf-b490-4544-ad33-24174ba4652e.png" width="220" height="400">      <img src="https://user-images.githubusercontent.com/68943993/141063582-d98ef56c-4e99-45da-9d4d-2a6c783dabac.png" width="220" height="400">
2. 카카오계정 연동을 통한 카카오계정 로그인 지원
3. 아이디찾기 및 비밀번호 재설정 지원  
<img src="https://user-images.githubusercontent.com/68943993/141065459-6c59ad14-db05-4f14-9fb6-f34f102a8ceb.PNG" width="250" height="400">    <img src="https://user-images.githubusercontent.com/68943993/141065450-d25660ca-725f-4518-9f14-e412d4828653.PNG" width="250" height="400">

+ #### [알람설정]
1. 문제 분야 (영단어 , 속담 , 사자성어 , 수도 , 역사 , 통합) 설정
2. 문제 개수 설정
3. 알람 시간 설정
4. 알람 메모 설정  
<img src="https://user-images.githubusercontent.com/68943993/141069603-119f7178-3bf0-4c0c-9927-e603cd1e57c5.PNG" width="200" height="400">      <img src="https://user-images.githubusercontent.com/68943993/141069617-b9e8940f-f375-43e5-a84b-3d797b238723.PNG" width="200" height="400">

+ #### [오답노트]
1. 틀린 문제들은 오답노트로 이동
2. 문제 카테고리별 분류 가능
3. 문제 탭 클릭시 문제 키워드 알림  
<img src="https://user-images.githubusercontent.com/68943993/141070441-0eea2ad0-3e0c-4573-b68c-1dbfd9164931.PNG" width="200" height="400">    <img src="https://user-images.githubusercontent.com/68943993/141070877-c467ec97-ec86-40f2-8983-e3b3bdfccb5f.PNG" width="200" height="400">

+ #### [학습하기]
1. 문제 분야 (영단어 , 속담 , 사자성어 , 수도 , 역사 , 통합) 중 원하는 카테고리 문제 풀기
2. 틀린 문제들은 오답노트로 이동  
<img src="https://user-images.githubusercontent.com/68943993/141070259-7678050d-ed7f-4048-80eb-36cb0a1be3af.PNG" width="200" height="400">    <img src="https://user-images.githubusercontent.com/68943993/141070358-bf97098e-97fb-46a4-a3e7-de73cd24d861.PNG" width="200" height="400">   <img src="https://user-images.githubusercontent.com/68943993/141070381-65d621f0-f553-4569-a8c2-36f8b2a5ba4f.PNG" width="200" height="400">

+ #### [설정]
1. 로그아웃 ( 회원가입 유저 , 카카오 로그인 유저 토큰을 따로 구분)
<img src="https://user-images.githubusercontent.com/68943993/141071971-0a099c2f-bab4-4dff-ac4d-2ade8d651bd4.jpg" width="200" height="400">
