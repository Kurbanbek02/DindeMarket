package com.dindeMarket.service;

import com.dindeMarket.api.payload.PhotoResponse;
import com.dindeMarket.api.payload.ProductResponse;
import com.dindeMarket.db.entity.PhotoEntity;
import com.dindeMarket.db.entity.ProductEntity;
import com.dindeMarket.db.repository.PhotoRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

    public PhotoResponse convertToResponse(PhotoEntity entity) {
         PhotoResponse response = new PhotoResponse();
         response.setId(entity.getId());
         response.setUrl(entity.getUrl());
        return response;
    }
}
