# 프로젝트 설정

## 목적
    스프링 프로젝트 개발의 규격을 위한 프로젝트로 구성

## 스펙
- jdk 21
- springboot 3
- querydsl 5.0.0
- postgresql 16

## 엔티티
    김영한의 스프링 JPA 강의 중 엔티티 생성 예제를 참고했습니다
- member
  - 
        create table psh_db.member (
          member_id bigint not null,
          age integer not null,
          username varchar(255),
          team_id bigint,
          primary key (member_id)
        )

        Hibernate:
          create table psh_db.member (
          member_id bigint not null,
          age integer not null,
          username varchar(255),
          team_id bigint,
          primary key (member_id)
        )
  - team
    -
        team_id bigint not null,
        name varchar(255),
        primary key (team_id)
        )
        Hibernate:
        create table psh_db.team (
        team_id bigint not null,
        name varchar(255),
        primary key (team_id)
        )
   

## API 명세
