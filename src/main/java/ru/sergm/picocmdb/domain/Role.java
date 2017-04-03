package ru.sergm.picocmdb.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
//@Table(name="role")
public class Role
		//implements Serializable
{

	//private static final long serialVersionUID = 1L;

	@Id
	private String id;
    private String description;
	private boolean isSystem;


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public boolean isSystem() {
		return isSystem;
	}

	public void setSystem(boolean system) {
		isSystem = system;
	}

}

