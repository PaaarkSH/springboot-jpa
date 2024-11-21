package com.jpaTest.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* 엔티티의 정보는 직접적으로 노출하면 안되기 때문에 DTO 를 만들어서 리턴해야함
* 여기서 리턴 대상은 api 뿐 만 아니라 메소드의 리턴도 포함한다
* */

@Data
@NoArgsConstructor
public class MemberDto {

    private String username;
    private int age;
    private String teamName;

    @QueryProjection
    public MemberDto(String username, int age, String teamName) {
        this.username = username;
        this.age = age;
        this.teamName = teamName;
    }
}
