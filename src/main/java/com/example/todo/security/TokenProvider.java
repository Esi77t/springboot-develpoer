package com.example.todo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.todo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenProvider {
	// create()메서드는 JWT라이브러리를 이용해 JWT토큰을 생성
	// 토큰을 생성하는 과정에서 우리가 임의로 지정한 SECRET_KEY를 개인키로 사용
	// validateAndGetUserId()는 토큰을 디코딩, 파싱 및 위조 여부를 확인
	// 이후 우리가 원하는 유저의 ID를 반환
	// 라이브러리 덕분에 JSON을 생성, 서명, 인코딩, 디코딩 파싱하는 작업을 직접 하지 않아도 된다
	// TokenProvider를 작성했으니, 로그인을 하면 create() 메서드를 통해 토큰을 생성하고
	
	// 비밀 키
	private static final String SECRET_KEY = "4b0dc6da6da745d6647e166a12250472f741778272229ec2a5f3523cc39cfbffff5e0fc7999fd8a047a0446182f206fb7f7062097f8dba098e742f03cd4c11da3948fecab2a233c0146f169b20696d4aaad01878d00dfe23cdca1713d1e630829dde0df8f85ab44cf819ea6cef867df3f09e0488188af56a0ece8119be137de505125ccbfcaa64108fcc25a3a5621ebb99286453645bb9fd1e288c18d79524d247d51c95c3f99aefca4a3f50133b5e6ec7c3f364b5a6892106f84b5637e298150fe24414e09f9be1efbf5a0f7ed4304088e657b7bbcf6f9dff05d3f7b8e6603519c3e3453f132a1b83b9a84a7fb797f0375b59950858d4e77eeaeb44e4e142e8a9e9a4fad66b47a730d5fa8ced184de72e42afba3345b8b879baafcc1a74806ed3b00cb177cd27b8236f40b334f6b8f6d8c1f21482e0e4c14226b00ae1187d5f5238375e858f7df3655cf41ae24ece860d941ef7a7c8393e13d182195901add4d69377c249b5c39e26e27aaf536bcc67bfd28c65435f4253a4998b9a4d0de0bbe62095fa52d7b924abb4959bb05472e1ba597868cfbfc2ab739e96eabf93e884e34ada62897ccdcbcef6fcbed9bc4a61a2f9eb1c2146546ea11cb398b2c856b267b2716f15e9017f32acd963a2188863865cc5af3753fda84be3f6adee91d7fad376bf9be1391012b1ec97cf55d616e636f05681baa82976fef854cd71e630ec";
	
	public String create(UserEntity entity) {
		// 토큰 만료 시간
		// 현재 시각 + 1일
		// Instant클래스 : 타임스탬프를 찍는다
		// plus() : 더할 양
		// ChronoUnit열거형의 DAYS 일 단위를 의미
		Date expiryDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));
		
		// JWT 토큰을 생성
		return Jwts.builder()
				// header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
				.signWith(SignatureAlgorithm.HS512,SECRET_KEY)	// 헤더 + 서명 알고리즘 설정
				.setSubject(entity.getId()) 	// sub 클레임 : 사용자 고유 ID
				.setIssuer("todo app")	// iss 클레임 : 토큰 발급자
				.setIssuedAt(new Date())	// iat 클레임 : 발급 시각
				.setExpiration(expiryDate)	// exp 클레임 : 만료 시각
				.compact();	// 최종 직렬화 된 토큰 문자열 반환
	}
	
	public String validateAndGetUserId(String token) {
		Claims claims = Jwts.parser()
							.setSigningKey(SECRET_KEY)	// 서명 검증용 키 설정
							.parseClaimsJws(token)		// 토큰 파싱 및 서명 검증
							.getBody();					// 내부 페이로드(Claims) 획득
		
		return claims.getSubject();		// sub 클레임(사용자 ID) 반환
	}
}
