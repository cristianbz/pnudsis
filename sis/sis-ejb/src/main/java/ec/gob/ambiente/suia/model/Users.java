package ec.gob.ambiente.suia.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: Users
 *
 */
@Entity
@Table(name = "users", schema = "public")
public class Users {

	
	

	@Getter
	@Setter
	@Column(name = "user_id")
	@Id
	@SequenceGenerator(name = "USERS_GENERATOR", initialValue = 1, sequenceName = "users_user_id_seq", schema = "sis")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_GENERATOR")
	private Integer userId;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
	private List<AdvanceExecutionSafeguards> advanceExecutionSaveguardsList;
	
	
   
}
