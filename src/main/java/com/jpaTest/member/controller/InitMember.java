package com.jpaTest.member.controller;

import com.jpaTest.member.entity.Member;
import com.jpaTest.team.entity.Team;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Profile("local")  // 해당 프로필로 테스트할 때 데이터를 넣고 시작하고 싶어서 프로필 세팅
@Component
@RequiredArgsConstructor
public class InitMember {

    private final InitMemberService InitMemberService;

    @PostConstruct  // 의존성 주입(bean 주입) 이 끝난 이후에 실행
    // todo: 구지 이렇게 만든 이유는 스프링 라이프 사이클 때문에 PostConstruct 와 Transactional 을 같이 사용 할 수 없어서 분리해서 구현
    public void init() {
        InitMemberService.init();
    }

    @Component
    static class InitMemberService {
        // 해당 클래스를 통해 데이터를 넣어주고 시작
        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            Team teamA = new Team("teamA");
            Team teamB = new Team("teamB");
            em.persist(teamA);
            em.persist(teamB);

            for (int i = 0; i < 100; i++) {
                Team selectedTeam = i % 2 == 0 ? teamA : teamB;
                em.persist(new Member("member" + i, i, selectedTeam));
            }
        }
    }
}
