package com.example.todo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
	
	// AWS 로드 밸런서는 루트경로인 "/"에 http요청을 보내서 애플리케이션이 동작하는지 확인한다
	// eb는 이를 기반으로 애플리케이션이 실행 중인지 주의가 필요한 상태인지 확인한다
	@GetMapping("/")
	public String healthCheck() {
		return "The service is up and running...";
	}
}
