package com.jpaTest.querydslTest;


import com.jpaTest.member.dto.MemberDto;
import com.jpaTest.member.dto.QMemberDto;
import com.jpaTest.member.dto.UserDto;
import com.jpaTest.member.entity.Member;
import com.jpaTest.member.entity.QMember;
import com.jpaTest.team.entity.Team;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Objects;

import static com.jpaTest.member.entity.QMember.*;  // 기본 별칭 사용
import static com.jpaTest.team.entity.QTeam.team;
import static org.assertj.core.api.Assertions.assertThat;
import static com.querydsl.jpa.impl.JPAQueryFactory.*;
import static com.querydsl.jpa.JPAExpressions.*;

@SpringBootTest
@Transactional  // 테스트 이후 db 컨트롤
@Commit
public class QuerydslTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;


    @BeforeEach
    public void before() throws Exception {
        // JPAQueryFactory 초기화
        queryFactory = new JPAQueryFactory(em);

        //
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    public void startJPQL() {
        //member1을 찾아라.
        // jpql 기본 문법
        String qlString = """
                select m
                from Member m
                where m.username = :username
                """;
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() throws Exception {
        //given

        /*
        JPAQueryFactory: Querydsl 을 사용하기 위한 유틸리티 클래스
        - 엔티티 매니저를 상속받아 영속성 컨텍스트의 기능 유지

        Querydsl: JPQL 코드를 동적쿼리로 변경해주는 쿼리 빌더
        - 엔티티 -> orm 코드 -> jpql 로 변환 -> jpql 코드를 querydsl 을 통해 동적 쿼리 변환 -> sql

        Q 클래스: 엔티티를 querydsl 에서 사용하기 위해 querydsl 에서 제공하는 메타 클래스
         - gradle 을 통해 빌드 하는 시점에 Entity 어노테이션을 보고 자동 생성
         - QMember 클래스는 직접 구현 할 일없음
         - 주의! build 경로를 통해 생성된 경로는 git 에 올라가면 안됨
        */

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        // QMember m = new QMember("m");
        // 이렇게 별칭을 지정하는 방법도 있음  // select * from Member as m 같은거

        //when
        Member findMember = queryFactory
                .select(member)  // 여기 member 라는 별칭은 Q 클래스에서 디폴트로 생성함
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();  // 단건 조회  // 단건 아니면 오류남
        //then
        assertThat(Objects.requireNonNull(findMember).getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl3() {
        //member1을 찾아라.
        List<Member> findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))  // where 조건은 이렇게 걸 수 있음
                .fetch();
        //assertThat(Objects.requireNonNull(findMember).getUsername()).isEqualTo("member1");
        assertThat(findMember).hasSize(1);

    }

    @Test
    public void search() {
        List<Member> result1 = queryFactory
                .selectFrom(member)
                .where(
                        // 빌더 패턴이라 체이닝 제공함
                        // member.username.eq("member1").and(member.age.eq(10))
                        member.username.eq("member1"), (member.age.eq(10))
                        // 같은 and 조건이면 and 함수 말고 쉼표로 묶을 수 있음
                )
                .fetch();  // 여러건 조회  // List 로 반환
        assertThat(result1.size()).isEqualTo(1);
    }

    /*
    // where 조건 정리인데 필요하면 확인
    member.username.eq("member1") // username = 'member1'
    member.username.ne("member1") //username != 'member1'
    member.username.eq("member1").not() // username != 'member1'
    member.username.isNotNull() //이름이 is not null
    member.age.in(10, 20) // age in (10,20)
    member.age.notIn(10, 20) // age not in (10, 20)
    member.age.between(10,30) //between 10, 30
    member.age.goe(30) // age >= 30
    member.age.gt(30) // age > 30
    member.age.loe(30) // age <= 30
    member.age.lt(30) // age < 30
    member.username.like("member%") //like 검색
    member.username.contains("member") // like ‘%member%’ 검색
    member.username.startsWith("member") //like ‘member%’ 검색
    * */


    /*
    // 조회 정리

    //List
    List<Member> fetch = queryFactory
    .selectFrom(member)
    .fetch();

    //단 건
    Member findMember1 = queryFactory
    .selectFrom(member)
    .fetchOne();

    //처음 한 건 조회
    Member findMember2 = queryFactory
    .selectFrom(member)
    .fetchFirst();

    //페이징에서 사용
    QueryResults<Member> results = queryFactory
    .selectFrom(member)
    .fetchResults();

    //count 쿼리로 변경
    long count = queryFactory
    .selectFrom(member)
    .fetchCount();
    */

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    public void sort() {
        // give
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        // when
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                // nullsLast 는 null 이 들어올 경우 우선순위를 최하위로
                // nullsFirst 는 null 이 들어올 경우 우선순위를 최상위로
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        // then
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    // 페이징 관련 -> 페이지네이션을 더 많이 사용함
    @Test
    public void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) //0부터 시작(zero index)
                .limit(2) //최대 2건 조회
                .fetch();
        assertThat(result.size()).isEqualTo(2);
    }

    /**
     * JPQL
     * select
     * COUNT(m), //회원수
     * SUM(m.age), //나이 합
     * AVG(m.age), //평균 나이
     * MAX(m.age), //최대 나이
     * MIN(m.age) //최소 나이
     * from Member m
     */
    @Test
    public void aggregation() throws Exception {

        List<Tuple> result = queryFactory
                .select(member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min())
                .from(member)
                .fetch();
        Tuple tuple = result.get(0);
        // tuple 은 querydsl 에서 제공
        // 데이터를 조회시 정해진 규격 외의 데이터가 올때 처리하기 위해 사용
        // 실무에서 안씀

        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    // group
    @Test
    //* 팀의 이름과 각 팀의 평균 연령을 구해라.
    public void group() throws Exception {
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)  // join(조인 대상, 별칭으로 사용할 Q타입)  // select * from member, team  // 이런식
                // 외래키랑 기본키 끼리 조인
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    @Test
    /*
     * 세타 조인: 연관관계가 없는 상태에서 조인
     *  - 2가지 방법이 있음
     *  - join 으로 다 가져온 다음 where 로 필터링
     *  - on 절을 통해서 필터링 (이 기능은 querydsl 예전 버전에서 지원을 하지 않음)
     * */
    public void theta_join1() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> result1 = queryFactory
                .select(member)
                .from(member, team)  // member 와 team 을 그냥 join 함 // select * from member, team  // 이런식
                .where(member.username.eq(team.name))  // member1.username = team.name
                .fetch();

        assertThat(result1)
                .extracting("username")
                .containsExactly("teamA", "teamB");

        List<Member> result2 = queryFactory
                .select(member)
                .from(member)
                .join(team).on(member.username.eq(team.name))  // inner 조인하는 방법 // inner join Team team with member1.username = team.name
                //
                .fetch();

        assertThat(result2)
                .extracting("username")
                .containsExactly("teamA", "teamB");

        // 찾아보니 암묵적 조인 이후 where 나 inner join 후 성능 상에 큰 차이는 없다고는 함

        // leftJoin 도 가능
        List<Member> result3 = queryFactory
                .select(member)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();
        result3.forEach(System.out::println);
    }

    /*
    패치 조인
     - jpa 에만 존재
     - 외래키에 해당하는 객체 적보를 지연로딩을 통해 처음부터 가져오지 않고 나중에 해당 값을 사용할 때 select 를 하다고니 N+1 문제가 발생
     - N+1 문제: jpa 를 통해 객체를 N개 조회 했을 때 외래키에 해당하는 값을 가져오지 않아서 N 개의 데이터를 사용하는 시점에 N번 쿼리를 호출하는 일이 발생(1은 처음에 데이터를 가져올때 실행)
     - 지연 로딩 없이 데이터를 한번에 가져오는 조인
    */

    @PersistenceUnit
    EntityManagerFactory emf;  // 앤티티 매니저들의 메모리를 관리하는 엔티티 매니저 팩토리의 객체
    // 어노테이션을 붙여야 해당 객체를 사용 할 수 있음(영속성 컨텍스트는 존재하는 메모리 구역이 중요하기 때문에)
    // 영속성 컨텍스트는 @Transactional 에서만 동작


    @Test
    public void fetchJoinNo() throws Exception {
        em.flush();
        em.clear();
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();
        // member1 의 데이터를 조회해 왔는데 member1 의 team 객체를 진짜 불러왔는지 확인
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        // 엔티티 매니저 팩토리는 사실 유틸 클래스를 쓸려고 사용
        assertThat(loaded).as("페치 조인 미적용").isFalse();
        // 안불러왔음
    }

    @Test
    public void fetchJoinUse() throws Exception {
        em.flush();
        em.clear();
        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .fetchJoin()  // 이걸 통해 fetch 조인함
                .where(member.username.eq("member1"))
                .fetchOne();
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 적용").isTrue();
    }

    @Test
    public void subQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");
        /*
        여기서 QMember 를 하나 더 만들어서 alias 를 바꾸는 이유는 우리가 서브 쿼리를 쓸 때 해당 쿼리에 alias 를 따로 줘야되기 때문

        select a.*
        from a
        where a.name = (
            select name
            from a as sub // 이런 의미
            where username = "박상훈"
        )

        */

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        // JPAExpressions  // 이런 서브 쿼리의 표현식을 사용하기 위한 객체 // static import 처리
                        select(memberSub.age.max())
                        .from(memberSub)
                ))
                .fetch();
        assertThat(result).extracting("age")
                .containsExactly(40);

        /*
        todo: 주의사항!
        JPAExpressions 은 인라인 뷰 함수를 지원하지 않음
        // select * from (select a.name, a.age from A where name = "박상훈");  // 이런거
        JPA 의 컨셉 자체가 SQL 은 데이터 조회를 위해 사용해야 하기 때문에 데이터의 필터링이나 포메팅은 어플리케이션단이나 view 단을 통해 만들길 권장
        */
    }

    // 조건절
    @Test
    public void testCase() throws Exception{
        //given
        List<String> result1 = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        //when

        //then
        result1.forEach(System.out::println);

        List<String> result2 = queryFactory
                .select(new CaseBuilder()  // 복잡한 조건일 경우에 빌더 패턴을 추가해서 구현
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
        result2.forEach(System.out::println);

        // 아예 case builder 를 밖으로 빼서 구현 가능  // 최근에 생김
        NumberExpression<Integer> rankPath = new CaseBuilder()
                .when(member.age.between(0, 20)).then(2)
                .when(member.age.between(21, 30)).then(1)
                .otherwise(3);

        List<Tuple> result3 = queryFactory
                .select(member.username, member.age, rankPath)
                .from(member)
                .orderBy(rankPath.desc())
                .fetch();
        result3.forEach(System.out::println);
    }

    @Test
    public void findMemberDto() throws Exception{
        // 만약 조회하는 DTO 아 member 가 아니라 커스텀 된 DTO 의 경우
        List<MemberDto> result1 = queryFactory
                .select(Projections.constructor(MemberDto.class,  // select 될 클래스의 정보를 넘김
                        // Projections 은 데이터가 잘못 될 경우 컴파일시점에 오류를 발견 할 수 없음
                        // 코드의 가독성이 떨어짐
                        member.username,
                        member.age))
                .from(member)
                .fetch()
        ;
        result1.forEach(System.out::println);
    }

    @Test
    public void findUserDto() throws Exception{
        // 만약 조회하는 DTO 아 member 가 아니라 커스텀 된 DTO 의 경우
        List<UserDto> result1 = queryFactory
                .select(Projections.fields(
                        UserDto.class,
                        member.username.as("name"),  //
                        member.age))
                .from(member)
                .fetch()
        ;
        result1.forEach(System.out::println);
    }

    @Test
    public void findMemberDtoFromQueryProjection() throws Exception{
        /*
        * DTO 에 QueryProjection 어노테이션을 추가
        * 그레이들 빌드 -> QMemberDto 을 생성
        * DTO 를 통해 데이터를 가져오는 방법 중 가장 좋지만 문제점을 내포하고있음
        *  - MemberDto 클래스는 의존성이 없는 그냥 클래스였지만 Querydsl 에 의존적인 코드가 됨
        *  - 아키텍쳐적으로 고민이 필요함
        *  - 만약 Querydsl 이 아닌 joop 같은 다른 라이브러리를 사용한다면 모든게 영향을 받음
        *  - 물론 코드상으로는 주석 처리만 하면 되긴함
        * */
        List<MemberDto> result = queryFactory.select(new QMemberDto(member.username, member.age))  // 여기선 오류가 나도 타입 체크가 가능함
                .from(member)
                .fetch();
        result.forEach(System.out::println);
    }
}
