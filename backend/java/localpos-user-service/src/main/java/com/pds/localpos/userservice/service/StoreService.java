package com.pds.localpos.userservice.service;

import com.pds.localpos.userservice.dto.response.StoreResponse;

import java.util.Set;

public interface StoreService {

    Set<StoreResponse> getAllStores();
}
