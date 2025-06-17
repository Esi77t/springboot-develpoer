package com.example.todo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.TestRequestBodyDTO;
import com.example.todo.model.ResponseDTO;

// @RestController
// 데이터를 반환하는 컨트롤러로 사용
// JSON이나 XML형식의 데이터를 반환한다
// @Controller, @ResponseBody 두개의 어노테이션의 결합이다
// @ResponseBody는 메서드의 반환 값을 HTTP ResponseBody로 직렬화 해 클라이언트에게 전달한다
@RestController
@RequestMapping("test")		// test주소로 요청이 들어왔을 때 현재 컨트롤러로 들어올 수 있게 해준다
public class TestController {
	
	@GetMapping("/testGetMapping")		// GET으로 요청이 들어왔을 때 요청을 받아서 아래 메서드를 호출
	public String testController() {
		return "Hello world";
	}
	
	// localhost:10000/users/1
	@GetMapping("/users/{id}")
	// 배열을 통해 여러개의 요청 URI를 받을 수 있다
	public String getUserById(@PathVariable(name="id", required=false) int userId) {
		return "User ID : " + userId;
	}
	
	//	@GetMapping("users/{userId}/orders/{orderId}")
	//	public String getOrderByUserAndOrderId(@PathVariable("userId") Long id, @PathVariable("orderId") Long orderId) {
	//		return "User ID : " + id + ", Order ID : " + orderId; 
	//	}
	
	// 정규식을 써서 변수형식을 제한할 수 있다
	//	@GetMapping("users/{userId:[0-9]{3}}")
	//	public String getOrderByUserAndOrderId(@PathVariable("userId") Long id) {
	//		return "User ID : " + id; `
	//	}
	
	@GetMapping("/users")
	// public String getUserById(@RequestParam Long id)
	// 쿼리스트링의 key와 매개변수의 변수명이 일치한다면 name값을 안줘도 된다
	// public String getUserById(@RequestParam(required=false) Long id)
	// 값을 필수로 넣지 않아도 에러가 나지는 않는다
	// public String getUserById(@RequestParam(defaultValue="0") Long id))
	// 값이 넘어오지 않았을 때 기본 값을 설정할 수 있다
	public String getUserById(@RequestParam(value = "id", defaultValue="0") Long userId) {
		return "User ID : " + userId;
	}
	
	@GetMapping("/search")
	public String search(@RequestParam("query") String query, @RequestParam("page") int page) {
		return "Search query : " + query + ", page : " + page;
	}
	
	@PostMapping("/submitForm")
	public String submitForm(@RequestParam("name") String name, @RequestParam("email") String email) {
		return "Form submitted : Name = " + name + ", Email = " + email;
	}
	
	@GetMapping("/testRequestBody")
	// JSON형식으로 전달되는 데이터를 TestRequestBodyDTO형식으로 만들어서 넣어준다
	// {"id" : 123, "message" :"hello ?"}
	public String testRequestBody(@RequestBody TestRequestBodyDTO dto) {
		return "ID : " + dto.getId() + ", Message : " + dto.getMessage();
	}
	
	@GetMapping("/testResponseBody")
	public ResponseDTO<String> testResponseBody() {
		List<String> list = Arrays.asList("하나", "둘", "셋");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		// String error, List<String> list가 담겨있는 ResponseDTO 객체를 반환
		return response;
	}
	
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testResponseEntity() {
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseEntity, And you got 400");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.badRequest().body(response);
	}
	// ResponseDTO를 반환하는 것과 비교했을 때 큰 차이는 없지만
	// 단지 헤더와 HTTPStatus를 조작할 수 있다는 점이 다르다
}
