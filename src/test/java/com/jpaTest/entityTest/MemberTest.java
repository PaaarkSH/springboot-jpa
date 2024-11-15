package com.jpaTest.entityTest;


import com.jpaTest.member.entity.Member;
import com.jpaTest.team.entity.Team;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

@SpringBootTest  // 스프링 부트 실행을 통한 테스트
@Transactional  // 테스트 이후 db 컨트롤
@Commit
public class MemberTest {

    /**
     * 영속성 컨텍스트를 관리하는 엔티티 매니저를 가져옴
     * 영속성 컨텍스트: jpa 가 관리하는 매모리 공간으로 해당 공간 안에서 엔티티 관리 이후 엔티티가 끝나는 시점에 db 로 변경사항을 sql 로 만들어 호출
     * */
    @PersistenceContext
    EntityManager em;

    @Test
    public void memberTest() throws Exception{
        //given
        Team teamA = new Team();
        Team teamB = new Team();
        // persist: 영속화 -> 데이터의 영속화? -> 저장!
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        //when

        // persist: 영속화 -> 데이터의 영속화? -> 저장!
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // db 로 트랜잭션을 날리고 영속성 컨텍스트를 비워줌
        em.flush();
        em.clear();

        //then
        /**
         * JPQL: jpa 기반으로 만든 쿼리 언어 -> 쿼리 방언들은 통합해서 쿼리를 만들기 위해 사용
         * jpa 를 통해 orm 코드 작성 -> jpa 를 통해 jpql 문법으로 변경 -> jpql 에서 sql 로 변경 -> 쿼리 실행
         * */

        List<Member> members = em.createQuery("select m from Member m",Member.class).getResultList();
        for (Member member : members) {
            System.out.println("member=" + member);
            System.out.println("-> member.team=" + member.getTeam());
        }
        // 경고! assert 가 아닌 이런 콘솔 출력은 테스트에 매우 부적합함
    }
}
