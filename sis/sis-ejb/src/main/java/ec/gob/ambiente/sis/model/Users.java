package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: Users
 *
 */
@Entity
@Table(name = "users", schema = "sis")
public class Users implements Serializable {

	
	private static final long serialVersionUID = 1L;

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
