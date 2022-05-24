package com.aaronrenner.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aaronrenner.spring.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	User    findByDiscordId(long discordId);
	Integer deleteByDiscordId(long discordId);
	Boolean existsByDiscordId(long discordId);
	
}
