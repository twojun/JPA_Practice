# 1. 엔티티 설계, 주의사항</br>
1-1. 외래 키를 보유한 곳을 연관관계의 주인으로 설정한다.</br>
(1) Reference : https://github.com/twojun/ex1-hello-jpa</br></br></br></br>




1-2. 엔티티 클래스는 가급적이면 Setter를 열지 않는다.</br>
(1) Getter는 열지만, Setter는 필요한 상황이 아니라면 가급적 열지 않는다.</br></br>

(2) 실무에서 데이터 조회는 매우 빈번한 일이므로 Getter의 경우 모두 열어두는 편이 편리하다.</br>
(단순 getter 조회만으로 큰 문제가 발생하진 않는다.</br></br>

(3) 하지만 setter는 데이터 변경을 발생시키므로 추후 미래에 엔티티에 대한 데이터가 수정된 경로를 추적하기 매우 어려워진다.(유지보수가 어려워짐)</br></br>

(4) 따라서 엔티티 변경 시 setter 대신에  변경 지점이 명확하도록 변경을 위한 비즈니스 메서드를 별도로 제공하는 것이 좋다.</br></br></br></br>




1-3. 실무에서 다대다 연관관계(N:M), @ManyToMany를 사용하지 않는다.</br>
(1) 따라서 중간 엔티티를 만들고 @ManyToOne, @OneToMany로 매핑해서 사용한다.(다대다 매핑을 일대다, 다대일 매핑으로 풀어낸다.)</br></br></br></br>



1-4. 값 타입은 변경 불가능하게 설계해야 한다.</br>
(1) @Setter를 제거하고 생성자에서 모두 값을 초기화하도록 하여 변경 불가능한 클래스로 설계한다. JPA 스펙상, 엔티티나 임베디드 타입은 자바 기본 생성자의
접근 제어자는 public or protected로 설정한다. </br></br>

(2) JPA가 이러한 제약을 설정한 이유는 JPA 구현 라이브러리가 객체 생성 시 리플렉션과 같은 기술을 사용할 수 있도록 지원해야 하기 때문</br></br></br></br>



1-5. 모든 연관관계는 지연 로딩(Lazy Loading)으로 설정한다.</br>
(1) 즉시 로딩은 어떠한 SQL이 실행될지 추적하기 어렵다. em.find()와 같은 단순 단건 조회가 아닌 JPQL을 실행하는 상황에서 N+1 문제를 발생시킨다.</br></br>

(2) 실무에서 모든 연관관계는 LAZY로 잡는다.</br></br>

(3) 연관 엔티티를 함께 조회해야 한다면 Fetch join, Entity graph 기능을 사용하여 최적화한다.
- fetch join : 특정 context에서 필요한 부분들을 별도로 가져오는 방법</br></br>

(4) @XToOne 어노테이션의 기본 로딩 전략은 EAGER이므로 직접 LAZY로 설정해야 한다.
- @XToMany 어노테이션은 기본 전략이 LAZY로 설정되어 있다. </br></br></br></br>




1-6. 컬렉션은 필드에서 초기화한다.</br>
(1) Best practice의 경우, 컬렉션은 필드에서 바로 초기화하는 것이 null 문제 등을 고려해 봤을 때 가장 안전하다.</br></br>

(2) Hibernate의 경우 엔티티 영속화 시, 컬렉션을 hibernate가 제공하는 내부 컬렉션으로 변경하게 된다.</br>
- 하이버네이트가 관리하는 별도의 컬렉션으로 변경되었기 때문에 사용자가 임의의 메서드를 통해 접근하거나 값을 수정, 컬렉션을 생성하고자 한다면 내부 메커니즘이 영향을 받아 정상 동작하지 않을 수도 있다.
- 따라서 필드 레벨에서 초기화하는 것이 안전하고 코드가 간결하다. </br></br></br></br>




1-7. 테이블, 컬럼명 생성 전략(스프링 부트 사용 시)</br>
(1) 자바의 camel case -> under score 방식으로 전환됨</br></br>
(2) .(dot) -> under score</br></br>
(3) 대문자 -> 소문자</br></br></br></br>




1-8. 연관관계 편의 메서드 </br>
(1) 연관관계 편의 메서드의 경우 양방향 연관관계가 설정된 엔티티 간 데이터의 동기화를 위해 사용된다. </br></br>
(2) 사용 시 코드의 가독성 향상, 연관관계를 변경해야 할 때 한쪽만 수정하면 된다는 장점 존재 </br></br>
(3) 비즈니스의 핵심 로직이 존재하는 엔티티에 연관관계 메서드를 구현하는 것이 일반적이다. </br></br></br></br></br></br>






# 2. 도메인 개발, 테스트 </br>
2-1 @Autowired</br>
(1) 스프링이 Dependency injection 기능을 수행하기 위한 어노테이션 </br></br>

(2) 일반적으로 스프링 빈으로 등록된 클래스이거나 @Component, @Service, @Repository, @Controller 등의 어노테이션으로 등록된 클래스, @Configuration으로 등록된 클래스 내부의 빈을
주입시킬 수 있다.</br></br></br></br>





2-2. @Transactional</br>
(1) JPA의 모든 데이터 변경은 트랜잭션 내부에서 진행되어야 한다.</br></br>

(2) @Transactional(readOnly = true) 어노테이션은 조회 서비스에서 성능을 최적화한다. 따라서 조회 메서드인 경우 해당 옵션을 추가해주면 좋다. </br></br></br></br>




2-3. @AllArgsConstructor, @RequiredArgsConstructor </br>
(1) 공통적으로 두 어노테이션은 Lombok 라이브러리에서 제공하는 기능으로, 생성자를 기본으로 만들어주는 기능이다.</br></br>

(2) 차이점은, @AllArgsConstructor은 모든 필드를 파라미터로 받는 생성자를 만들고,  @RequiredArgsConstructor은 @NotNull 또는 final로
선언된 필드만을 생성자의 파라미터로 받게 된다. 필드의 선언 방식에 따라 결정되는 파라미터들이 달라지는 것이 두 어노테이션의 차이점이라고 볼 수 있다. </br></br></br></br></br>





2-4. 테스트 </br>
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
  이 메서드는 예상 값(expected value)과 실제 값(actual value)을 비교하여 두 값이 동일한지 확인하고, 만약 값이 다르다면 테스트가 실패하게 된다.</br></br></br>
