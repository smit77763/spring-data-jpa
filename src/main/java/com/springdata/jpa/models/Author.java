package com.springdata.jpa.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
		name = "author_table" // This is the name of the table in the database.
)
public class Author {
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE
	)
//	@SequenceGenerator(
//			name = "author_sequence",
//			sequenceName = "author_sequence",
//			allocationSize = 1
//	)
//	@TableGenerator(
//			name = "author_sequence",
//			table = "author_sequence",
//			pkColumnName = "seq_name"
//	)
	
	private Integer id;
	
	@Column(
			name = "f_name",
			length = 40
	)
	private String firstName;
	private String lastName;
	
	@Column(unique = true)
	private String email;
	private  int age;
	
	@ManyToMany(mappedBy = "authors")
	private List<Course> courses;
}
