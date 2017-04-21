package com.github.sergemart.picocmdb.system;

import java.io.Serializable;

import com.github.sergemart.picocmdb.domain.ManagedArea;
import com.github.sergemart.picocmdb.domain.ConfigurationItemRelationType;


/**
 * Represents effective settings for a current user session.
 */
public class CurrentSessionSettings
		implements Serializable {

	private static final long serialVersionUID = -286022453858677753L;

	private ManagedArea currentManagedArea;

	private ConfigurationItemRelationType currentCiRelationType;


	// Getters/setters

	public ManagedArea getCurrentManagedArea() {
		return currentManagedArea;
	}

	public void setCurrentManagedArea(ManagedArea currentManagedArea) {
		this.currentManagedArea = currentManagedArea;
	}

	public ConfigurationItemRelationType getCurrentCiRelationType() {
		return currentCiRelationType;
	}

	public void setCurrentCiRelationType(ConfigurationItemRelationType currentCiRelationType) {
		this.currentCiRelationType = currentCiRelationType;
	}

}
