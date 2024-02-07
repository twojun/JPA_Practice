package jpa_basic_shop.jpa_basic_shop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입력입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;

}