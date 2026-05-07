package com.textilflow.platform.profiles.application.acl;

import com.textilflow.platform.profiles.domain.model.queries.GetBusinessmanByUserIdQuery;
import com.textilflow.platform.profiles.domain.model.queries.GetSupplierByUserIdQuery;
import com.textilflow.platform.profiles.domain.services.BusinessmanQueryService;
import com.textilflow.platform.profiles.domain.services.SupplierQueryService;
import com.textilflow.platform.profiles.interfaces.acl.ProfilesContextFacade;
import org.springframework.stereotype.Service;

/**
 * Profiles context facade implementation
 */
@Service
public class ProfilesContextFacadeImpl implements ProfilesContextFacade {

    private final BusinessmanQueryService businessmanQueryService;
    private final SupplierQueryService supplierQueryService;

    public ProfilesContextFacadeImpl(BusinessmanQueryService businessmanQueryService,
                                     SupplierQueryService supplierQueryService) {
        this.businessmanQueryService = businessmanQueryService;
        this.supplierQueryService = supplierQueryService;
    }

    @Override
    public Long getBusinessmanByUserId(Long userId) {
        var businessman = businessmanQueryService.handle(new GetBusinessmanByUserIdQuery(userId));
        return businessman.map(b -> b.getUserId()).orElse(null);
    }

    @Override
    public Long getSupplierByUserId(Long userId) {
        var supplier = supplierQueryService.handle(new GetSupplierByUserIdQuery(userId));
        return supplier.map(s -> s.getUserId()).orElse(null);
    }

    @Override
    public boolean hasBusinessmanProfile(Long userId) {
        return businessmanQueryService.handle(new GetBusinessmanByUserIdQuery(userId)).isPresent();
    }

    @Override
    public boolean hasSupplierProfile(Long userId) {
        return supplierQueryService.handle(new GetSupplierByUserIdQuery(userId)).isPresent();
    }

    @Override
    public String getCompanyNameByUserId(Long userId) {
        // Try businessman first
        var businessman = businessmanQueryService.handle(new GetBusinessmanByUserIdQuery(userId));
        if (businessman.isPresent()) {
            return businessman.get().getCompanyNameValue();
        }

        // Try supplier
        var supplier = supplierQueryService.handle(new GetSupplierByUserIdQuery(userId));
        if (supplier.isPresent()) {
            return supplier.get().getCompanyNameValue();
        }

        return null;
    }
}