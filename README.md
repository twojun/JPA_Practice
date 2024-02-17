[JPA를 공부하며 정리한 Repository입니다. 잘못 작성된 부분이 있다면 별도로 수정하겠으며 학습했던 내용에 대해 새롭게 알게 된 부분에 대해서는 추가 커밋 예정입니다.] </br></br>

# 1. 엔티티 설계, 주의사항 </br> 
## 1-1. 외래 키를 보유한 곳을 연관관계의 주인으로 설정한다.</br>
(1) Reference : https://github.com/twojun/ex1-hello-jpa</br></br></br></br></br>




##  1-2. 엔티티 클래스는 가급적이면 Setter를 열지 않는다.</br>
(1) Getter는 열지만, Setter는 필요한 상황이 아니라면 가급적 열지 않는다.</br></br>

(2) 실무에서 데이터 조회는 매우 빈번한 일이므로 Getter의 경우 모두 열어두는 편이 편리하다.</br>
(단순 getter 조회만으로 큰 문제가 발생하진 않는다.</br></br>

(3) 하지만 setter는 데이터 변경을 발생시키므로 추후 미래에 엔티티에 대한 데이터가 수정된 경로를 추적하기 매우 어려워진다.(유지보수가 어려워짐)</br></br>

(4) 따라서 엔티티 변경 시 setter 대신에  변경 지점이 명확하도록 변경을 위한 비즈니스 메서드를 별도로 제공하는 것이 좋다.</br></br></br></br></br>




##  1-3. 실무에서 다대다 연관관계(N:M), @ManyToMany를 사용하지 않는다.</br>
(1) 따라서 중간 엔티티를 만들고 @ManyToOne, @OneToMany로 매핑해서 사용한다.(다대다 매핑을 일대다, 다대일 매핑으로 풀어낸다.)</br></br></br></br></br>



## 1-4. 값 타입은 변경 불가능하게 설계해야 한다.</br>
(1) @Setter를 제거하고 생성자에서 모두 값을 초기화하도록 하여 변경 불가능한 클래스로 설계한다. JPA 스펙상, 엔티티나 임베디드 타입은 자바 기본 생성자의
접근 제어자는 public or protected로 설정한다. </br></br>

(2) JPA가 이러한 제약을 설정한 이유는 JPA 구현 라이브러리가 객체 생성 시 리플렉션과 같은 기술을 사용할 수 있도록 지원해야 하기 때문</br></br></br></br></br>



##  1-5. 모든 연관관계는 지연 로딩(Lazy Loading)으로 설정한다.</br>
(1) 즉시 로딩은 어떠한 SQL이 실행될지 추적하기 어렵다. em.find()와 같은 단순 단건 조회가 아닌 JPQL을 실행하는 상황에서 N+1 문제를 발생시킨다.</br></br>

(2) 실무에서 모든 연관관계는 LAZY로 잡는다.</br></br>

(3) 연관 엔티티를 함께 조회해야 한다면 Fetch join, Entity graph 기능을 사용하여 최적화한다.
- fetch join : 특정 context에서 필요한 부분들을 별도로 가져오는 방법</br></br>

(4) @XToOne 어노테이션의 기본 로딩 전략은 EAGER이므로 직접 LAZY로 설정해야 한다.
- @XToMany 어노테이션은 기본 전략이 LAZY로 설정되어 있다. </br></br></br></br></br>




##  1-6. 컬렉션은 필드에서 초기화한다.</br>
(1) Best practice의 경우, 컬렉션은 필드에서 바로 초기화하는 것이 null 문제 등을 고려해 봤을 때 가장 안전하다.</br></br>

(2) Hibernate의 경우 엔티티 영속화 시, 컬렉션을 hibernate가 제공하는 내부 컬렉션으로 변경하게 된다.</br>
- 하이버네이트가 관리하는 별도의 컬렉션으로 변경되었기 때문에 사용자가 임의의 메서드를 통해 접근하거나 값을 수정, 컬렉션을 생성하고자 한다면 내부 메커니즘이 영향을 받아 정상 동작하지 않을 수도 있다.
- 따라서 필드 레벨에서 초기화하는 것이 안전하고 코드가 간결하다. </br></br></br></br></br>




##  1-7. 테이블, 컬럼명 생성 전략(스프링 부트 사용 시)</br>
(1) 자바의 camel case -> under score 방식으로 전환됨</br></br>
(2) .(dot) -> under score</br></br>
(3) 대문자 -> 소문자</br></br></br></br></br>




## 1-8. 연관관계 편의 메서드 </br>
(1) 연관관계 편의 메서드의 경우 양방향 연관관계가 설정된 엔티티 간 데이터의 동기화를 위해 사용된다. </br></br>
(2) 사용 시 코드의 가독성 향상, 연관관계를 변경해야 할 때 한쪽만 수정하면 된다는 장점 존재 </br></br>
(3) 비즈니스의 핵심 로직이 존재하는 엔티티에 연관관계 메서드를 구현하는 것이 일반적이다. </br></br></br></br></br></br></br>






# 2. 도메인 개발, 테스트 </br>
##  2-1 @Autowired</br>
(1) 스프링이 Dependency injection 기능을 수행하기 위한 어노테이션 </br></br>

(2) @Autowired를 통해 외부 클래스가 빈을 주입받기 위해선 주입 클래스들은 일반적으로 스프링 빈으로 등록된 클래스이거나 또는
@Component, @Service, @Repository, @Controller 등의 어노테이션이 적용, 또는 @Configuration으로 등록된 클래스들을 주입시킬 수 있다.</br></br></br></br></br>





##  2-2. @Transactional</br>
(1) JPA의 모든 데이터 변경은 트랜잭션 내부에서 진행되어야 한다.</br></br>

(2) @Transactional(readOnly = true) 어노테이션은 조회 서비스에서 성능을 최적화한다. Service 계층에서 우선 해당 어노테이션을 
@Transactional(readOnly = true)으로 주고, 단순 조회가 아닌 메서드에  @Transactional 어노테이션을 별도로 추가한다. </br></br></br></br></br>




## 2-3. @AllArgsConstructor, @RequiredArgsConstructor </br>
(1) 공통적으로 두 어노테이션은 Lombok 라이브러리에서 제공하는 기능으로, 생성자를 기본으로 만들어주는 기능이다.</br></br>

(2) 차이점은, @AllArgsConstructor은 모든 필드를 파라미터로 받는 생성자를 만들고,  @RequiredArgsConstructor은 @NotNull 또는 final로
선언된 필드만을 생성자의 파라미터로 받게 된다. 필드의 선언 방식에 따라 결정되는 파라미터들이 달라지는 것이 두 어노테이션의 차이점이라고 볼 수 있다. </br></br></br></br></br>





## 2-4. 테스트 </br>
(1) given / when / then</br>
- given : ~이 주어질 때,
- when : ~으로 처리하면,
- then : ~한 결과가 예상된다.</br></br></br>


(2) 테스트 과정에서 실제 사용하는 외부 DB가 아닌 완전히 격리된 환경인 In-memory DB를 사용하는 방법(스프링 부트)</br>
- build.gradle에서 dependencies에 runtimeOnly 'com.h2database:h2' 필드가 존재하는 경우</br>
- main이 아닌 test 디렉토리에 resource/application.yml을 생성한다.</br>
- datasource/url 영역을 jdbc:h2:mem:test으로 설정하면 외부 데이터베이스가 없어도 인메모리 데이터베이스 환경에서 테스트를 할 수 있다.</br>
- 만약 스프링 부트를 사용한다면 위와 같은 설정이 없어도 테스트를 인메모리 DB 환경에서 동작하도록 설계되어 있다.</br></br></br>



(3) Assertions.assertEquals()</br>
- 해당 메서드의 경우 테스트 프레임워크에서 사용되는 메서드로, 두 값을 비교하여 그 결과가 같은지 확인하는 데 사용된다.
  이 메서드는 예상 값(expected value)과 실제 값(actual value)을 비교하여 두 값이 동일한지 확인하고, 만약 값이 다르다면 테스트가 실패하게 된다.</br></br></br></br></br>




## 2-5. 도메인 주도 설계(엔티티) </br>
(1) 객체지향적으로 생각해 보면 데이터(필드)를 가지고 있는 도메인(엔티티)가 핵심 비즈니스 로직을 담고 있는 설계가 응집력이 좋은 설계이다.</br></br>

(2) (객체지향적 설계) 데이터를 변경할 경우 @Setter를 사용하는 것이 아닌 도메인에 핵심 비즈니스 로직을 담은 메서드를 통해 변경하는 것이 바람직하다.</br></br>

(3) 엔티티에 특정 엔티티의 비즈니스와 관련된 로직을 추가하는 것은 도메인 주도 설계의 한 방법으로 코드의 응집성, 재사용성, OOP의 핵심 내용 중 하나인 
단일 책임 원칙(SRP, 특정 엔티티 내부에 관련 로직들이 캡슐화되어 있음)을 준수하는 데 도움이 된다.</br></br>

(4) 비즈니스 로직이 대부분 엔티티에 존재하고, 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 하는데 이처럼 엔티티가 비즈니스 로직(상태와 행위)을 가지고 객체지향의 특성을
활용하는 것을 Domain Model Pattern이라고 하고, 반대로 엔티티에 비즈니스 로직이 거의 없고 서비스 계층에서 대부분 비즈니스 로직을 처리하는 것을 
Transaction Script Pattern이라고 한다. JPA와 같은 ORM 기술을 사용할 때는 Transaction Script Pattern보다는 Domain Model Pattern 방법을 채택해서 
대부분 설계한다. 각각 장단점이 존재하기 때문에 상황에 따라 설계 방법을 고민해본다. </br></br>

(5) Domain Model Pattern을 사용하면 Repository와 관계없이 관련 엔티티에 대해 바로 테스트를 작성할 수 있다.</br></br>

(6) Cascade 전파 옵션의 경우, 라이프 사이클에 대해서 다른 연관된 엔티티도 동일하게 관리할 수 있을 때 사용하는 것이 좋다.</br></br>

(7) 특정 객체를 생성해야 하는 경우 new를 통해 생성해서 setter()를 사용하지 말고 별도의 객체 생성 메서드를 별도로 만드는 방식으로
코드의 별도 제약을 두면서 설계하는 것이 추후 유지보수성, 가독성을 높일 수 있다. </br></br></br></br></br>




## 2-6. JPA의 장점</br>
(1) 데이터베이스 SQL을 직접 다루는 라이브러리인 mybatis, Jdbctemplate들은 데이터 변경 시 직접 서비스를 다루는 서비스 계층에서 
Transactional script(데이터베이스 상의 여러 작업을 묶어 하나의 트랜잭션에서 처리하는 것)를 모두 작성해 줘야 하는 번거로움이 있다.</br></br>

(2) 그러나 JPA를 사용하게 되면, 엔티티의 데이터만 변경해 줘도 JPA가 알아서 변경된 포인트들을 변경 감지를 통해 트랜잭션 커밋 시 데이터베이스에 관련된 쿼리를 모두 전송해준다. </br></br></br></br></br></br></br>




# 3. 웹 계층 개발 </br>
## 3-1. Bean Validation & BindingResult </br>
(1) Bean Validation의 경우 서버사이드에서 자바 어노테이션을 기반으로 필드에 대한 검증을 수행할 수 있는 라이브러리이다. </br></br>

(2) @NotNull, @NotEmpty</br>
- @NotNull의 경우 말 그대로 null 조건을 허용하지 않는다. 따라서 " ", "" 도 입력 가능하다.</br>
- @NotEmpty의 경우 공백을 허용하지 않는다.</br></br>

(3) BindingResult 객체 </br>
- 검증 과정에서 발생한 오류를 담는 객체로, 스프링에서 데이터 검증 및 바인딩 과정에서 발생한 오류를 담는 역할을 수행하며 controller 영역에서
  주로 사용된다. BindingResult를 통해 오류에 대한 후처리를 할 수 있게 된다.</br></br></br></br></br>




## 3-2. Model, @ModelAttribute, @PathVariable, @RequestParam, @RequestBody</br>
(1) Model </br>
- View, Controller 간의 데이터 전달을 위해 사용되는 객체 </br>
- 주로 Controller에서 View 영역으로 데이터를 전달할 때 사용한다. </br>
- 주로 addAttribute()를 통해 데이터를 Model에 추가하고 이를 View 영역에서 사용한다. </br> </br> </br>

(2) @ModelAttribute </br>
- Controller의 메서드 매개변수에 사용되는 어노테이션  </br></br>
- 메서드 매개변수에 해당 어노테이션 지정 시, 관련 객체를 자동으로 생성하고 요청 파라미터를 바인딩하여 전달한다. </br>
- 폼 입력값을 받거나 Controller로 -> View 데이터 전달 시 사용 </br>
- 주로 해당 어노테이션을 사용해 Controller에서 Model 객체를 생성하고 초기화하게 된다. </br></br></br>

(3) @PathVariable</br>
- 요청 URL 경로 변수를 바인딩할 때 사용된다. </br>
- 경로 변수를 메서드 매개변수로 전달받아 Controller에서 사용할 수 있도록 함 </br>
- RESTful API에서 resource identifier를 전달할 때 사용한다. </br></br></br>

(4) @RequestParam </br>
- HTTP 요청의 쿼리 파라미터나 폼 데이터를 메서드의 파라미터로 바인딩할 때 사용한다 </br>
- URL에 포함된 쿼리 스트링, HTML For 데이터를 주로 처리할 때 사용 </br></br></br>

(5) @RequestBody </br>
- HTTP 요청 바디를 메서드의 파라미터로 바인딩할 때 사용한다.</br>
- 주로 JSON, XML 형식의 데이터를 전달할 때 사용한다.</br>
- HTTP Request의 Content-type 헤더에 따라 알맞은 메시지 컨버터를 적용하여 요청 바디를 지정된 타입으로 변환한다. </br></br></br></br></br>





## 3-3. 엔티티는 최대한 순수하게 유지해야 한다. </br>
(1) 엔티티는 최대한 순수하게 유지하는 것이 중요하다. 특정 영역(화면 계층 등)에 종속되지 않고 핵심 비즈니스 로직에만
종속하도록 설계해야 한다.</br></br>

(2) 이러한 방식으로 설계해야 애플리케이션 복잡도가 증가하더라도, 엔티티를 여러 곳에서 많이 사용하더라도 유지보수성이 높아지게 된다. </br></br>

(3) 엔티티는 핵심 비즈니스 로직만 가지고 있고, 별도의 계층(화면 등)을 위한 로직은 존재하지 않아야 한다. </br>
(화면을 위한 로직, 화면에 맞는 API들은 폼 객체나 DTO(Data Transfer Object)를 사용하도록 한다. </br></br>

(4) API를 설계할 때는 엔티티를 그대로 외부에 직접 반환시키면 안 된다. API는 일종의 스펙이다. 
엔티티에 수정 사항이 생겨서 엔티티를 수정하면 해당 API의 스펙 자체가 변경되기 때문에 불완전한 API가 될 수 있다. </br>
(예외로 서버사이드 템플릿 엔진을 쓰는 경우 서버 안에서 데이터가 도는 것이기 때문에 엔티티 직접 반환 방법을 고려해볼만 하다 그렇다 하더라도 가장 권장되는 방법은
특정 목적에 맞는 DTO로 변환해서 반환하는 것이 좋다.) </br></br></br></br></br>




## 3-4. (중요) JPA에서 수정은 변경 감지(Dirty checking)를 사용하는 방법이 Best practice이다.</br>
(1) 참고로 특정 엔티티에 대한 ID값은 항상 조심해야 한다.
- @PathVariable로 바인딩 받은 아이디 값이 임의적으로 조작되어 넘겨받을 수도 있다. 이를 대비하기 위해 해당 아이디가
  서비스 계층의 앞단이나 뒷단에서 유저가 해당 엔티티에 대한 권한이 있는지 체크하는 로직이 서버사이드에 존재해야 한다. </br></br></br>


(2) 변경감지와 병합(Dirth checking & merge) : 실무에서 em.merge()는 거의 사용되지 않는다. JPA에서 허용하고 있는 데이터 수정 방법?</br>
- 변경감지와 병합의 차이를 이해하는 것이 중요, </br></br>

- 준영속 엔티티란? : 영속성 컨텍스트에서 더 이상 관리되지 않는 엔티티를 의미 </br></br>
- 엔티티를 업데이트(수정)하는 로직의 경우 해당 엔티티 객체는 이미 데이터베이스에 저장되어서 JPA에서 인식할 수 있는 identifier가 존재하는 상태이다.
  엔티티 수정을 위해 아래 코드와 같이 임의로 생성한 엔티티더라도 기존 식별자를 가지고 있다면 준영속 상태의 엔티티로 볼 수 있다. </br></br>

- 설명상 편의를 위해 setter()를 사용했다 하지만 아래에서 엔티티의 핵심 비즈니스 로직인 상품 수정을 엔티티 내부에 포함된 핵심 메서드를 통해 값을
  변경하는 부분에 대한 코드를 보여드리겠다. 
  
      @PostMapping("/items/{itemId}/edit")
      public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form) {
          Book book = new Book();
          book.setId(form.getId());
          book.setName(form.getName());
          book.setPrice(form.getPrice());
          book.setStockQuantity(form.getStockQuantity());
          book.setAuthor(form.getAuthor());
          book.setIsbn(form.getIsbn());

          itemService.saveItem(book);
          return "redirect:/items";
      }


(3) 준영속 엔티티를 수정하는 방법?
- 총 두 가지 존재 : 변경 감지(Dirty checking), 병합(Merge) </br></br></br>


(4) 변경 감지 사용</br>    

    @Transactional
      public void updateItem(Long itemId, Book param) {
          Item findItem = itemRepository.findOne(itemId);  // 식별자를 기반으로 실제 영속 상태의 엔티티를 조회
          findItem.setPrice(param.getPrice());
          findItem.setName(param.getName());
          findItem.setStockQuantity(param.getStockQuantity());

          // @Transactional 어노테이션에 의해 메서드 종료 시점 커밋이 발생
          // 필드 데이터의 변화를 확인하여 변경 감지 발생
      }
- 위의 코드보단 DTO를 써서 코드가 줄었지만 아직 setter가 사용되고 있다. 아래에서 더 줄여보도록 하자.</br>
- find(조회) 메서드를 통해 엔티티를 조회하면 영속성 컨텍스트에서 관리된다. 이때 데이터를 수정한다.
  트랜잭션 내부이므로 엔티티 조회 후 내부 데이터 변경 > 메서드 종료 시점에 트랜잭션이 커밋되며 변경감지가 작동하여 UPDATE SQL를 보내게 된다.</br></br></br>


  (5) 병합(Merge)</br>
  - 병합은 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능이다. </br>
  - 영속 엔티티의 값을 준영속 엔티티의 값으로 모두 바꾼다.(병합한다)</br>
  - 트랜잭션 커밋 시점에 변경 감지가 발생해서 데이터베이스로 UPDATE SQL이 전달된다. </br></br></br>


  (6) 실무에서는 변경 감지를 사용하는 것이 좋다. </br>
  - 변경 감지를 사용하면 원하는 필드만 부분적으로 수정 가능하지만 병합 사용 시 모든 필드가 변경된다.
    실무에서는 보통 업데이트 기능이 거의 제한적이고 심지어 병합 과정에서 필드가 null일 경우 null로 업데이트될 수도 있다. (병합 특성상 모든 필드를 업데이트하기 때문)
    또한 변경 가능한 데이터만 노출하기 때문에 병합을 사용하는 것이 오히려 번거로운 편이다.</br>

  - 엔티티 변경 시 변경 감지를 사용하도록 한다. </br></br></br>

        @Transactional
        public void updateItem2(Long itemId, UpdateItemDto itemDto) {
            Item findItem = itemRepository.findOne(itemId);  // 식별자를 기반으로 실제 영속 상태의 엔티티를 조회
            findItem.itemModify(itemDto.getPrice(), itemDto.getName(), itemDto.getStockQuantity());

            // @Transactional 어노테이션에 의해 메서드 종료 시점 커밋이 발생
            // 필드 데이터의 변화를 확인하여 변경 감지 발생
        }

  (4)번 변경 감지를 설명할 때 코드처럼 setter를 남발하는 것이 아닌 의미있는 비즈니스 메서드를 만들어서 데이터를 변경하도록 한다. </br>
  - 위의 코드처럼 트랜잭션이 존재하는 서비스 계층에 엔티티 식별자, 변경할 데이터를 보낼 때 DTO나 파라미터에 의해 명확하게 전달한다.</br>
  - 위의 코드처럼 트랜잭션이 존재하는 서비스 계층에서 영속 상태의 엔티티를 조회하고 엔티티의 데이터를 직접 변경하도록 한다.</br>
  - 트랜잭션 커밋 시점에 변경 감지가 발생하게 되어 업데이트 쿼리가 전송된다. </br></br></br>
    


  (7) 추가적으로 엔티티 필드 수정 시 의미있는 메서드를 생성한다. </br>
  - setter를 막 열어서 값을 함부로 수정하면 안 된다. 데이터 변경이 필요할 경우 의미있는 메서드를 별도로 만들어서
    변경 지점이 엔티티 레벨로 올 수 있도록 해줘야 한다. setter를 함부로 사용하면 이후 코드 추적이 어려워지는 단점이 드러난다. </br></br></br></br></br>





## 3-5. 커맨드성 (주문 등)은 컨트롤러 레벨에서 식별자만 전달, 실제 핵심 비즈니스가 담겨져 있는 서비스 계층에서 커맨드성 작업 진행</br>
(1) 이렇게 되면 트랜잭션 내부에서 엔티티 조회 시 영속 상태로 작업을 수행할 수 있게 된다.</br>


    @PostMapping("/order")
      public String order(@RequestParam("memberId") Long memberId,
                          @RequestParam("itemId") Long itemId,
                          @RequestParam("count") int count) {

          orderService.order(memberId, itemId, count);
          return "redirect:/orders";
      }
</br>

    @Transactional
      public Long order(Long memberId, Long itemId, int count) {
          // 주문자, 관련 상품 (엔티티) 조회
          Member member = memberRepository.findOne(memberId);
          Item item = itemRepository.findOne(itemId);

          // 배송정보 생성
          Delivery delivery = new Delivery();
          delivery.setAddress(member.getAddress());

          // 주문상품 생성
          OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

          // 주문 생성
          Order order = Order.createOrder(member, delivery, orderItem);

          // 주문 저장
          orderRepository.save(order);
          return order.getId();
      }
      
(2) 위처럼 단순 조회가 아닌 핵심 비즈니스 로직을 수행해야 한다면 컨트롤러에서 식별자만 넘겨주고 핵심 비즈니스 로직을 트랜잭션 내부(주로 서비스 계층)에서 수행하도록 코드를 구성한다. </br></br>


    @Transactional(readOnly = true)
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
(3) 위와 같이 서비스 계층에서 단순히 조회를 위해 Repository에 접근하는 거라면(단순 위임 상태), 바로 서비스 계층에서 Repository에 대한 접근을 바로 호출한다.</br></br></br></br></br></br></br>







# 4. API 개발</br> 
## 4-1. API</br>
(1) 최근에는 프론트엔드에서 SPA(Single Page Application) 형식으로 React.js를 통해 View 영역을 개발하고 있다. 이런 경우에는 백엔드 입장에서 뭔가 예전처럼
서버사이드에서 HTML을 렌더링할 일이 많진 않다. 이러한 작업들은 프론트엔드, 앱 개발 진영에서 해결한다</br>

(2) Monolithic Architecture(MA)에서 Micro-service Architecture(MSA)로 많이 변화하고, 다양한 클라이언트(프론트엔드, 앱) 원활한 통신, 마이크로서비스 자체 간의 통신 등을 
위해 최근에는 API로 통신할 일이 더욱 늘어나게 되었다. </br>

(3) 따라서 API를 잘 설계하고 개발하는 것이 중요하다. </br>

(4) JPA를 사용하면서 API를 설계하는 부분이 기존 SQL을 가지고 설계하는 방식과 다르기 때문에 이 부분에 대해 올바른 방법인지 정리해 보고자 한다. </br></br>

(5) 기타 라이브러리 : 스프링 부트 3.0 이상) Hibernate5JakartaModule
- 엔티티 간 LAZY 전략이 걸린 경우 엔티티를 조회할 때 연관 엔티티는 프록시 객체로 끌고 온다. 스프링 부트의 경우 REST API를 Json 형태로 반환하는 데
  이 과정에서 Jackson 라이브러리가 사용된다. 프록시 객체는 영속화되어 있지 않아서 Jackson 라이브러리가 이를 직렬화하여 json으로 반환 시에는 문제가 생긴다.
  이때 Hibernate5JakartaModule 라이브러리를 사용해서 하이버네이트가 제공하는 기능을 jackson 라이브러리가 이해할 수 있는 형태로 변환하는 역할을 수행하게 된다.
  이를 통해 하이버네이트가 로딩한 프록시 객체에 대해서도 해당 객체를 직렬화하여 json 형태로 반환될 수 있도록 도와준다. </br></br></br></br></br>





