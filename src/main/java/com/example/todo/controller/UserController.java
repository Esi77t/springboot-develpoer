package com.example.todo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.UserDTO;
import com.example.todo.model.ResponseDTO;
import com.example.todo.model.UserEntity;
import com.example.todo.security.TokenProvider;
import com.example.todo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
	
	// UserService 생성자 주입
	private final UserService userService;
	
	// TokenProvider 생성자 주입
	private final TokenProvider tokenProvider;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	// 회원가입
	// 로그인을 해야 토큰을 주는거지, 회원가입을 했다고 토큰을 주는게 아니다
	@PostMapping("/signup")
	public ResponseEntity<?> regiterUser(@RequestBody UserDTO dto) {
		// 요청 본문에 포함된 UserDTO 객체를 수신하여 처리
		try {
			// UserDTO기반으로 UserEntity 객체 생성하기
			UserEntity entity = UserEntity.builder()
					.username(dto.getUsername())
					.password(passwordEncoder.encode(dto.getPassword()))
					.build();
			
			// UserEntity 객체를 service로 보내서 데이터베이스에 보내기
			UserEntity responseUserEntity = userService.create(entity);
			
			// 등록된 UserEntity정보를 UserDTO로 변환하여 응답에 사용
			UserDTO responseUserDTO = UserDTO.builder()
					.id(responseUserEntity.getId())
					.username(responseUserEntity.getUsername())
					.build();
			
			return ResponseEntity.ok(responseUserDTO);
		} catch (Exception e) {
			// 예외가 발생한 경우, 에러 메시지를 포함한 ResponseDTO객체를 만들어 응답에 보낸다
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error(e.getMessage())
					.build();
			return ResponseEntity
					.badRequest()	// HTTP 400응답을 생성한다
					.body(responseDTO);		// 에러 메시지를 포함한 응답 본문을 반환한다
		}
		
		
	}
	
	// Get으로 만들면 브라우저의 주소창에 아이디와 비밀번호가 노출될 수 있기 때문
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO dto) {
		// 요청 본문으로 전달된 UserDTO의 username과 password를 기반으로 유저를 조회
		UserEntity user = userService.getByCredential(
				dto.getUsername(), 
				dto.getPassword(),
				passwordEncoder
				);
		
		// 조회된 user가 있다면
		if(user != null) {
			// 토큰을 만든다
			final String token = tokenProvider.create(user);
			
			// 인증에 성공한 경우 유저정보를 UserDTO로 변환하여 응답에 사용
			final UserDTO responseUserDTO = UserDTO.builder()
												.id(user.getId())
												.username(user.getUsername())
												.token(token)
												.build();
			return ResponseEntity.ok().body(responseUserDTO);
		} else {
			// 조회된 유저가 없거나, 인증에 실패한 경우 에러 메시지를 포함한 ResponseDTO를 반환한다
			ResponseDTO responseDTO = ResponseDTO.builder()
										.error("Login Failed")
										.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
}
