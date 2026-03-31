
package com.plagiarism.plagiarismchecker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.plagiarism.plagiarismchecker.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}