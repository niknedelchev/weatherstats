package com.group9.weatherstats.model;

public enum Role {
	
	ADMIN("ADMIN"), REGULAR("REGULAR"),  ;

	
	private String userType;

	Role(String userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return userType;
	}
}
