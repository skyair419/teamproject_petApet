# teamproject_petApet
team project - pet site (20220927~)

v 0. 1. 1 2023/02/07

- Product 관련 html <hidden> 태그 제거 및 js 추출
- ProductController 클래스 updateProduct() 리팩터링
- CorsConfig 클래스 수정 (https 장애 대응)\
  addAllowedOrigin(“\*”) -> addAllowedOriginPattern(“*”)
- Product 장바구니 담기, 바로구매 수정
- 상품 등록 시 이미지 파일 검증 추가 및 사이즈 제한
  src/main/resources/application.properties
  
v 0. 2. 0 2023/02/13

- ExceptionLog 클래스 추가

- 모든 장바구니 추가, 바로 구매 버튼 수정
  로그인 상태 및 role 검증, 상황별 예외 처리

- 장바구니에 이미 같은 상품이 있는지 검증
  존재하면 새로 추가 안되고 수정 되게 변경

- 장바구니 화면, 체크박스에 따른 총 가격 및 수량 변경 되도록 수정

- 장바구니에서 총 주문 시 결제 구현

- 장바구니 -> 결제 플로우 view, js 하나로 통합

- 장바구니 전체 삭제 수정

- 쿠폰 적용하기 기능 구현 (정가 할인만 가능, 백분율 할인은 보류)

- 주문 시 비즈니스에 영향이 있는 가격, 쿠폰 등을 검증하기 위한 로직 다수 추가

- Buy와 Product 사이에 BuyProduct 엔티티 추가

- 구매 목록 페이지, mail.html 수정

- 찜하기 기능 수정 및 찜 목록 페이지 구현

** Buy에 있는 Product와 quantity는 더 이상 쓰면 안 됩니다.
    기존에 구현된 기능은 정상 작동하게 바꿨습니다.

v 0. 2. 1 2023/02/14
- 신고와 관련된 DTO에서 발생하는 InvalidDefinitionException 처리
- 사업자 승인, 신고 승인 관련 IllegalStateException 처리
- 신고 승인 관련 Controller에서 예외 처리 (현재 커뮤니티 신고 승인 컨트롤러에만 구현)