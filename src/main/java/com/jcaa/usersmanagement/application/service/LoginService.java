package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.service.dto.command.LoginCommand;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class LoginService implements LoginUseCase {

  private final GetUserByEmailPort getUserByEmailPort;
  private final Validator validator;

  @Override
  public UserModel execute(final LoginCommand command) {
    validateCommand(command);
    final UserModel user = findUserOrFail(new UserEmail(command.email()));
    verifyPasswordOrFail(user, command.password());
    verifyUserIsActiveOrFail(user);
    return user;
  }

  private UserModel findUserOrFail(final UserEmail email) {
    return getUserByEmailPort.getByEmail(email)
            .orElseThrow(InvalidCredentialsException::becauseCredentialsAreInvalid);
  }

  private void verifyPasswordOrFail(final UserModel user, final String plainPassword) {
    if (!user.passwordMatches(plainPassword)) {
      throw InvalidCredentialsException.becauseCredentialsAreInvalid();
    }
  }

  private void verifyUserIsActiveOrFail(final UserModel user) {
    if (!user.isAllowedToLogin()) {
      throw InvalidCredentialsException.becauseUserIsNotActive();
    }
  }

  private void validateCommand(final LoginCommand command) {
    final Set<ConstraintViolation<LoginCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}