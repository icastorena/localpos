package com.pds.localpos.userservice.mapper;

import com.pds.localpos.userservice.dto.StoreDTO;
import com.pds.localpos.userservice.model.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public static StoreDTO toDTO(Store store) {
        if (store == null) return null;
        return new StoreDTO(
                store.getId(),
                store.getCode(),
                store.getName(),
                store.getAddress(),
                store.getCreatedAt(),
                store.getUpdatedAt()
        );
    }

    public static Store toEntity(StoreDTO dto) {
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
