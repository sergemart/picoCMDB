package ru.sergm.picocmdb.domain;

import javax.persistence.*;

@Entity
public class Role {

	@Id
	private String id;
    private String description;
	private boolean isSystem;


	public String getId() {
        return id;
    }

    public void setId(String name) {
        this.id = name;
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

