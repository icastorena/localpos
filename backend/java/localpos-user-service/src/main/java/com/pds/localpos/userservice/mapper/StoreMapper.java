package com.pds.localpos.userservice.mapper;

import com.pds.localpos.userservice.dto.response.StoreResponse;
import com.pds.localpos.userservice.model.Store;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StoreMapper {

    public static StoreResponse toDTO(Store store) {
        if (store == null) return null;
        return new StoreResponse(
                store.getId(),
                store.getCode(),
                store.getName(),
                store.getAddress(),
                store.getCreatedAt(),
                store.getUpdatedAt()
        );
    }

    public static Store toEntity(StoreResponse dto) {
        if (dto == null) return null;
        Store store = new Store();
        store.setId(dto.id());
        store.setCode(dto.code());
        store.setName(dto.name());
        store.setAddress(dto.address());
        store.setCreatedAt(dto.createdAt());
        store.setUpdatedAt(dto.updatedAt());
        return store;
    }
}
