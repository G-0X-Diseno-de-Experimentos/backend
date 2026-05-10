package com.textilflow.platform.profiles.application.acl;

import com.textilflow.platform.profiles.domain.model.aggregates.Businessman;
import com.textilflow.platform.profiles.domain.model.aggregates.Supplier;
import com.textilflow.platform.profiles.domain.model.queries.GetBusinessmanByUserIdQuery;
import com.textilflow.platform.profiles.domain.model.queries.GetSupplierByUserIdQuery;
import com.textilflow.platform.profiles.domain.services.BusinessmanQueryService;
import com.textilflow.platform.profiles.domain.services.SupplierQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfilesContextFacadeImplTest {

    @Mock private BusinessmanQueryService businessmanQueryService;
    @Mock private SupplierQueryService supplierQueryService;

    @InjectMocks private ProfilesContextFacadeImpl facade;

    @Test
    @DisplayName("getBusinessmanByUserId debe retornar el ID si existe (AAA)")
    void getBusinessmanByUserId_ShouldReturnId() {
        // Arrange
        Businessman businessmanMock = mock(Businessman.class);
        when(businessmanMock.getUserId()).thenReturn(10L);
        when(businessmanQueryService.handle(any(GetBusinessmanByUserIdQuery.class)))
                .thenReturn(Optional.of(businessmanMock));

        // Act
        Long result = facade.getBusinessmanByUserId(10L);

        // Assert
        assertEquals(10L, result);
        verify(businessmanQueryService).handle(any(GetBusinessmanByUserIdQuery.class));
    }

    @Test
    @DisplayName("getSupplierByUserId debe retornar el ID si existe (AAA)")
    void getSupplierByUserId_ShouldReturnId() {
        // Arrange
        Supplier supplierMock = mock(Supplier.class);
        when(supplierMock.getUserId()).thenReturn(20L);
        when(supplierQueryService.handle(any(GetSupplierByUserIdQuery.class)))
                .thenReturn(Optional.of(supplierMock));

        // Act
        Long result = facade.getSupplierByUserId(20L);

        // Assert
        assertEquals(20L, result);
        verify(supplierQueryService).handle(any(GetSupplierByUserIdQuery.class));
    }

    @Test
    @DisplayName("hasBusinessmanProfile y hasSupplierProfile deben validar existencia (AAA)")
    void existenceCheck_Methods() {
        // Arrange
        when(businessmanQueryService.handle(any(GetBusinessmanByUserIdQuery.class)))
                .thenReturn(Optional.of(new Businessman()));
        when(supplierQueryService.handle(any(GetSupplierByUserIdQuery.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertTrue(facade.hasBusinessmanProfile(10L));
        assertFalse(facade.hasSupplierProfile(20L));
    }

    @Test
    @DisplayName("getCompanyNameByUserId debe priorizar Businessman, luego Supplier (AAA)")
    void getCompanyNameByUserId_Priorities() {
        // Arrange - Caso 1: Está en Businessman
        Businessman bMock = mock(Businessman.class);
        when(bMock.getCompanyNameValue()).thenReturn("Corp B");
        when(businessmanQueryService.handle(any(GetBusinessmanByUserIdQuery.class)))
                .thenReturn(Optional.of(bMock));

        // Act
        String name1 = facade.getCompanyNameByUserId(100L);
        // Assert 1
        assertEquals("Corp B", name1);
        verifyNoInteractions(supplierQueryService);

        // Reset
        reset(businessmanQueryService);

        // Arrange
        when(businessmanQueryService.handle(any(GetBusinessmanByUserIdQuery.class)))
                .thenReturn(Optional.empty());
        Supplier sMock = mock(Supplier.class);
        when(sMock.getCompanyNameValue()).thenReturn("Corp S");
        when(supplierQueryService.handle(any(GetSupplierByUserIdQuery.class)))
                .thenReturn(Optional.of(sMock));

        // Act
        String name2 = facade.getCompanyNameByUserId(200L);
        // Assert 2
        assertEquals("Corp S", name2);
    }
}