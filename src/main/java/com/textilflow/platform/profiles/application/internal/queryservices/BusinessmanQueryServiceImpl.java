package com.textilflow.platform.profiles.application.internal.queryservices;

import com.textilflow.platform.profiles.domain.model.aggregates.Businessman;
import com.textilflow.platform.profiles.domain.model.queries.GetAllBusinessmenQuery;
import com.textilflow.platform.profiles.domain.model.queries.GetBusinessmanByUserIdQuery;
import com.textilflow.platform.profiles.domain.services.BusinessmanQueryService;
import com.textilflow.platform.profiles.infrastructure.persistence.jpa.repositories.BusinessmanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Businessman query service implementation
 */
@Service
public class BusinessmanQueryServiceImpl implements BusinessmanQueryService {

    private final BusinessmanRepository businessmanRepository;

    public BusinessmanQueryServiceImpl(BusinessmanRepository businessmanRepository) {
        this.businessmanRepository = businessmanRepository;
    }

    @Override
    public Optional<Businessman> handle(GetBusinessmanByUserIdQuery query) {
        return businessmanRepository.findByUserId(query.userId());
    }

    @Override
    public List<Businessman> handle(GetAllBusinessmenQuery query) {
        return businessmanRepository.findAll();
    }
}
