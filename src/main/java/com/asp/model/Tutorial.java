package com.asp.model;

import org.hibernate.validator.constraints.Length;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name="tutorials")
public class Tutorial {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	private long id;
	
	@Column(name = "title")
	@Getter @Setter 
	@NotEmpty(message = "error.title.empty")
	@Length(max = 255, message = "error.title.length")
	private String title;
	
	@Column(name = "description")
	@Getter @Setter
	@NotEmpty(message = "error.description.empty")
	@Length(max = 255, message = "error.description.length")
	private String description;
	
	@Column(name = "published")
	@Getter @Setter
	private boolean published = false;

	public Tutorial(String title, String description, boolean published) {
		this.title = title;
		this.description = description;
		this.published = published;
	}

	@Override
	public String toString() {
		return "Tutorial [id=" + id + ", title=" + title + ", desc=" + description + ", published=" + published + "]";
	}
}
