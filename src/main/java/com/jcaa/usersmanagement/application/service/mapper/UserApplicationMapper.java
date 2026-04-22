package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;

public class UserApplicationMapper {

  public static UserModel fromCreateCommandToModel(final CreateUserCommand command) {
    return UserModel.create(
            new UserId(command.id()),
            new UserName(command.name()),
            new UserEmail(command.email()),
            UserPassword.fromPlainText(command.password()),
            UserRole.fromString(command.role()));
  }

  public static UserModel fromUpdateCommandToModel(
          final UpdateUserCommand command, final UserPassword currentPassword) {

    final UserPassword passwordToUse;
    if (command.password() == null || command.password().isBlank()) {
      passwordToUse = currentPassword;
    } else {
      passwordToUse = UserPassword.fromPlainText(command.password());
    }

    return new UserModel(
            new UserId(command.id()),
            new UserName(command.name()),
            new UserEmail(command.email()),
            passwordToUse,
            UserRole.fromString(command.role()),
            UserStatus.fromString(command.status()));
  }

  public static UserId fromGetUserByIdQueryToUserId(final GetUserByIdQuery query) {
    return new UserId(query.id());
  }

  public static UserId fromDeleteCommandToUserId(final DeleteUserCommand command) {
    return new UserId(command.id());
  }

  public static int roleToCode(final String role) {
    if (role == null || role.isBlank()) {
      throw new IllegalArgumentException("Role must not be null or blank");
    }
    if ("ADMIN".equalsIgnoreCase(role))    return 1;
    if ("MEMBER".equalsIgnoreCase(role))   return 2;
    if ("REVIEWER".equalsIgnoreCase(role)) return 3;
    throw new IllegalArgumentException("Unknown role: " + role);
  }
}
