package jpa_basic_shop.jpa_basic_shop;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpaBasicShopApplication {
	public static void main(String[] args) {
		SpringApplication.run(JpaBasicShopApplication.class, args);
	}

	// JSON으로 가져올 때 LAZY LOADING이 걸린 객체들도 우선 무시하고 가져오는 라이브러리
	@Bean
	Hibernate5JakartaModule hibernate5Module() {
		return new Hibernate5JakartaModule();
	}
}


