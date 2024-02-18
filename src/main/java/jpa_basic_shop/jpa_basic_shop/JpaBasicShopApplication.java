package jpa_basic_shop.jpa_basic_shop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpaBasicShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(JpaBasicShopApplication.class, args);
	}

	/**
	 * JSON으로 가져올 때 LAZY LOADING이 걸린 프록시 객체에 대해서도 하이버네이트가
	 * 제공하는 기능을 사용해 올바르게 직렬화하여 JSON 형태로 데이터를 반환해주는 라이브러리
	 */
	@Bean
	Hibernate5JakartaModule hibernate5Module() {
		return new Hibernate5JakartaModule();
	}

	@Bean
	JPAQueryFactory query(EntityManager em) {
		return new JPAQueryFactory(em);
	}
}


