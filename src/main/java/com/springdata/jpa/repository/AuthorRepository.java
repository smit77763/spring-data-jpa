package com.springdata.jpa.repository;

import com.springdata.jpa.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer>{
	
//	List<Author> findAllByFirstName(String firstName);
//	List<Author> findAllByFirstNameIgnoreCase(String firstName);
//	List<Author> findAllByFirstNameStartsWithIgnoreCase(String firstName);
//	List<Author> findAllByAgeBetween(int min, int max);

	@Modifying
	@Query("update Author a set a.firstName = :name where a.id = :id")
	void updateAuthorName(String name, int id);
		
	
//	Author updateAuthorNameById(String name, int id);
	
	@Modifying
	@Query("update Author a set a.age = :age")
	void updateAllAuthorAge(int age);
	
//	int updateAllAuthorAge(int age);
	
	
	
	@Modifying
	@Transactional
	@Query("update Author a set a.email = :email where a.id = :id")
	void updateAuthorEmail(String email, int id);
	
//	@Override
	int deleteAuthorsById();
	
	
//	Author updateAuthorEmailById(String email, int id);
	

	//update author
}
