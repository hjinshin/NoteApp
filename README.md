<h1>MATHINK</h1>

이공계열 학생을 위하여 OCR 기능을 활용한 안드로이드 기반의 수식 특화형 노트 필기앱

### 목적
* OCR을 통해 노트앱 이용 중 편리하게 수식 작성
* 노트앱 내에서 공학계산기 기능 사용
* 노트앱 내에서 자동 그래프 그리기

### 일정
* 23.11.24 ~ 23.12.16

### 인원
총 2인
* Android: 권성찬
* BE: 신형진

### 기능
[세부 기능 보기](DETAILFUNCTION.md)
</br>
</br>

## 개발 환경
### Android
* IDE: Android Studio 2033.3.1 
* JDK: JBR 17
* 최소 요구사항: 안드로이드 SDK 26 (Android 8.0 Oreo) 이상

### Backend
* IDE: IntelliJ IDEA
* Springboot: 2.7.4
* Java: 8
* MySQL: 8.0.x

</br>
</br>

## 기술 스택
### Android
* Kotlin
* JavaScript / Web view

### Backend
* Springboot
* Spring Data JPA
* Azure Database for MySQL
* Azure Cosmos DB for MongoDB

</br>
<img src="readme_img/stack.png" />


</br>
</br>

## Flow Chart
</br>
</br>
<img src="readme_img/flowchart.png" />

</br>
</br>

## 시스템 아키텍처
</br>

### Android 아키텍처
</br>
<img src="readme_img/androidarchitect.png" />
</br>
</br>
</br>

### Backend 아키텍처
</br>

<img src="readme_img/backendarchitect.png" />

## API
</br>

### OCR

| 종류 | Endpoint | Description | Header | Request payload |
|------|---------|-------------------------| ---------|------|
| OCR API | POST</br> /api/mathpix | 이미지->LaTeX OCR 요청  | | - access_token: String</br> - img: String |

</br></br></br>

### AUTH

| 종류 | Endpoint | Description | Header | Request payload |
|------|---------|-------------------------| ---------|------|
| AUTH API | GET</br> /api/auth | access_token 인증 요청  | | - access_token: String|
| | GET</br> /api/revoke | access_token 취소 요청</br>(refresh_token 재발급)  | | - access_token: String|
| Login API | GET</br> /login | 로그인 요청(access_token 발급)  | | - code: String|

</br></br></br>

### NOTEAPI

| 종류 | Endpoint | Description | Header | Request payload |
|------|---------|-------------------------| ---------|------|
| NOTE API | GET</br> /api/database/namelist | 사용자의 노트 제목 리스트 요청  | | - access_token: String|
|| GET</br> /api/database | 사용자의 모든 노트페이지 요청  | | - access_token: String|
|| POST</br> /api/database | 사용자의 모든 노트페이지 업데이트  | | - access_token: String</br> - new_pages: List&lt;NewPage&gt; </br> - NewPage:{ </br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  - name: String, </br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - data: Map&lt;String, Object&gt; </br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;} |
|| DELETE</br> /api/database | 사용자의 모든 노트페이지 삭제  | | - access_token: String|
|| GET</br> /api/database/page | 특정 노트페이지 요청  | | - access_token: String</br> - page_name: String|
|| POST</br> /api/database/page | 특정 노트페이지 업데이트  | | - access_token: String</br> - old_page_name: String </br> - new_page: NewPage </br> - NewPage:{ </br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  - name: String, </br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - data: Map&lt;String, Object&gt; </br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}  |
|| DELETE</br> /api/database/page | 특정 노트페이지 삭제  | | - access_token: String</br> - page_name: String|