## 4-2. 관련 어노테이션들
(1) @RequestBody </br>
- HTTP 요청 바디를 메서드의 파라미터로 바인딩할 때 사용한다.</br>
- 주로 JSON, XML 형식의 데이터를 전달할 때 사용한다.</br>
- HTTP Request의 Content-type 헤더에 따라 알맞은 메시지 컨버터를 적용하여 요청 바디를 지정된 타입으로 변환한다.</br></br></br></br> </br>




## 4-3. 엔티티를 그대로 노출할 때 생기는 문제들 </br>

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
(1) 특정한 영역을 위한(화면 등) 필드 validation을 위해 엔티티에 모두 넣어줘야 하는 문제</br>
- 특정한 api에서는 validation이 필요하지 않을 수도 있는데 이미 원본 엔티티에 validation 로직을 넣은 것.</br></br> </br>


(2) 엔티티의 스펙이 변경되는 경우, API 스펙까지 변경된다. 엔티티를 외부에 노출시키지 않는다.</br>
- 이 경우 엔티티의 필드를 변경해 버려서 API 스펙이 변해버리는 것이 문제가 된다. </br>
- 엔티티라는 것은 굉장히 여러 곳에서 사용될 수 있기 때문에 변경될 확률이 높다. 이를 통해 API 스펙까지 변경되고 엔티티와 API 자체가 1:1로 강하게 결합되어 버리는 것. </br>
- 결론적으로는 API 스펙을 위한 별도의 DTO(Data Transfer Object)를 설계하는 것이 좋다. </br> 
- 특히 API를 설계할 때는 요청과 응답 모두 엔티티를 사용하지 않고 DTO를 사용해 요청과 응답을 처리한다. </br> </br>

