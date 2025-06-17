package com.example.todo.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todo.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{
	UserEntity findByUsername(String username);	// username값으로 데이터를 한 건 찾아 반환한다
	Boolean existsByUsername(String username);	// username이 존재하면 true, 아니면 false
	UserEntity findByUsernameAndPassword(String username, String password);
	// username과 password를 기준으로 데이터를 한 건 조회한다
	// And가 사용되었으므로, 두 필드를 모두 만족하는 데이터를 찾는 조건으로 쿼리가 생성된다
}
