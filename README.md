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

(2) 모놀리식 아키텍처에서 마이크로서비스 아키텍처로 많이 변화하고, 다양한 클라이언트(프론트엔드, 앱) 원활한 통신, 마이크로서비스 자체 간의 통신 등을 
위해 최근에는 API로 통신할 일이 더욱 늘어나게 되었다. </br>

(3) 따라서 API를 잘 설계하고 개발하는 것이 중요하다. </br>

(4) JPA를 사용하면서 API를 설계하는 부분이 기존 SQL을 가지고 설계하는 방식과 다르기 때문에 이 부분에 대해 올바른 방법인지 정리해 보고자 한다. </br></br></br></br></br>





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






# 5. 지연 로딩(Lazy Loading)과 조회 성능 최적화하기 </br> 
## 5-1. 개요 
(1) 실무에서 여러 테이블들을 JOIN하며 API가 생성된다. 이러한 과정에서 어떻게 성능까지 챙길 수 있는지, 페이징 처리같은 것들을 포함해
성능 문제들을 어떻게 해결할 수 있는지 확인해 보고자 한다.</br> 

(2) JPA, Spring을 활용해 복잡한 API를 설계해야 할 때 성능을 충분히 챙기면서 이 부분을 설계할 수 있는지 이 부분에 대해 문제 해결이 필요하다.</br></br></br></br>
  

