package com.pds.localpos.orderservice.client;

import com.pds.localpos.orderservice.dto.request.AdjustStockRequest;
import com.pds.localpos.orderservice.dto.response.InventoryResponse;
import com.pds.localpos.orderservice.dto.request.UpdateStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", url = "${inventory-service.url}")
public interface InventoryClient {

    @GetMapping("/{storeId}/{productId}")
    InventoryResponse getByProductAndStore(
            @PathVariable("productId") String productId,
            @PathVariable("storeId") String storeId
    );

    @PostMapping("/increase")
    void increaseStock(@RequestBody AdjustStockRequest request);

    @PostMapping("/decrease")
    void decreaseStock(@RequestBody AdjustStockRequest request);

    @PutMapping("/update")
    InventoryResponse updateStock(@RequestBody UpdateStockRequest request);
}
