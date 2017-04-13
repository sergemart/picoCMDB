package com.github.sergemart.picocmdb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

import java.util.Set;


@Entity
public class ConfigurationItemType {

	@Id
	private String id;
    private String description;
	@JsonIgnore // to break the circular dependency during deserialization to avoid loops and stack overflows
    @OneToMany(mappedBy = "type", // the field on the other side
			//cascade = CascadeType.ALL, // no state changes propagate to children
			fetch = FetchType.LAZY)
	private Set<ConfigurationItem> configurationItems;


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
	
    public Set<ConfigurationItem> getConfigurationItems() {
		return configurationItems;
	}


	// Overrides; have no 'configurationItems' references in order to avoid loops and stack overflows

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


	@Override
	public String toString() {
		return "ConfigurationItemType{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}

