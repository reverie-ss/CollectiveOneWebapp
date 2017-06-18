package org.collectiveone.model.support;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.collectiveone.model.basic.AppUser;
import org.collectiveone.model.basic.DecisionRealm;
import org.collectiveone.model.enums.DecisionMakerRole;
import org.collectiveone.web.dto.DecisionMakerDto;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity( name = "decision_makers")
public class DecisionMaker {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator",
		parameters = { @Parameter( name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	@ManyToOne
	private DecisionRealm realm;
	
	@ManyToOne 
	private AppUser user;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private DecisionMakerRole role;

	public DecisionMakerDto toDto() {
		DecisionMakerDto dto = new DecisionMakerDto();
		
		dto.setId(id.toString());
		dto.setUser(user.toDto());
		dto.setRole(role.toString());
	
		return dto;
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public DecisionRealm getRealm() {
		return realm;
	}

	public void setRealm(DecisionRealm realm) {
		this.realm = realm;
	}
	
	public DecisionMakerRole getRole() {
		return role;
	}

	public void setRole(DecisionMakerRole role) {
		this.role = role;
	}
	
	
}