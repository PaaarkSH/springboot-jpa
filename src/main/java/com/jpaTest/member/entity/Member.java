package com.jpaTest.member.entity;

import com.jpaTest.team.entity.Team;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor  // 생성자 매개변수가 없는 경우 생성자
@ToString(of = {"id", "username", "age"})
public class Member {

    // pk 생성 // GeneratedValue: auto increase
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    /**
     * member 에서 team 을 외래키로 가지고 있는 상황
     * N:1 의 상황 -> many to one
     * */
    @ManyToOne(fetch = FetchType.LAZY)  // 지연 로딩
    @JoinColumn(name = "team_id")  // 외래키 지정
    private Team team;

    /*
    * 생성자
    * */

    public Member(String username) {
        this(username, 0);
    }
    public Member(String username, int age) {
        this(username, age, null);
    }
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}