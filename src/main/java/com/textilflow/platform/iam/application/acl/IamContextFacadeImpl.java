package com.textilflow.platform.iam.application.acl;

import com.textilflow.platform.iam.domain.model.commands.UpdateUserDataCommand;
import com.textilflow.platform.iam.domain.model.queries.GetUserByEmailQuery;
import com.textilflow.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.textilflow.platform.iam.domain.services.UserCommandService;
import com.textilflow.platform.iam.domain.services.UserQueryService;
import com.textilflow.platform.iam.interfaces.acl.IamContextFacade;
import com.textilflow.platform.iam.interfaces.acl.model.UserData;
import org.springframework.stereotype.Service;

/**
 * Facade implementation for IAM context
 */
@Service
public class IamContextFacadeImpl implements IamContextFacade {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public IamContextFacadeImpl(UserQueryService userQueryService,
                                UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    @Override
    public Long getUserIdByEmail(String email) {
        var getUserByEmailQuery = new GetUserByEmailQuery(email);
        var user = userQueryService.handle(getUserByEmailQuery);
        return user.map(u -> u.getId()).orElse(null);
    }

    @Override
    public boolean userExists(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        return user.isPresent();
    }

    @Override
    public String getUserRole(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        return user.map(u -> u.getRoleName()).orElse(null);
    }

    @Override
    public UserData getUserData(Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);

        if (user.isEmpty()) {
            return null;
        }

        var userEntity = user.get();
        return new UserData(
                userEntity.getName(),
                userEntity.getEmail(),
                userEntity.getCountry(),
                userEntity.getCity(),
                userEntity.getAddress(),
                userEntity.getPhone()
        );
    }

    @Override
    public void updateUserData(Long userId, String name, String email,
                               String country, String city, String address, String phone) {
        var updateUserDataCommand = new UpdateUserDataCommand(
                userId, name, email, country, city, address, phone
        );
        userCommandService.handle(updateUserDataCommand);
    }
}
