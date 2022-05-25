package com.aaronrenner.spring.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.aaronrenner.spring.exceptions.DuplicateException;
import com.aaronrenner.spring.exceptions.ResourceNotFoundException;
import com.aaronrenner.spring.models.User;
import com.aaronrenner.spring.models.Wallet;
import com.aaronrenner.spring.repositories.UserPagingRepository;
import com.aaronrenner.spring.repositories.UserRepository;
import com.aaronrenner.spring.repositories.WalletRepository;
import com.aaronrenner.spring.utils.WalletUtils;
import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
	
	// Assets for Users
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserPagingRepository userPagingRepository;
	private final String userNotFoundException = "User with the ID %s was not found";
	private final String userDuplicateException = "User with the ID %s already exists";
	
	// Assets for Wallets
	@Autowired
	WalletRepository walletRepository;
	private final String walletNotFoundException = "Wallet with the value %s was not found";
	private final String walletDuplicateException = "Wallet with the value %s already exists";
	
	private WalletUtils wUtils = new WalletUtils();;
	
	@Transactional
	public void createUsers(User u) {
		long discordIdForUpdate = u.getDiscordId();
		// Check existence
		checkUserNotExists(discordIdForUpdate);
		// Run function
		userRepository.save(u);
	}
	
	@Transactional
	public Page<User> readAll(int page, int limit) {
		Pageable where = PageRequest.of(page, limit);
		return userPagingRepository.findAll(where);
	}
	
	@Transactional
	public Optional<User> readUsers(long userDiscordId) {
		return userRepository.findByDiscordId(userDiscordId);
	}
	
	@Transactional
	public void updateUsers(User u) {
		long discordIdForUpdate = u.getDiscordId();
		// Check existence
		checkUserExists(discordIdForUpdate);
		// Run function
		User existingUser = readUsers(discordIdForUpdate).get();
		if(nonNull(u.getName()))  existingUser.setName(u.getName());
		if(nonNull(u.getImage())) existingUser.setImage(u.getName());
		userRepository.save(existingUser);
	}

	@Transactional
	public void deleteUsers(long userDiscordId) {
		// Check existence
		checkUserExists(userDiscordId);
		// Run function
		userRepository.deleteByDiscordId(userDiscordId);
	}
	
	@Transactional
	public List<Wallet> readUsersWallets(long userDiscordId, String type) {
		// Check existence
		checkUserExists(userDiscordId);
		// Run function
		User existingUser = readUsers(userDiscordId).get();
		// If no filter
		if(isNull(type)) return existingUser.getWallets();
		// Else continue
		List<Wallet> returnData = new ArrayList<>();
		for(Wallet w : existingUser.getWallets()) {
			if(w.getType().equalsIgnoreCase(type)) {
				returnData.add(w);
			}
		}
		return returnData;
	}
	
	@Transactional
	public void createUsersWallets(long userDiscordId, String walletAddress) {
		// Check existence
		checkUserExists(userDiscordId);
		checkUserWalletNotExists(walletAddress);
		// Run function
		User existingUserLookup = readUsers(userDiscordId).get();
		existingUserLookup.addWallet(walletAddress, wUtils.checkWalletType(walletAddress));
		userRepository.save(existingUserLookup);
	}
	
	@Transactional
	public void deleteUsersWallets(long userDiscordId, String walletAddress) {
		// Check existence
		checkUserExists(userDiscordId);
		checkUserWalletExists(walletAddress);
		// Run function
		User existingUserLookup = readUsers(userDiscordId).get();
		existingUserLookup.removeWallet(walletAddress);
		userRepository.save(existingUserLookup);
		walletRepository.deleteByAddressAllIgnoreCase(walletAddress);
	}
	
	/*
	 * Helper function checks for existence in the database
	 */
	private void checkUserExists(long userDiscordId) {
		// Check existence
		if(!userRepository.existsByDiscordId(userDiscordId))
			throw new ResourceNotFoundException(String.format(userNotFoundException, userDiscordId));
	}
	
	/*
	 * Helper function checks for no existence in the database
	 */
	private void checkUserNotExists(long userDiscordId) {
		// Check existence
		if(userRepository.existsByDiscordId(userDiscordId))
			throw new DuplicateException(String.format(userDuplicateException, userDiscordId));
	}
	
	/*
	 * Helper function checks for existence in the database
	 */
	private void checkUserWalletExists(String walletAddress) {
		// Check existence
		if(!walletRepository.existsByAddressAllIgnoreCase(walletAddress))
			throw new ResourceNotFoundException(String.format(walletNotFoundException, walletAddress));
	}
	
	/*
	 * Helper function checks for existence in the database
	 */
	private void checkUserWalletNotExists(String walletAddress) {
		// Check existence
		if(walletRepository.existsByAddressAllIgnoreCase(walletAddress))
			throw new DuplicateException(String.format(walletDuplicateException, walletAddress));
	}

}
