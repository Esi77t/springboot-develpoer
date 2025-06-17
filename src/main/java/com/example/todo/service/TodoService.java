package com.example.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.model.TodoEntity;
import com.example.todo.persistance.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
// 스프링 프레임워크에서 제공하는 어노테이션 중 하나로, 서비스 레이어에 사용되는 클래스를 명시할 때 사용
// 이 어노테이션을 사용하면 스프링이 해당 클래스를 스프링 컨테이너에서 관리하는 빈(bean)으로 등록하고,
// 비즈니스 로직을 처리하는 역할을 맡는다
public class TodoService {
	
	// 영속 계층의 클래스를 주입해서 사용할 수 있다
	@Autowired
	private TodoRepository repository;
	
	//	public String testService() {
	//		// 엔티티  하나 생성
	//		TodoEntity entity = TodoEntity.builder()
	//								.userId("홍길동")
	//								.title("My first todo item")
	//								.build();
	//		// TodoEntity저장
	//		repository.save(entity);
	//		// TodoEntity검색
	//		List<TodoEntity> savedEntity = repository.findByUserIdQuery(entity.getUserId());
	//		return savedEntity.get(0).getTitle();
	//	}
	
	public List<TodoEntity> create(TodoEntity entity) {
		// 매개변수로 넘어온 entity가 유효한지 검사한다
		if(entity == null ) {
			log.warn("Entity cannot be null");
			throw new RuntimeException("Entity cannot be null");
		}
		
		if(entity.getUserId() == null) {
			log.warn("Unknown user");
			throw new RuntimeException("Unknown user");
		}
		
		// 데이터베이스 추가
		repository.save(entity);
		
		log.info("Entity ID : {} is saved", entity.getId());
		
		return repository.findByUserId(entity.getUserId());
	}
	
	private void validate(TodoEntity entity) {
		
		// 매개변수로 넘어온 entity가 유효한지 확인
		if(entity == null ) {
			log.warn("Entity cannot be null");
			throw new RuntimeException("Entity cannot be null");
		}
		
		if(entity.getUserId() == null) {
			log.warn("Unknown user");
			throw new RuntimeException("Unknown user");
		}
	}
	
	// 조회하는 retrive메서드 구현
	public List<TodoEntity> retrive(String userId) {
		return repository.findByUserId(userId);
	}
	
	// 업데이트 구현
	public List<TodoEntity> update(TodoEntity entity) {
		// 저장할 엔티티가 유효한지 확인
		validate(entity);
		
		// 넘겨받은 엔티티id를 이용해 TodoEntity를 가져온다
		// 존재하지 않는 엔티티는 수정할 수 없으니까
		Optional<TodoEntity> original = repository.findById(entity.getId());
		
		original.ifPresent(todo -> {
			// 반환된 TodoEntity가 존재한다면, 값을 새 Entity값으로 덮어 씌운다
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			// 데이터 베이스에 새 값을 저장한다
			repository.save(todo);
		});
		
		// 전체 내용을 조회해서 반환
		return retrive(entity.getUserId());
	}
	
	// 삭제하기
	// 넘어온 entity가 유효한지 확인하고 delete()를 이용해서 db에서 삭제를 하고
	// findByUserId()를 사용해 조회를 해서 반환
	public List<TodoEntity> delete(TodoEntity entity) {
		validate(entity);
		
		try {
			repository.delete(entity);
		} catch (Exception e) {
			log.error("error deleting entity", entity.getId(), e);
			throw new RuntimeException("error deleting entity" + entity.getId());
		}
		
		return retrive(entity.getUserId());
		
	}
	
}

// Optional
// null 값이 나와도 추가적인 처리를 할 수 있는 다양한 메서드를 제공한다
// 1. isPresent() : 반환된 Optional 객체 안에 값이 존재하면 true, 존재하지 않으면 false를 반환한다.
// 2. get() : Optional 안에 값이 존재할 때, 그 값을 반환한다.
// 없는데 호출하면 NoSuchElementException이 발생할 수 있다
// 3. orElse(T other) : 값이 존재하지 않을 때 기본값을 반환한다
// findById는 반환하려는 메서드의 반환형이 Optional인 이유는 조회하려는 ID가 존재하지 않을 수 있기 때문이다