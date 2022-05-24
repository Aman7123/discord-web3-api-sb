package com.aaronrenner.spring.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.aaronrenner.spring.exceptions.ResourceNotFoundException;
import com.aaronrenner.spring.models.*;
import com.aaronrenner.spring.services.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	// Error messages
	private final String notFoundException = "User with the ID: %s was not found";

	// Endpoints
	private final String CONTENT_TYPE = "application/json";
	private final String BASE_PATH    = "/users";
	private final String WITH_USER_ID = "/{userDiscordId}";
	private final String WALLET_PATH  = "/wallets/{walletAddress}";

	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(
			method = RequestMethod.POST,
			value  = BASE_PATH,
			consumes = CONTENT_TYPE,
			produces = CONTENT_TYPE)
	public void createUsers(@RequestBody User u) throws Exception {
		userService.createUsers(u);
	}
	
	@GetMapping(path = BASE_PATH, produces = CONTENT_TYPE)
	public Page<User> readAllUsers(@RequestParam Optional<Integer> page, @RequestParam Optional<Integer> limit) {
		int getPage = (page.isPresent()) ? page.get() : 0;
		int withCount = (limit.isPresent()) ? limit.get() : 100;
		return userService.readAll(getPage, withCount);
	}
	
	@GetMapping(path = BASE_PATH+WITH_USER_ID, produces = CONTENT_TYPE)
	public User readUsers(@PathVariable long userDiscordId) {
		return userService.readUsers(userDiscordId)
				 .orElseThrow(() -> new ResourceNotFoundException(String.format(notFoundException, userDiscordId)));
	}
	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@PatchMapping(path = BASE_PATH+WITH_USER_ID)
	public void updateUsers(@PathVariable long userDiscordId, @RequestBody User u) {
		u.setDiscordId(userDiscordId);
		userService.updateUsers(u);
	}
	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping(path = BASE_PATH+WITH_USER_ID)
	public void deleteUsers(@PathVariable long userDiscordId) {
		userService.deleteUsers(userDiscordId);
	}
	
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(
			method = RequestMethod.POST,
			value  = BASE_PATH+WITH_USER_ID+WALLET_PATH,
			produces = CONTENT_TYPE)
	public void createUsersWallets(@PathVariable long userDiscordId, @PathVariable String walletAddress) throws Exception {
		userService.createUsersWallets(userDiscordId, walletAddress);
	}
	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping(path = BASE_PATH+WITH_USER_ID+WALLET_PATH)
	public void deleteUsers(@PathVariable long userDiscordId, @PathVariable String walletAddress) {
		userService.deleteUsersWallets(userDiscordId, walletAddress);
	}
}
