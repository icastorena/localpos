package com.pds.localpos.userservice.service.impl;

import com.pds.localpos.userservice.dto.response.StoreResponse;
import com.pds.localpos.userservice.mapper.StoreMapper;
import com.pds.localpos.userservice.repository.StoreRepository;
import com.pds.localpos.userservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public Set<StoreResponse> getAllStores() {
        log.info("Fetching all stores");
        Set<StoreResponse> stores = storeRepository.findAll().stream()
                .map(StoreMapper::toDTO)
                .collect(Collectors.toSet());
        log.info("Fetched {} stores", stores.size());
        return stores;
    }
}
