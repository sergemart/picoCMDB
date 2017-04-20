package com.github.sergemart.picocmdb.domain;

import javax.persistence.*;


@Entity
@Table(name = "ci_ci_link_type")
public class ConfigurationItemRelationType {

	// Attributes

	@Id
	private String id;
	private String description;


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


	// Overrides; have no referenced attributes involved in order to avoid loops and stack overflows

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ConfigurationItemRelationType that = (ConfigurationItemRelationType) o;

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
		return "ConfigurationItemRelationType{" +
				"id='" + id + '\'' +
				", description='" + description + '\'' +
				'}';
	}

}
