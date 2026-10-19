Petclinic 3-Tier 배포 정리

WEB VM
- Apache Reverse Proxy 설정만 배치합니다.
- 애플리케이션 소스와 이미지는 WEB에 복사하지 않습니다.

WAS VM
- was/app 폴더 전체를 WAS에서 빌드합니다.
- ./mvnw clean package -PMySQL -DskipTests
- target/*.war를 Tomcat webapps/petclinic.war로 복사합니다.

DB
- db/mysql/schema.sql: 테이블 생성
- db/mysql/data.sql: 초기 데이터
- Azure MySQL에 petclinic 데이터베이스를 만든 후 사용합니다.

주의
- pom.xml의 MySQL 프로필에서 Azure MySQL FQDN, 사용자명, 비밀번호를 수정해야 합니다.
- src/main/resources/db/mysql 파일은 WAS WAR에도 포함되므로 DB 폴더는 참고/수동 실행용입니다.

미용 예약 기능
- WEB: web/index.html에서 보호자/반려동물 정보, 메뉴, 희망 날짜와 시간을 입력합니다.
- WAS: POST /api/grooming/bookings 로 예약을 저장하고 GET /api/grooming/bookings 로 최근 예약 50건을 조회합니다.
- DB: grooming_bookings 테이블에 예약을 저장합니다. WAS 시작 시 resources/db/mysql/schema.sql의 CREATE TABLE IF NOT EXISTS가 적용됩니다.
- 수동 DB 구성 시 db/mysql/schema.sql을 실행하면 새 테이블도 생성됩니다.
- 기존 MySQL 접속 정보([Change Me], 계정, 비밀번호) 설정은 여전히 필요합니다.
