package com.textilflow.platform.profiles.application.internal.outboundservices;

import com.textilflow.platform.iam.interfaces.acl.IamContextFacade;
import com.textilflow.platform.iam.interfaces.acl.model.UserData;
import com.textilflow.platform.profiles.application.internal.outboundservices.acl.ExternalIamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExternalIamServiceTest {

    @Mock
    private IamContextFacade iamContextFacade;

    @InjectMocks
    private ExternalIamService service;

    @Test
    @DisplayName("userExists debe delegar correctamente")
    void userExists_ShouldReturnValue() {

        when(iamContextFacade.userExists(1L)).thenReturn(true);

        assertTrue(service.userExists(1L));
    }

    @Test
    @DisplayName("getUserRole debe delegar correctamente")
    void getUserRole_ShouldReturnRole() {

        when(iamContextFacade.getUserRole(1L)).thenReturn("ADMIN");

        assertEquals("ADMIN", service.getUserRole(1L));
    }

    @Test
    @DisplayName("updateUserData debe llamar al facade correctamente")
    void updateUserData_ShouldDelegate() {

        service.updateUserData(1L, "a", "b", "c", "d", "e", "f");

        verify(iamContextFacade).updateUserData(1L, "a", "b", "c", "d", "e", "f");
    }

    @Test
    @DisplayName("getUserData debe retornar data del facade")
    void getUserData_ShouldReturnData() {

        UserData mock = mock(UserData.class);

        when(iamContextFacade.getUserData(1L)).thenReturn(mock);

        assertEquals(mock, service.getUserData(1L));
    }
}