package com.aaronrenner.spring.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString(includeFieldNames = true)
@JsonIgnoreProperties(ignoreUnknown = true, value = {"id"})
public class User {
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	@Column(nullable = false)
	private String name;
	
	@NotNull
	@Column(nullable = false)
	private String image;
	
	@NotNull
	@Column(
		nullable = false, 
		name = "discord_id", 
		unique = true)
	private long discordId;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private List<Wallet> wallets = new ArrayList<>();
	
	public User() { }

	public User(String name, String image, long discordId) {
		this.name = name;
		this.image = image;
		this.discordId = discordId;
	}
	
	public boolean addWallet(String wallet, String type) {
		return this.wallets.add(new Wallet(wallet, type));
	}
	
	public boolean removeWallet(String wallet) {
		return this.wallets.remove(new Wallet(wallet));
	}
}
