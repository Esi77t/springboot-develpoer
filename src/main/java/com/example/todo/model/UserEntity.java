package com.example.todo.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data	// lombok을 사용하여 getter, setter, toString, equals, hashcode를 자동 생성
@Entity	// JPA에서 엔티티 클래스로 사용할 것임을 명시
@Builder // lombok의 Builder패턴을 사용하여 객체를 생성할 수 있도록 한다
@NoArgsConstructor	// 기본 생성자를 생성해준다
@AllArgsConstructor	// 모든 필드를 인자로 받는 생성자를 자동으로 생성해준다
@Table(uniqueConstraints = {@UniqueConstraint(columnNames="username")})
// 테이블에서 username컬럼에 유니크 제약조건을 설정
public class UserEntity {
	
	@Id	// JPA에서 해당 필들가 테이블 PK임을 나타낸다
	@GeneratedValue(generator="system-uuid")	// id 필드의 값을 자동으로 넣는데, 값의 생성은 system-uuid라는 이름을 가진 어노테이션에 맡긴다
	@GenericGenerator(name="system-uuid", strategy="uuid")	// uuid : 128비트 길이의 고유 식별자
	private String id;	// 유저에게 고유하게 부여되는 ID, uuid로 생성되며 완전 고유한 값
	
	@Column(nullable = false, unique = true) // not null 제약조건
	private String username;	// 아이디로 사용할 유저네임, 이메일일 수도 있고 그냥 문자열일 수도 있다
	private String password;	// 유저의 패스워드
	private String role;	// 유저의 권한. 예를 들어 "관리자", "일반 사용자"와 같은 값이 들어갈 수 있다
	private String authProvider; // 소설 로그인 할 때 사용할 유저 정보의 제공자
}

