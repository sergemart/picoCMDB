package com.github.sergemart.picocmdb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

import java.util.Set;


@Entity
@DynamicUpdate // to put only modified columns into SQL 'UPDATE' statement
public class ManagedArea {

	// Attributes

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // to conform with Heroku Postgres
	private Long id;
	private String name;
    private String description;
	@JsonIgnore // to break the circular dependency during deserialization to avoid loops and stack overflows
    @ManyToMany(cascade = CascadeType.ALL) // all state changes propagate to children
	@JoinTable(
			name = "ci_marea_link",
			joinColumns = @JoinColumn(
					name = "marea_id",
					referencedColumnName = "id"
			),
			inverseJoinColumns = @JoinColumn(
					name = "ci_id",
					referencedColumnName = "id"
			)
	)
    private Set<ConfigurationItem> configurationItems;


	// Getters/setters

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

	public Set<ConfigurationItem> getConfigurationItems() {
		return configurationItems;
	}


	// Overrides; have no referenced attributes involved in order to avoid loops and stack overflows

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ManagedArea that = (ManagedArea) o;

		if (!id.equals(that.id)) return false;
		if (!name.equals(that.name)) return false;
		return description != null ? description.equals(that.description) : that.description == null;
	}


	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + (description != null ? description.hashCode() : 0);
		return result;
	}

}

