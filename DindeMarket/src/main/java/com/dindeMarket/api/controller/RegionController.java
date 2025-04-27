package com.dindeMarket.api.controller;

import com.dindeMarket.api.payload.RegionRequest;
import com.dindeMarket.db.entity.RegionEntity;
import com.dindeMarket.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/regions")
//@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
@CrossOrigin
public class RegionController {

    @Autowired
    private RegionService regionService;

    @PostMapping
    public ResponseEntity<RegionEntity> createRegion(@RequestBody RegionRequest regionRequest) {
        try {
            RegionEntity createdRegion = regionService.createRegion(regionRequest);
            return ResponseEntity.ok(createdRegion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionEntity> updateRegion(@PathVariable Long id, @RequestBody RegionRequest regionRequest) {
        try {
            RegionEntity updatedRegion = regionService.updateRegion(id, regionRequest);
            return ResponseEntity.ok(updatedRegion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionEntity> getRegionById(@PathVariable Long id) {
        Optional<RegionEntity> region = regionService.findById(id);
        return region.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Iterable<RegionEntity>> getAllRegions() {
        Iterable<RegionEntity> regions = regionService.findAll();
        return ResponseEntity.ok(regions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteRegions(@RequestBody List<Long> ids) {
        regionService.deleteRegionsByIds(ids);
        return ResponseEntity.noContent().build();
    }
}