- 추가적으로 실무에서는 동일한 엔티티에 대해 API 용도에 따라 조금씩 달라지는 스펙을 요구하는 일이 많다. 한 엔티티에 각각의 API를 위한
  응답 로직을 담기는 어렵다.</br></br>

- 컬렉션을 직접 반환하면 향후 API 스펙을 반환하기 어렵다.</br>

- 위와 같은 과정을 통해 DTO와 API 스펙이 1:1이 되도록 설계한다.</br></br></br></br></br></br></br>






# 5. 지연 로딩(Lazy Loading)과 조회 성능 최적화 : @XToOne </br> 
## 5-1. 개요 
(1) 실무에서 여러 테이블들을 JOIN하며 API가 생성된다. 이러한 과정에서 어떻게 성능까지 챙길 수 있는지, 페이징 처리같은 것들을 포함해
성능 문제들을 어떻게 해결할 수 있는지 확인해 보고자 한다.</br> 

(2) JPA, Spring을 활용해 복잡한 API를 설계해야 할 때 성능을 충분히 챙기면서 이 부분을 설계할 수 있는지 이 부분에 대해 문제 해결이 필요하다.</br></br></br></br></br>




## 5.2 엔티티를 외부 API로 노출시키지 않는다.</br>
(1) 계속 강조되는 부분이지만 간단한 애플리케이션이 아니라면 엔티티를 직접 외부로 노출시키지 않는 것이 중요하다.</br></br>

