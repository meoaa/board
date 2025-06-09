package project.board;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import project.board.member.domain.Member;

@SpringBootTest
class BoardApplicationTests {

	@Test
	void contextLoads() {
		Member member = Member.builder()
				.username("username1")
				.password("1234")
				.email("test@test.com")
				.nickname("nickname")
				.build();
	}

}
