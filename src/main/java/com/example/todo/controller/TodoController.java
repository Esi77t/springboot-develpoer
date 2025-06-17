package com.example.todo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.model.ResponseDTO;
import com.example.todo.model.TodoDTO;
import com.example.todo.model.TodoEntity;
import com.example.todo.service.TodoService;

@RestController
@RequestMapping("todo")
public class TodoController {
	
	// 실행할 때 service객체가 필드로 직접 주입이 된다
	@Autowired
	TodoService service;
	
	// 주입받은 객체로 메서드를 실행하면 된다
	//	@GetMapping("/test")
	//	public ResponseEntity<?> testTodo() {
	//		// service클래스에 있는 메서드를 호출
	//		String str = service.testService();
	//		List<String> list = new ArrayList<>();
	//		list.add(str);
	//		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
	//		return ResponseEntity.ok().body(response);
	//	}
	
	// @AuthenticationPrincipal
	// 스프링 시큐리티에서 사용자의 Principal 객체를 컨트롤러의 메서드의 인자로 주입하기 위해 사용하는 어노테이션
	// Principal : 시큐리티에서 현재 인증된 사용자를 나타내는 객체
	// 주로 SecurityContextHolder에 저장된 Authentication 객체를 통해 접근할 수 있다
	// Authentication 객체의 getPrincipal() 메서드는 인증된 사용자의 정보를 담고 있으며
	// 이 정보는 @AuthenticationPrincipal 어노테이션을 통해 컨트롤러 메서드에 자동 주입된다
	
	// 요청을 통해서 넘어오는 정보는 요청본문에 담겨져 온다
	@PostMapping
	public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
		try {
			// String tempararyUserId = "temporary-user";	// 임시 유저 아이디
			
			// TodoDTO객체를 TodoEntity객체로 변환한다
			TodoEntity entity = dto.toEntity(dto);
			
			// id값을 null로 설정하여 entity가 새로운 데이터임을 보장한다
			entity.setId(null);
			entity.setUserId(userId);
			
			// 서비스 계층에 있는 create메서드를 호출하여, TodoEntity를 데이터베이스에 저장하는 작업을 수행한다
			// 이 메서드는 추가만 하는 것이 아니라 저장된 TodoEntity 객체들을 저장한 리스트를 반환한다.
			List<TodoEntity> entities = service.create(entity);
			
			// 자바 스트림을 이용해 반환된 엔티티 리스트를 TodoDTO리스트로 반환한다
			// TodoDTO::new -> TodoDTO 생성자의 호출
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			// 변환된 TodoDTO리스트를 이용해 responseDTO를 초기화한다
			// error문자열과 List<T> data 필드 두가지를 가지고 있다
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			// 혹시나 예외가 발생하는 경우 dto대신 error에 메시지를 넣어서 반환
			String error = e.getMessage();
			
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping
	// retriveTodoList 메서드
	public ResponseEntity<?> retriveTodoList(@AuthenticationPrincipal String userId) {
		// String tempararyUserId = "temporary-user";
		
		List<TodoEntity> entities = service.retrive(userId);
		
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
	
	@PutMapping
	public ResponseEntity<?> upadateTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
		// String tempararyUserId = "temporary-user";
		
		// dto를 entity로 변환
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		entity.setUserId(userId);
		
		// 서비스 레이어의 update메서드를 이용해 entity를 업데이트한다
		List<TodoEntity> entities = service.update(entity);
		
		// 자바 스트림을 이용해 반환된 엔티티 리스트를 TodoDTO 리스트로 반환
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
	
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto) {
		// String tempararyUserId = "temporary-user";
		
		// TodoDTO객체를 TodoEntity객체로 변환한다
		TodoEntity entity = TodoDTO.toEntity(dto);
		
		// 임시 유저 아이디 설정
		entity.setUserId(userId);
		
		service.delete(entity);
		
		List<TodoEntity> entities = service.delete(entity);
		
		// 스트림을 이용해서 List안에 있는 Entity객체를 DTO로 바꾼다
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		return ResponseEntity.ok().body(response);
	}
}


	//요청 -> Controller -> Service -> Repository
	// -> Service -> Controller -> 응답