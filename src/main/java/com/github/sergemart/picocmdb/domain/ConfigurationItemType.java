package com.github.sergemart.picocmdb.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConfigurationItemType {

	@Id
	private String id;
    private String description;


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


	// Overrides

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ConfigurationItemType that = (ConfigurationItemType) o;

		if (!id.equals(that.id)) return false;
		return description != null ? description.equals(that.description) : that.description == null;
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + (description != null ? description.hashCode() : 0);
		return result;
	}





}

