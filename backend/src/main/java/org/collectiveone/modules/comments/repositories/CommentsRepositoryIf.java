package org.collectiveone.modules.comments.repositories;

import java.util.List;
import java.util.UUID;

import org.collectiveone.modules.comments.Comments;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CommentsRepositoryIf extends CrudRepository<Comments, UUID> {

	Comments findById(UUID id);
	
	/*@Query("SELECT init FROM Initiative init JOIN init.members mem WHERE mem.user.c1Id = ?1")
	List<Initiative> findOfMember(UUID memberId);
	
	@Query("SELECT rels.ofInitiative from InitiativeRelationship rels WHERE rels.initiative.id = ?1 AND rels.type = ?2")
	Initiative findOfInitiativesWithRelationship(UUID initiativeId, InitiativeRelationshipType type);
		
	
	@Query("SELECT rels.initiative from InitiativeRelationship rels WHERE rels.ofInitiative.id = ?1 AND rels.type = ?2")
	List<Initiative> findInitiativesWithRelationship(UUID initiativeId, InitiativeRelationshipType type);
	
	@Query("SELECT init FROM Initiative init WHERE lower (init.meta.name) LIKE %?1%")
	List<Initiative> searchBy(String q);
	
	Initiative findByTokenType_Id(UUID tokenTypeId);*/
	
}
