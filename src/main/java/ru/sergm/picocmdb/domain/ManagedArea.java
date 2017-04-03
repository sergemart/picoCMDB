package ru.sergm.picocmdb.domain;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
//@Table(name="managed_area")
@DynamicUpdate // to put only modified columns into SQL 'UPDATE' statement
public class ManagedArea
		//implements Serializable
{

	//private static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // to conform with Heroku Postgres
	private Long id;
	private String name;
    private String description;


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

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

