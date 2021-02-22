package ztysdm.crypto.trade.model;

import java.util.Optional;

import org.springframework.data.annotation.Id;

public abstract class Persisted {

	@Id
	private String id;
	
	public Optional<String> getID() {
		return Optional.ofNullable(id);
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