(2) 별도의 DTO로 변환해서 반환하는 것이 더 좋은 방법이다. </br></br>

(3) 추가적으로 지연 로딩(LAZY)을 피하기 위해 즉시 로딩(EAGER)으로 전략을 변경하지 말아야 한다. EAGER의 경우 연관관계로 맺어져 있는
데이터가 필요한 상황이 아닐 때에도 해당 엔티티의 데이터를 가져오기 때문에 성능 문제가 발생할 수 있다. </br></br>

(4) EAGER는 성능 튜닝에 큰 어려움을 주기 때문에 항상 LAZY를 기본으로 하고 성능 최적화가 필요한 경우 FETCH JOIN을 활용한다. </br></br>

(5) fetch join은 JPA에서 제공하는 성능 최적화 방법으로, 특정 엔티티를 조회할 때 연관관계로 맺어진 엔티티를 프록시 객체로 조회하는 것이 아니라 
실제 데이터베이스에 쿼리를 통해 연관 엔티티를 모두 끌고 오고 Persistence context의 1차 캐시에 저장해둔다. 이를 통해 조회된 엔티티들의 
데이터가 필요할 경우 1차 캐시를 바로 참조하기 때문에 추가적인 쿼리가 발생하지 않는다.</br></br></br></br></br>




## 5-3. 기본 전략은 LAZY, 여러 엔티티를 한 번에 가져와야 하는 경우 FETCH JOIN을 사용한다. </br>
[case 1 : 엔티티 조회 이후에 엔티티를 DTO로 변환하는 경우]</br>

    public List<Order> findAllWithMemberDelivery() {
          return em.createQuery("select o from Order o " +
                                          "join fetch o.member m " +
                                          "join fetch o.delivery d", Order.class)
                  .getResultList();
    }
    

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        return orders.stream()
                .map(order -> new SimpleOrderDto(order))
                .collect(Collectors.toList());
    }

(1) 기본 로딩 전략을 lazy, 이후 여러 엔티티 필요 시 해당 부분에 fetch join을 적용하여 엔티티 그래프를 한 번에 묶어서 가져오면
대부분의 성능 문제를 해결해 볼 수 있다. </br></br></br>


    2024-02-14T16:13:24.280+09:00 DEBUG 10257 --- [nio-8080-exec-3] org.hibernate.SQL                        : 
        select
            o1_0.order_id,
            d1_0.delivery_id,
            d1_0.city,
            d1_0.street,
            d1_0.zipcode,
            d1_0.status,
            m1_0.member_id,
            m1_0.city,
            m1_0.street,
            m1_0.zipcode,
            m1_0.name,
            o1_0.order_date,
            o1_0.status 
        from
            orders o1_0 
        join
            member m1_0 
                on m1_0.member_id=o1_0.member_id 
        join
            delivery d1_0 
                on d1_0.delivery_id=o1_0.delivery_id
                
(2) 위와 같이 엔티티를 fetch join해서 쿼리 한 번으로 조회할 수 있다. </br>

(3) Fetch join으로 order를 select하는 과정에서 order -> member, order -> delivery는 이미 조회된 상태이므로 LAZY LOADING이 발생하지 않는다.</br>

(4) 해당 코드의 경우 코드를 작성하기 쉽고 재사용성이 높다는 특징이 있다.</br> </br> </br>



[case 2 : DTO를 바로 조회하는 경우]</br>

       public List<OrderSimpleQueryDto> findOrderDto() {
            return em.createQuery("select new jpa_basic_shop.jpa_basic_shop.dto.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                                            "from Order o " +
                                            "join o.member m " +
                                            "join o.delivery d", OrderSimpleQueryDto.class)
                    .getResultList();
      }
  
        @GetMapping("/api/v4/simple-orders")
        public List<OrderSimpleQueryDto> ordersV4() {
            return orderRepository.findOrderDto();
        }
</br>

        
(1) 조회 결과 

    2024-02-14T17:53:29.246+09:00 DEBUG 15538 --- [nio-8080-exec-2] org.hibernate.SQL                        : 
      select
          o1_0.order_id,
          m1_0.name,
          o1_0.order_date,
          o1_0.status,
          d1_0.city,
          d1_0.street,
          d1_0.zipcode 
      from
          orders o1_0 
      join
          member m1_0 
              on m1_0.member_id=o1_0.member_id 
      join
          delivery d1_0 
              on d1_0.delivery_id=o1_0.delivery_id

  
(2) case2의 방식은 일반적인 쿼리를 사용할 때처럼 원하는 값을 프로젝션에 넣어 직접 조회한다. </br>
(3) new 명령어로 JPQL의 결과를 DTO로 즉시 변환했다. </br>
(4) 프로젝션에 원하는 데이터를 넣어 조회한다는 장점이 있다. </br>
(5) Repository 재사용 관점에서 좋지 않다. Repository에 API 스펙을 위한 코드가 직접적으로 들어가기 때문이다. </br></br></br></br></br>





## 5-4. CASE1, CASE2 정리 </br>
(1) Repository는 순수히 엔티티를 조회하거나 엔티티 객체 그래프 조회를 최적화하는 데 사용해야 한다. </br></br>


