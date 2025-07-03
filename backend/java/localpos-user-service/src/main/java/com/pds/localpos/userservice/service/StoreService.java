package com.pds.localpos.userservice.service;

import com.pds.localpos.userservice.dto.StoreDTO;

import java.util.Set;

public interface StoreService {

    Set<StoreDTO> getAllStores();
}
