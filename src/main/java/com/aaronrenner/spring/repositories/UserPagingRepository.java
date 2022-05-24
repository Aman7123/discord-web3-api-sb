package com.aaronrenner.spring.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.aaronrenner.spring.models.User;

public interface UserPagingRepository extends PagingAndSortingRepository<User, Long> {}
