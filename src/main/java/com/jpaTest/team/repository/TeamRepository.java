package com.jpaTest.team.repository;

import com.jpaTest.member.entity.Member;
import com.jpaTest.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
