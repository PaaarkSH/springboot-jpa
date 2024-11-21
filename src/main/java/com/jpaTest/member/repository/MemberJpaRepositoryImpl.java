package com.jpaTest.member.repository;

import com.jpaTest.member.dto.MemberDto;
import com.jpaTest.member.dto.QMemberDto;
import com.jpaTest.member.entity.QMember;
import com.jpaTest.team.entity.QTeam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jpaTest.member.entity.QMember.*;  // 기본 별칭 사용
import static com.jpaTest.team.entity.QTeam.*;

public class MemberJpaRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public MemberJpaRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberDto> findAllAsDto() {
        return queryFactory
                .select(new QMemberDto(member.username, member.age, team.name))
                .from(member)
                .join(member.team, team)
                .fetch();
    }

    @Override
    public MemberDto findByIdAsDto(Long memberId) {
        QMember member = QMember.member;
        QTeam team = QTeam.team;

        return queryFactory
                .select(new QMemberDto(member.username, member.age, team.name))
                .from(member)
                .join(member.team, team)
                .where(member.id.eq(memberId))
                .fetchOne();
    }
}
