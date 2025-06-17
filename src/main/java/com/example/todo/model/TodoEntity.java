package com.example.todo.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor	// 엔티티 클래스로 지정할 때는 매개변수가 없는 생성자가 필요하다
// 내부적으로 Class.newInstance()와 비슷한 방식으로 객체를 만드는데
// 매개변수가 있는 생성자만 있으면 어떤 인자를 넘겨야 하는지 몰라서 예외가 발생한다
@AllArgsConstructor
@Data
// 자바 클래스를 JPA엔티티로 지정하기 위해 사용한다
@Entity
@Table(name="Todo") // Todo테이블을 찾아서 매핑을 하거나, 생성을 해준다
public class TodoEntity {
	@Id	// 엔티티의 기본키(Primary Key)필드를 나타낸다
	@GeneratedValue(generator="system-uuid")	// 기본키 값을 자동 생성하도록 지시한다
	// generator를 써서 이름을 붙여둔 @GenericGenerator를 참조한다
	@GenericGenerator(name="system-uuid", strategy="uuid")
	// Hibernate가 제공하는 커스텀 생성 전략을 정의한다
	// name="system-uuid" : 이 이름을 @GeneratedValue에서 참조한다
	// strategy="uuid" : UUID 문자열을 기본키로 생성
	private String id;	// 이 객체의 id
	private String userId;	// 이 객체를 생성한 유저의 아이디
	private String title;	// Todo의 타이틀
	private boolean done;	// Todo의 완료 여부
	
}
