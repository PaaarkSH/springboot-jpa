package com.jpaTest.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

// todo: 이 DTO 는 기존 MemberDto 를 사용하다가 만약 다른 규격을 사용하고 싶은 경우 사용하는 DTO

@Data
@NoArgsConstructor  //
public class UserDto {
    private String name;  // username -> name 으로 변경했다는 가정
    private int age;

    @QueryProjection  // Querydsl 에서 사용하기 위해 어노테이션 추가
    public UserDto(String name, int age) {
        this.name = name;
        this.age = age;
    }
}