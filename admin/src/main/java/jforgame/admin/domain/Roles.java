package jforgame.admin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Roles {
	@Id
	private Long id;
	@Column
	private String name;

	public Roles() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Roles(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
