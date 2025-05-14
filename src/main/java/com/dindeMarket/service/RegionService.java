package com.dindeMarket.service;

import com.dindeMarket.api.payload.RegionRequest;
import com.dindeMarket.db.entity.RegionEntity;
import com.dindeMarket.db.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public RegionEntity createRegion(RegionRequest regionRequest) {
        try {
            RegionEntity region = new RegionEntity();
            region.setName(regionRequest.getName());
            region.setPriceDelivery(regionRequest.getPriceDelivery());
            return regionRepository.save(region);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Region name must be unique", e);
        }
    }

    public RegionEntity updateRegion(Long id, RegionRequest regionRequest) {
        try {
            RegionEntity region = regionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Region not found"));

            region.setName(regionRequest.getName());
            region.setPriceDelivery(regionRequest.getPriceDelivery());
            return regionRepository.save(region);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Region name must be unique", e);
        }
    }

    public Optional<RegionEntity> findById(Long id) {
        return regionRepository.findById(id);
    }

    public Iterable<RegionEntity> findAll() {
        return regionRepository.findAll();
    }

    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }

    @Transactional
    public void deleteRegionsByIds(List<Long> ids) {
        regionRepository.deleteAllById(ids);
    }
}