![image](https://github.com/twojun/JPA_Practice/assets/118627166/b2ab10eb-0d7f-4d5d-83c3-a5852cb6de54)</br>
(2) 이런 경우 repository 디렉토리 하위에 최적화를 위한 새로운 디렉토리를 생성하고 별도의 클래스를 만든다.</br>

- 위와 같이 따로 패키지를 나눈 이유?</br>
- OrderRepository : 순수히 실제 Order 엔티티를 조회하거나 객체 그래프 탐색을 위해 존재하는 Repository</br>
- OrderSimpleQueryRepository : 특정 영역(화면)이나 특정 API에 설계 및 의존하기 위해 순수 Repository와 분리된 영역(결과적으로 의존관계를 떼어내기 위함이다.)</br>
- 꼭 Order 엔티티가 아니더라도 비즈니스상 다른 엔티티, 해당 엔티티들을 위한 Repository도 위와 같이 설계하는 것이 좋다.</br></br>



![image](https://github.com/twojun/JPA_Practice/assets/118627166/318b3a07-95c1-4ecd-9bd4-0b1ef513d405)</br>
(3) 생성한 클래스에 DTO를 바로 조회하는 코드를 작성한다. </br></br>


(4) 정리 
- 엔티티를 DTO로 변환하거나 DTO로 직접 조회하는 방식은 Trade-off가 존재한다. </br>
- 각각의 장단점이 명확하기 때문에 상황에 맞는 방법을 채택해서 사용한다. </br>

- 우선 엔티티를 DTO로 변환하는 방법을 선택 </br>
- 필요하다면 FETCH JOIN을 활용해 성능을 최적화한다. </br>
- 이 방법으로 해결되지 않는다면 DTO를 바로 조회하는 방법을 채택해 본다. </br>
- 이 부분까지 와도 해결이 되지 않는다면 JPA가 제공하는 Native SQL이나 Spring JDBCTemplate으로 SQL을 직접 사용한다. </br></br></br></br></br></br></br>







# 6. 컬렉션 조회 최적화 : OneToMany </br>
## (참고) 6-1. JPA에서 ManyToMany를 적용하지 않는 이유?</br>
- 데이터베이스에서 n:m 설계 측면에서는 ManyToMany 연관관계가 사용될 수 있지만 JPA에서는 일반적으로 권장되지 않는데 이유는 아래와 같이 정리해볼 수 있다.</br></br>

(1) 복잡성 증가 
- ManyToMany를 관리하기 위한 중간 테이블을 추가해 줘야 하며 중간 테이블에는 양측 엔티티를 관리하기 위한 주요 키가 포함되기 때문에
데이터베이스 스키마가 오히려 복잡해지는 문제를 갖고 있다.</br></br>

(2) 관리의 어려움
- 중간 테이블은 양측 엔티티와 연관이 없다. 중간 테이블을 위한 추가적인 로직이 필요해지는데 이 부분이 유지보수에 어려움을 준다.</br></br>

(3) 중간 테이블로 인한 성능 저하 
- ManyToMany는 중간 테이블을 통해 해결되기 때문에 양측 엔티티에 접근할 때마다 중간 테이블을 거쳐야 하기 때문에 대규모 시스템 에선 성능저하가 발생할 수 있다. </br></br></br></br></br>





## 6-2. 개요 </br>
(1) @XToOne 관계는 fetch join등을 활용해 쉽게 최적화 문제를 풀 수 있었다. 그러나 컬렉션 조회(@OneToMany)의 경우 데이터베이스 입장에서는 
N:M 관계에 있을 때 그만큼 결과 로우 수가 늘어나기 때문에 최적화가 어려운 편이다. </br> </br>

(2) 이러한 부분들을 최적화할 수 있는 방법을 정리힌다. </br> </br> </br> </br> </br>





## 6-3. Value Object(값 타입)외 엔티티들은 DTO에도 노출시키지 않는다.
![image](https://github.com/twojun/JPA_Practice/assets/118627166/f0916b40-389d-4ae7-bbb2-d7d214baa7ad) </br>
![image](https://github.com/twojun/JPA_Practice/assets/118627166/7d147f11-bc22-4f66-a3d8-425bcd5723c5) </br>

(1) DTO 내부에 엔티티가 존재하면 안 된다. </br>

(2) 엔티티를 외부에 노출하지 않고 DTO를 사용한다는 것은 DTO가 엔티티에 대한 의존을 완전히 끊어야 한다는 의미이다. </br>

(3) 단순 컬렉션 조회의 경우 지연로딩 특성상 수많은 쿼리가 발생된다. 단순한 어드민 애플리케이션에서는 문제가 되지 않을 수 있으나
실시간(Real-time), 어느 정도 대규모 및 사용자가 몰릴 수 있는 시스템에서는 성능 문제가 충분히 발생할 수 있다. 이 부분을 해결하기 위한 방법을 확인해 보자.</br></br></br></br></br>





## 6-4. 컬렉션 조회 최적화 - fetch join 사용

(1) JPQL에서 distinct는 루트 엔티티의 아이디를 기준으로 중복을 제거시킨다. </br>
![image](https://github.com/twojun/JPA_Practice/assets/118627166/d666cdff-bebe-4d81-9b20-f4afa8f3f22e)
- 위처럼 프로젝션 이후에 오는 객체를 루트라고 보통 표현하는데, 루트가 중복인 경우 해당 중복 엔티티를 제외시키고 컬렉션에 담는다.</br></br>


(2) 지연 로딩이 설정되었을 때 FETCH JOIN을 통해 쿼리를 최적화시켰다.

    public List<Order> findWithItem() {
          return em.createQuery(
                          "select distinct o " +
                          "from Order o " +
                          "join fetch o.member m " +
                          "join fetch o.delivery d " +
                          "join fetch o.orderItems oi " +
                          "join fetch oi.item i", Order.class)
                  .getResultList();
      }

</br>

      select
        o1_0.order_id,
        d1_0.delivery_id,
        d1_0.city,
        d1_0.street,
        d1_0.zipcode,
        d1_0.status,
        m1_0.member_id,
        m1_0.city,
        m1_0.street,
        m1_0.zipcode,
        m1_0.name,
        o1_0.order_date,
        oi1_0.order_id,
        oi1_0.order_item_id,
        oi1_0.count,
        i1_0.item_id,
        i1_0.dtype,
        i1_0.name,
        i1_0.price,
        i1_0.stock_quantity,
        i1_0.artist,
        i1_0.etc,
        i1_0.author,
        i1_0.isbn,
        i1_0.actor,
        i1_0.director,
        oi1_0.order_price,
        o1_0.status 
    from
        orders o1_0 
    join
        member m1_0 
            on m1_0.member_id=o1_0.member_id 
    join
        delivery d1_0 
            on d1_0.delivery_id=o1_0.delivery_id 
    join
        order_item oi1_0 
            on o1_0.order_id=oi1_0.order_id 
    join
        item i1_0 
            on i1_0.item_id=oi1_0.item_id

(3) 위와 같이 쿼리가 최적회된 것을 확인해 볼 수 있었다.</br>
(4) fetch join으로 SQL이 1번만 실행됨 </br>
(5) OneToMany join의 경우 조회되는 데이터베이스 로우 수가 증가한다. JPA의 distinct는 실제 쿼리에 distinct를 추가시켜주고
아이디 값이 동일한 중복 엔티티에 대해서 중복을 제거시키게 된다. </br></br></br></br></br>





## 6-4. @OneToMany 연관관계에서 Collection Fetch join의 심각한 단점
- 여기까지 봤을 땐 OneToMany 컬렉션 조회에서도 Fetch join을 사용하면 대부분의 문제가 해결되는 것처럼 보인다. </br>
- 하지만 데이터베이스 제약 조건으로 인해 발생하게 되는 심각한 단점이 하나 존재한다. </br></br></br>


(1) 단점 : OneToMany(1:N) 관계에서 fetch join 사용 시 페이징이 불가능하다. </br>

    public List<Order> findWithItem() {
          return em.createQuery(
                  "select o " +
                          "from Order o " +
                          "join fetch o.member m " +
                          "join fetch o.delivery d " +
                          "join fetch o.orderItems oi " +
                          "join fetch oi.item i", Order.class)
                  .setFirstResult(0)
                  .setMaxResults(200)
                  .getResultList();
      }

- 위처럼 페이징 JPQL이 작성된 메서드를 호출하게 되면 아래와 같은 문구가 확인된다.</br>
![image](https://github.com/twojun/JPA_Practice/assets/118627166/de0b1b6f-3ea3-410d-8ec7-62cc34285957)</br>
위의 경고는 firstResult, maxResults를 컬렉션 fetch join과 함께 사용하면 fetch join 과정에서 사용된 페이징 쿼리는
실제 페이징되지 않고 메모리 내부에서 페이징 처리를 하게 된다는 경고 메시지이다.</br>

- 데이터의 수가 Hello world 수준으로 적은 수준이라면 당장 문제가 되지 않겠지만 실무에서는 적어도 몇 천개, 많다면 수십 만개의 데이터가
  애플리케이션 메모리에 로드되어 페이징 처리가 되는 것이다.</br>

- 방대한 양의 데이터를 다루고 있다면 메모리 부하로 인해 서비스 장애가 발생할 수 있다.</br></br></br>




(2) 또한 limit, offset 등 실제 쿼리에서도 페이징 쿼리가 발생하지 않는다.</br>
- fetch join을 사용하는 경우 연관된 엔티티까지 모두 로드하기 때문에(데이터의 로우 수가 예상한 것보드 많아질 수 있는 문제 발생) 예상되는 페이징 처리 결과를 얻기가 어려울 수 있다.
  따라서 JPA는 메모리 내부에서 페이징 처리를 하고 실제 쿼리로는 페이징 처리를 하지 않는다.</br>

- 따라서 OneToMany 관계에서 fetch join이 사용되는 경우 페이징을 바로 하면 안 된다 </br>

- Collection fetch join을 사용하면 실제 페이징 처리가 불가능하다. 위와 같이 Hibernate는 경고 로그를 남기면서
  모든 데이터를 데이터베이스에서 읽어오고 애플리케이션 메모리에서 페이징 처리를 해버리는 문제가 생기기 때문에 매우 위험하다. </br>

- 컬렉션 fetch join은 한 번만 사용한다. </br>

- ManyToOne, OneToOne 상태에서는 페이징 처리를 해도 된다.</br></br></br></br></br>





## 6-5. 컬렉션 조회 최적화 - fetch join, 페이징 처리 개요 </br>
(1) 컬렉션을 fetch join하면 실제로 페이징 처리가 불가능하다. </br>
- OneToMany 관계에서 1:N join이 발생하게 되어 데이터가 예측할 수 없게 증가한다. </br>
- 예측이 어려운 이유는 위에서도 잠시 언급되었지만, 페이징의 주체는 1이다. 그러나 컬렉션 fetch join 시, N을 기준으로
  데이터 로우 수가 N배만큼 증가하게 된다. </br>
- 따라서 N 관계를 갖는 엔티티를 기준으로 페이징 처리의 기준이 생기게 된다. </br> </br> </br>


(2) 따라서 대부분의 페이징 처리 + Collection fetch join을 함께 조회하는 방법을 정리해 보겠다.</br> </br> </br> </br> 





## 6-6. XToOne(OneToOne, ManyToOne) 관계에서는 Fetch join 허용, ManyToOne 관계에서는 hibernate.default_batch_fetch_size, @BatchSize를 사용 </br>

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
          return em.createQuery(
                  "select o " +
                          "from Order o " +
                          "join fetch o.member m " +  // order -> member(N:1)
                          "join fetch o.delivery d", Order.class)  // order -> delivery(1:1)
                  .setFirstResult(offset)
                  .setMaxResults(limit)
                  .getResultList();
      }
(1) XToOne(OneToOne, ManyToOne) 관계에서는 Fetch join의 결과 로우 수가 영항을 받지 않기 때문에 모두 Fetch join을 적용한다. </br>

(2) 컬렉션은 지연 로딩으로 조회한다.</br>

(3) 지연 로딩의 성능 최적화를 위해 hibernate.default_batch_fetch_size, @BatchSize를 사용한다.</br></br></br></br>



(4) hibernate.default_batch_fetch_size : Global settings (상황에 따라 다르긴 하지만 대부분 글로벌 설정을 적용시킨다.)</br>
![image](https://github.com/twojun/JPA_Practice/assets/118627166/fca1f6dc-969e-4c55-8c36-618dd099674e)</br>
- application.yml 기준으로 위와 같이 batch size를 줄 수 있다.</br></br>


(5) hibernate.default_batch_fetch_size : Entity settings</br>
![image](https://github.com/twojun/JPA_Practice/assets/118627166/7cc668f9-2faa-4786-9e63-fe98abc2b875)</br>
- 컬렉션 엔티티에 개별적으로 적용</br></br>

![image](https://github.com/twojun/JPA_Practice/assets/118627166/b9b22d88-9689-40f3-baf7-12baadeced70)</br>
- 만약 ManyToOne(N:1) 단일 엔티티에 적용시켜야 한다면 해당 레퍼런스 변수에 주는 것이 아닌</br>

![image](https://github.com/twojun/JPA_Practice/assets/118627166/eac61c9d-fa6c-44bb-91d4-b9964fd6f1ff)</br>
- 위와 같이 엔티티 상단에 직접 적용시킨다.</br></br></br>


(6) 핵심은 해당 옵션을 사용하게 되면 컬렉션(OneToMany)이나 프록시 객체를 조회할 때 설정된 Batch size만큼 in 쿼리를 통해 한 번에 조회하게 된다. 

     /**
       * V3.1 : Entity -> DTO : fetch join, paging(Batch size 사용)
       */
      @GetMapping("/api/v3.1/orders")
      public List<OrderDto> orderV3Page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit ) {
          List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
  
          return orders.stream()
                  .map(order -> new OrderDto(order))
                  .collect(Collectors.toList());
      }
- 해당 메서드를 호출하면 페이징된 SQL을 보내게 된다.</br></br>


![image](https://github.com/twojun/JPA_Practice/assets/118627166/3b01178f-2a93-4936-a884-eec8d3ba9c21)</br>
- Fetch join이 적용된 Member, Delivery 엔티티의 경우 order와 각각 ManyToOne, OneToOne(N:1, 1:1 - XToOne) 실제 조회되는 조회 로우 수가
  N쪽에 의해 증가하는 영향을 받지 않기 때문에 페이징 처리에 있어 영향을 받지 않는다. </br>

- 이후 OrderItem은 별도로 fetch join이 명시되어 있지 않기 때문에 지연 로딩을 통해 프록시 객체로 조회된다. </br>

- 이후 OrderItem이 실제로 사용될 때 해당 엔티티 개수대로 hibernate.default_batch_fetch_size 옵션에 의해 실제 in 쿼리를 날려서 batch size만큼 데이터를 끌어온다.</br>

- OrderItem 내부에 존재하는 엔티티들에 대해서도 해당 엔티티 개수대로 batch size만큼 데이터를 가져온다.</br>

- 만약 특정 엔티티의 데이터 개수가 1,000개이고 batch_size = 100라면, 모든 데이터를 끌고 오기 위해 in 쿼리가 10번 발생된다.</br>

- 해당 방식은 fetch join과 비교 시 쿼리 호출 수가 조금 증가하지만 쿼리에 대한 실질적인 데이터베이스 전송량이 감소한다는 장점이 있다. </br>

- 컬렉션 Fetch join의 경우 페이징 처리가 불가능하지만 batch_size 방법을 활용한 최적화에서는 페이징 처리가 가능하다. </br> </br> </br>


(7) 결론 </br>
- XToOne 연관관계는 fetch join을 해도 페이징에 영향을 받지 않는다. </br>
- 따라서 XToOne 관계는 fetch join을 사용해서 성능 최적화를 하고, 나머지는 hibernate.default_batch_fetch_size를 사용해서 최적화한다. 
- 보통 fetch size 옵션은 글로벌로 설정하는 것이 편리하다. </br></br></br></br></br>




## 6-7. 참고 : hibernate.default_batch_fetch_size의 크기는 얼마나 줘야 할까? </br>
(1) 사이즈의 경우 최댓값은 1000이지만 최솟값에는 제한이 없다 (0~1000)</br>

(2) 데이터베이스 벤더사마다 in 쿼리의 제한이 있을 수 있으므로 이 부분을 확인하고 size를 결정한다. </br>

(2) WAS, 데이터베이스가 버틸 수 있다면 size를 크게 잡아도 되지만 만약 이 부분에 대해 순간적인 성능 부하 부담이 있다면
초기에 100 정도로 잡아두고 사용해 보면서 상황에 따라 천천히 늘려가는 방법도 고민해 볼 수 있다. </br></br></br></br></br></br></br>






# 7. JPA에서 DTO로 바로 조회 (컬렉션 조회) </br>
## 7-1. 컬렉션 조회

     /**
       * V4 : JPA에서 DTO로 바로 조회(Collection)
       */
      @GetMapping("/api/v4/orders")
      public List<OrderQueryDto> ordersV4() {
          return orderQueryRepository.findOrderQueryDto();
      }
 </br>

     public List<OrderQueryDto> findOrderQueryDto() {
          List<OrderQueryDto> result = findOrders();
  
          result.forEach(order -> {
              List<OrderItemQueryDto> orderItems = findOrderItems(order.getOrderId());
              order.setOrderItems(orderItems);
          });
  
          return result;
      }
  
      private List<OrderItemQueryDto> findOrderItems(Long orderId) {
          return em.createQuery(
                          "select new jpa_basic_shop.jpa_basic_shop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                  "from OrderItem oi " +
                                  "join oi.item i " +
                                  "where oi.order.id = :orderId", OrderItemQueryDto.class)
                  .setParameter("orderId", orderId)
                  .getResultList();
      }
  
      private List<OrderQueryDto> findOrders() {
          return em.createQuery(
                          "select new jpa_basic_shop.jpa_basic_shop.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                                  "from Order o " +
                                  "join o.member m " +
                                  "join o.delivery d", OrderQueryDto.class)
                  .getResultList();
      }

 </br>
(1) 루트 프로젝션(Order)의 경우 1번, 컬렉션은 n번 실행되었다. </br> </br>

(2) XToOne 관계들을 먼저 조회하고 OneToMany 관계는 각각 별도로 처리  </br>
- 위와 같이 처리되는 이유는 우선 XToOne 관계는 조회 데이터 로우 수가 급격히 증가하지 않는다. </br>
- 하지만 OneToMany 관계는 조인 시 로우 수가 증가하게 된다.  </br> </br>

(3) 로우 수가 증가하지 않는 XToOne 관계는 조인으로 한 번에 조회, OneToMany 관계는 최적화가 어려우므로 
findOrderItems()와 같은 메서드로 별도 조회한다. </br> </br> </br> </br> </br> 




## 7-2. 쿼리 조회 결과</br>

     select
          o1_0.order_id,
          m1_0.name,
          o1_0.order_date,
          o1_0.status,
          d1_0.city,
          d1_0.street,
          d1_0.zipcode 
      from
          orders o1_0 
      join
          member m1_0 
              on m1_0.member_id=o1_0.member_id 
      join
          delivery d1_0 
              on d1_0.delivery_id=o1_0.delivery_id


    select
          oi1_0.order_id,
          i1_0.name,
          oi1_0.order_price,
          oi1_0.count 
      from
          order_item oi1_0 
      join
          item i1_0 
              on i1_0.item_id=oi1_0.item_id 
      where
          oi1_0.order_id=?


     select
          oi1_0.order_id,
          i1_0.name,
          oi1_0.order_price,
          oi1_0.count 
      from
          order_item oi1_0 
      join
          item i1_0 
              on i1_0.item_id=oi1_0.item_id 
      where
          oi1_0.order_id=?

(1) 총 Order 1번, OrderItem -> Item (ManyToOne) 2번 </br>
(2) 이 부분에서도 order를 가져오기 위해 총 2번의 추가 쿼리가 발생된 N+1 문제가 발생한 것을 확인할 수 있다. </br>
(3) 이 부분을 최적화하는 방법을 확인해 보자 </br></br></br></br></br></br></br>







# 8. JPA에서 DTO로 직접 조회 - 컬렉션 조회 최적화 (N + 1 문제 해결)</br>
## 8-1. 컬렉션 조회 최적화</br>

    /**
       * V5 : JPA에서 DTO로 바로 조회(Collection) - N+1 문제 최적화
       */
      @GetMapping("/api/v5/orders")
      public List<OrderQueryDto> ordersV5() {
          return orderQueryRepository.isOptimizeFindAllByDto();
      }


    public List<OrderQueryDto> isOptimizeFindAllByDto() {
        List<OrderQueryDto> result = findOrders();   /** Order -> Member, Order -> Delivery 조회 */
        List<Long> orderIds = toOrderIds(result);

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);  /** OrderItem join Item on oi_item_id = i_item_id */

        result.forEach(order -> order.setOrderItems(orderItemMap.get(order.getOrderId())));    /** 각각의 OrderItem 루프를 돌며 orderId를 가져온다. */
        return result;
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                        "select new jpa_basic_shop.jpa_basic_shop.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                                "from Order o " +
                                "join o.member m " +
                                "join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

     private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream()
                .map(order -> order.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }


    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpa_basic_shop.jpa_basic_shop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                "from OrderItem oi " +
                                "join oi.item i " +
                                "where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

</br>

<수행 쿼리 결과>

    select
          o1_0.order_id,
          m1_0.name,
          o1_0.order_date,
          o1_0.status,
          d1_0.city,
          d1_0.street,
          d1_0.zipcode 
      from
          orders o1_0 
      join
          member m1_0 
              on m1_0.member_id=o1_0.member_id 
      join
          delivery d1_0 
              on d1_0.delivery_id=o1_0.delivery_id

      select
        oi1_0.order_id,
        i1_0.name,
        oi1_0.order_price,
        oi1_0.count 
      from
          order_item oi1_0 
      join
          item i1_0 
              on i1_0.item_id=oi1_0.item_id 
      where
          oi1_0.order_id in (?, ?)
</br>
(1) 쿼리의 경우 루트 프로젝션(Order) 1번, Collection(OrderItem) 1번으로 총 2번의 쿼리가 실행되었다.</br>

(2) 코드를 보면, ToOne 관계를 우선적으로 조회하고 이 과정에서 얻은 식별자(orderIds)를 통해 컬렉션인 OrderItem을 한 번에 조회한다. 이때 in절을 사용해 OrderItem의 개수만큼 OrderItem을 한 번에 조회했다.</br>

(3) 현재 보면 직접 JPQL을 써서 DTO로 조회하는 부분이 마냥 편하지만은 않다는 생각이 든다. 약간의 트레이드 오프가 존재하는데 이전에 fetch join보다는
코드는 훨씬 길지만 프로젝션 절에 들어가는 데이터의 양이 훨씬 줄어든 것을 볼 수 있다. </br> </br> </br> </br> </br> </br> </br>







# 9. JPA에서 DTO로 직접 조회 - 컬렉션 조회 (플랫 데이터 최적화) 쿼리 1회 발생</br>
## 9-1. 플랫 데이터 최적화</br>

    /**
       * V6 : JPA에서 DTO로 바로 조회(Collection) - 플랫 데이터 최적화
       */
      @GetMapping("/api/v6/orders")
      public List<OrderQueryDto> ordersV6() {
          List<OrderFlatDto> flats = orderQueryRepository.findAllByDtoWithFlat();
  
          return flats.stream()
                  .collect(Collectors.groupingBy(o -> new OrderQueryDto(
                                  o.getOrderId(),
                                  o.getName(),
                                  o.getOrderDate(),
                                  o.getOrderStatus(),
                                  o.getAddress()),
                          Collectors.mapping(o -> new OrderItemQueryDto(
                                          o.getOrderId(),
                                          o.getItemName(),
                                          o.getOrderPrice(),
                                          o.getCount()),
                                  Collectors.toList())))
                  .entrySet().stream()
                  .map(e -> new OrderQueryDto(
                          e.getKey().getOrderId(),
                          e.getKey().getName(),
                          e.getKey().getOrderDate(),
                          e.getKey().getOrderStatus(),
                          e.getKey().getAddress(),
                          e.getValue()))
                  .collect(toList());
      }


    public List<OrderFlatDto> findAllByDtoWithFlat() {
        return em.createQuery(
                        "select new jpa_basic_shop.jpa_basic_shop.dto.OrderFlatDto(" +
                                "o.id, " +
                                "m.name, " +
                                "o.orderDate," +
                                " o.status, " +
                                "d.address, " +
                                "i.name, " +
                                "oi.orderPrice, " +
                                "oi.count)" +
                                "from Order o " +
                                "join o.member m " +
                                "join o.delivery d " +
                                "join o.orderItems oi " +
                                "join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
</br>
(실행된 쿼리) </br>

    select
          o1_0.order_id,
          m1_0.name,
          o1_0.order_date,
          o1_0.status,
          d1_0.city,
          d1_0.street,
          d1_0.zipcode,
          i1_0.name,
          oi1_0.order_price,
          oi1_0.count 
      from
          orders o1_0 
      join
          member m1_0 
              on m1_0.member_id=o1_0.member_id 
      join
          delivery d1_0 
              on d1_0.delivery_id=o1_0.delivery_id 
      join
          order_item oi1_0 
              on o1_0.order_id=oi1_0.order_id 
      join
          item i1_0 
              on i1_0.item_id=oi1_0.item_id
</br>

(1) 총 한 번의 쿼리로 원하는 데이터를 최적화해서 가져온다. </br>

(2) 쿼리는 한 번이지만 여러 번의 조인으로 인해 데이터베이스에서 애플리케이션으로 전달하는 중복 데이터가 추가되므로 상황에 따라서는
이전 방식보다 더 느릴 수도 있다.</br>

(3) OneToMany join으로 인해 페이징이 불가능하다. </br></br></br></br></br></br></br>






# 10. 지연 로딩에서의 조회 성능 최적화, 컬렉션 조회 최적화 정리</br>
## 10-1. 점진적으로 확인해본 최적화 방법 : 엔티티 조회 및 DTO로 변환 후 조회</br>
- 엔티티를 조회해서 그대로 반환 ->  엔티티에서 DTO로 변환 ->  fetch join을 활용해 쿼리 최적화 ->  OneToMany에서의 컬렉션 페이징 </br></br>

(1) 컬렉션 페이징에서 문제를 확인해 볼 수 있었다. 바로 페이징 처리가 불가능하다는 것.</br>
(2) XTonOne 연관관계의 경우 fetch join으로 쿼리 수를 최적화한다.</br>
(3) OneToMany 컬렉션의 경우 LAZY를 적용하고 hibernate.default_batch_fetch_size 또는 @BatchSize로 최적화</br></br></br></br></br>


 
 ## 10-2. JPA에서 DTO 직접 조회 </br>
- JPA에서 DTO 직접 조회(컬렉션 조회, 플랫 데이터 최적화)</br></br>
 
- 컬렉션 조회 최적화 : OneToMany 관계의 컬렉션은 in절을 활용해서 메모리에 미리 조회해놓고 최적화한다. </br>
- 플랫 데이터 최적화 : join 결과를 그대로 조회한 후, 애플리케이션 레벨에서 원하는 형태로 변형 </br></br></br></br></br>




## 10-3. 처리 순서 </br>
(1) 엔티티로 조회해서 이후 DTO로 변환하는 방법을 우선적으로 접근 </br>

(2) 이후 fetch join으로 쿼리 수를 최적화한다. </br>

(3) 만약 컬렉션이 존재하는 경우 : 페이징이 필요한 경우에 hibernate.default_batch_fetch_size 또는 @BatchSize로 최적화한다, 페이징이 필요없다면 fetch join을 사용한다. </br>

(4) (1) - (3)까지 엔티티 -> DTO 변환 조회 방식으로 해결되지 않는 경우 DTO 조회 방식을 사용한다. </br>

(5) DTO 조회 방식으로도 해결되지 않는다면 Native SQL, Spring JDBCTemplate을 사용한다. </br></br>


(6) 엔티티 조회 접근 방식을 추천하는 이유 : 엔티티 조회 방식의 경우 fetch join이나 hibernate.default_batch_fetch_size 또는 @BatchSize 같이 코드를 거의 수정하지 않고
일부 옵션만 변경해서 다양한 성능 최적화 시도가 가능하다. 하지만 DTO 직접 조회 방식의 경우 성능 최적화 시 많은 코드 변경이 따르게 된다.</br></br></br></br></br>





## 10-4. 성능 최적화와 코드 복잡도 사이의 트레이드 오프</br>
(1) 모든 경우가 그런 것은 아니지만, 일반적인 성능 최적화의 경우 기존에 단순했던 코드를 복잡하게 몰고갈 수 있다.</br>

(2) 엔티티 조회(조회 후 DTO 변환) 방식은 JPA가 많은 부분을 최적화해 주기 때문에 단순한 코드를 대체적으로 유지하면서 성능 최적화가 가능했다. </br>

(3) 하지만 DTO 조회 방식의 경우 SQL을 직접 다루는 부분과 패턴이 유사하기 때문에 기존 코드를 복잡하게 몰고갈 수 있다.</br></br></br>


(4) 앞서 작성된 V4 코드의 경우 코드가 단순하고 유지보수가 쉽다는 특징이 있기 때문에 단건 조회의 경우 V4 방식을 생각해 볼 수 있다. </br>

(5) V5의 특징을 살펴보면 우선 V4는 연관된 엔티티 n개 필요할 때, 추가적인 쿼리가 n번 발생된다. v5는 이 부분의 문제를 해결해 줌으로써
연관된 엔티티를 가져올 때 한 번에 메모리에 가지고 올라와서 루트 프로젝션을 조회하는 쿼리 1번, 연관된 엔티티를 모두 가져오는 쿼리 1번으로 총 2번의 
쿼리가 발생됐다. 쿼리의 수를 최적화해 줬지만 코드 복잡도가 증가한 것을 알 수 있다. 지금은 단순히 Hello World 예제 수준의 데이터셋으로 큰 차이를 못 느끼지만
실무에서 만약 주문수가 1,000개였다고 가정해보자. V4의 경우 주문은 한 번의 쿼리로 모두 가져오지만 각각의 주문상품에 대한 쿼리를 1,000번 추가적으로 더 날려서 N+1
문제를 일으키지만 V5의 경우 주문 1번, 주문 상품 1번 총 2번의 쿼리를 보냄으로써 상당한 성능 최적화를 가져온 것을 알 수 있다.</br>

(6) 마지막 V6은 접근방식부터가 다르다. 최종적으로 쿼리가 1번만 발생되어 좋아보이지만, Order를 기준으로 페이징 처리가 불가능하다. 실무에서는 
보통 페이징이 필요하기 때문에 채택하기 어려운 최적화 방법이다.</br></br></br></br></br></br></br>







# 11. 실무에서의 필수 최적화 : OSIV(Open Session In View)와 성능 최적화 </br>
## 11-1. OSIV(Open Session In View) </br>
(1) JPA에서 Persistence context(엔티티의 라이프사이클 관리)를 관리하고 데이터베이스와 상호작용, 트랜잭션 관리 등 핵심 역할을 수행하는 인터페이스를
Entity Manager(엔티티 매니저)라고 불렀다. 하지만 JPA가 표준화되기 전 하이버네이트만 사용될 때는 이러한 엔티티 매니저를 Session(세션)이라고 불렀다. </br>

(2) Open Session In View : Hibernate  / Open EntityManager In View : JPA, 관례상 OSIV라고 다들 부르고 있다.</br></br>

(3) OSIV는 API를 반환한다면 반환할 때까지, 서버사이드에서 html을 렌더링한다고 하면 렌더링할 때까지(완전히 응답이 나갈 때까지), 
트랜잭션이 종료되더라도 영속성 컨텍스트, 데이터베이스 커넥션을 끝까지 가지고 있는 속성을 의미한다.</br>

(4) 트래픽이 조금이라도 몰리는 프로덕션을 개발하고 운영 및 관리를 하고 있다면, OSIV를 충분히 이해하고 있어야 한다.</br></br></br></br></br>




## 11-2. OSIV ON </br>
![image](https://github.com/twojun/JPA_Practice/assets/118627166/e89836ac-3c2d-49b1-bc51-71de92b6ebdf)</br>
<이미지 출처 : 김영한님의 Java ORM 표준 JPA 프로그래밍></br></br>


![image](https://github.com/twojun/JPA_Practice/assets/118627166/a980a556-7d10-45b5-aa1f-df759ba19195)</br>
(1) 우선 스프링 부트를 JPA와 함께 실행시키면 위와 같은 WARN 로그가 남는다.</br>
- spring.jpa.open-in-view : true (default)</br></br>


(2) 기본적으로 JPA가 데이터베이스 커넥션 가져오고 반납할까?</br>
- JPA는 영속성 컨텍스트를 동작시키기 위해 데이터베이스 커넥션을 사용해야 한다. 영속성 컨텍스트와 데이터베이스 커넥션은 밀접하게 매칭되어 있다.</br>
- 영속성 컨텍스트의 기능들은 데이터베이스 커넥션을 1:1로 가져오면서 사용할 수 있게 된다. </br>
- 데이터베이스 커넥션 획득 시점은, 기본적으로 서비스 계층에서 데이터베이스 트랜잭션을 시작할 때 얻는다. 그리고 OSIV가 기본값으로 켜져 있으면,
트랜잭션이 이미 끝났더라도 위에서 언급한 것처럼 응답 결과가 클라이언트측으로 던져질 때까지 커넥션, 영속성 컨텍스트를 계속 유지하는 전략이다. </br></br>


(3) LAZY LOADING은 영속성 컨텍스트가 존재해야 가능하고 영속성 컨텍스트는 기본적으로 데이터베이스 커넥션을 유지한다. api 컨트롤러에서 지연 로딩이 가능했던 것이다.</br>

(4) 하지만 OSIV ON 전략은 치명적인 단점이 한 가지 존재한다. </br>

(5) 바로 너무 오랜 시간동안 커넥션 리소스를 사용하기 때문에 실시간 트래픽이 중요한 도메인에서는 커넥션 풀에서 가져다가 사용할 커넥션이 모자라는 문제가 생길 수도 있다. 
이 부분은 위와 같은 도메인에서는 서비스 장애로 이어질 수 있다. </br></br></br></br></br>





## 11-3. OSIV OFF </br>
![image](https://github.com/twojun/JPA_Practice/assets/118627166/4c7e16ba-d94b-4f8b-a2ce-4a5f6ae6d9ce) </br>
- spring.jpa.open-in-view : false </br></br>

(1) OSIV OFF 전략은 반대로 트랜잭션이 종료되면 영속성 컨텍스트를 종료하고 커넥션 풀에서 가져왔던 데이터베이스 커넥션도 반납한다. </br>

(2) OFF 전략도 단점이 존재한다. 모든 지연 로딩을 트랜잭션 내부에서 처리해야 한다. 따라서 OSIV를 끈 상태에서는 위의 그림처럼
지연 로딩을 영속성 컨텍스트의 라이프 사이클이 살아있는 범위 내에서 모두 처리해야 한다.</br>

(3) 지금까지 작성된 많은 코드가 트랜잭션을 컨트롤러 레벨에서 처리해 준 것도 많다. 결론적으로 트랜잭션이 종료되기 전에 지연 로딩을
강제로 호출해 두어야 하는 문제가 있다. 참고로 보통 서비스 계층에 선언적 트랜잭션(Declarative Transaction, @Transactional)을 적용해 주어 트랜잭션을 관리하는 경우가 많다.
일반적으로 비즈니스 로직을 담고 있는 서비스 계층의 메소드와 결합시키는 것이 좋다. 이유는 Repository(DAO)로부터 읽어온 데이터를 사용하고 변경하는 등의 
작업을 수행하는 곳이 서비스 계층이기 떄문이다. </br></br>

(4) 해당 옵션을 끄고 Repository, Service Layer를 벗어난 곳에서 지연로딩을 할 경우 예외가 발생하는데 이 부분을 확인해 보자</br></br>

![image](https://github.com/twojun/JPA_Practice/assets/118627166/004f9969-c693-4797-b4ee-2dcfc67ab3d5)</br>
- 우선 위와 같이 open-in-view를 꺼둔다.</br>


       /**
         * V3.1 : Entity -> DTO : fetch join, paging(Batch size 사용)
         */
        @GetMapping("/api/v3.1/orders")
        public List<OrderDto> orderV3Page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                          @RequestParam(value = "limit", defaultValue = "100") int limit) {
            List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
    
            return orders.stream()
                    .map(order -> new OrderDto(order))
                    .collect(Collectors.toList());
        }

- 위의 메서드를 호출하게 되면 아래와 같은 LazyInitializationException 예외가 터지게 된다. 이 부분을 어떻게 해결할까?
![image](https://github.com/twojun/JPA_Practice/assets/118627166/4c2d8492-3bb0-4b51-b003-7d4429acdb7b) </br>
(OrderItem이 LAZY이기 때문에 프록시를 초기화해서 실제 값을 가져오려는 과정에서 프록시를 초기화 할 수 없다는 메시지)</br></br></br></br></br>






## 11-4. 해결 방법 </br>
- 일반적으로 엔티티 몇 개를 수정하거나 등록하는 것은 성능상 크게 문제되지 않음 </br>
- 문제는 복잡한 화면이나 api를 반환하기 위한 쿼리는 해당 스펙에 맞추어 최적화가 필요 </br>
- 규모 있고 큰 서비스를 개발한다면 이 둘의 관심사를 명확하게 분리하는 것은 유지보수 관점에서 크게 도움이 되기 때문에
  아래와 같이 분리가 가능하다면 분리하는 것이다. </br></br>

- 예를 들어 주문 서비스에 대해서
(1) OrderService : 핵심 비즈니스 로직이 담겨있는 계층</br>
(2) OrderQueryService : 특정 화면이나 api에 맞춘 서비스(주로 읽기 전용 선언적 트랜잭션 사용)</br></br>

(3) 트래픽이 몰리는 실시간 서비스에서는 OSIV OFF를 하고 어드민처럼 많은 커넥션을 필요로 하지 않는 영역에선 OSIV를 ON으로 설정한다.
