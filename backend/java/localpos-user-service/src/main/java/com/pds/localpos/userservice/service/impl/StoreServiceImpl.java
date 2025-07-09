package com.pds.localpos.userservice.service.impl;

import com.pds.localpos.userservice.dto.StoreDTO;
import com.pds.localpos.userservice.mapper.StoreMapper;
import com.pds.localpos.userservice.repository.StoreRepository;
import com.pds.localpos.userservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    @Override
    public Set<StoreDTO> getAllStores() {
        return storeRepository.findAll().stream()
                .map(StoreMapper::toDTO)
                .collect(Collectors.toSet());
    }
}
