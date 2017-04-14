package com.github.sergemart.picocmdb.domain;

import javax.persistence.*;

@Entity
public class Role {

	// Attributes

	@Id
	private String id;
    private String description;
	private boolean isSystem;


	// Getters/setters

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


	// Overrides

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Role role = (Role) o;

		if (isSystem != role.isSystem) return false;
		if (!id.equals(role.id)) return false;
		return description != null ? description.equals(role.description) : role.description == null;
	}


	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (isSystem ? 1 : 0);
		return result;
	}


}

