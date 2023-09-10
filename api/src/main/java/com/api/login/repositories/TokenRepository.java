package com.api.login.repositories;

import com.api.login.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {
    @Query(value = """
      select t from token t inner join user u\s
      on t.user.id = u.id\s
      where u.id = :id\s
      and t.tokenType = 'BEARER'\s
      and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Long id);
    Optional<Token> findByToken(String token);
}
