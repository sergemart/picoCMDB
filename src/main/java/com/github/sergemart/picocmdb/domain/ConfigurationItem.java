package com.github.sergemart.picocmdb.domain;

import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@Entity
@DynamicUpdate // to put only modified columns into SQL 'UPDATE' statement
public class ConfigurationItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // to conform with Heroku Postgres
	private Long id;
	private String name;
    private String description;
	@ManyToOne
	@JoinColumn(name = "ci_type_id")
    private ConfigurationItemType type;


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

	public ConfigurationItemType getType() {
		return type;
	}

	public void setType(ConfigurationItemType type) {
		this.type = type;
	}


	// Overrides

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ConfigurationItem that = (ConfigurationItem) o;

		if (!id.equals(that.id)) return false;
		if (!name.equals(that.name)) return false;
		if (description != null ? !description.equals(that.description) : that.description != null) return false;
		return type.equals(that.type);
	}


	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + type.hashCode();
		return result;
	}


}

