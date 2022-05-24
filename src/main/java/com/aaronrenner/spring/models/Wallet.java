package com.aaronrenner.spring.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString(includeFieldNames = true)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"id"})
public class Wallet {
	
	@Id
	@GeneratedValue
	private long   id;
	@Column(nullable = false, unique = true)
	private String address;
	@Column(nullable = false)
	private String type;

	public Wallet() {}
	
	public Wallet(String address) {
		this.address = address;
	}
	
	public Wallet(String address, String type) {
		this.address = address;
		this.type    = type;
	}
	
	@Override
	public boolean equals(Object o) {
		/* Check if o is an instance of Wallet or not
        "null instanceof [type]" also returns false */
      if (!(o instanceof Wallet)) return false;
       
      // typecast o to Wallet so that we can compare data members
      Wallet that = (Wallet) o;
       
      // Compare the names and return accordingly
      return this.address.equalsIgnoreCase(that.getAddress());
	}
}
