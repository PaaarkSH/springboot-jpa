package com.jpaTest.team.entity;


import com.jpaTest.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(of = {"id", "name"})
@NoArgsConstructor
public class Team {
    @Id @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    // 생성자
    public Team(String name) {
        this.name = name;
    }
}