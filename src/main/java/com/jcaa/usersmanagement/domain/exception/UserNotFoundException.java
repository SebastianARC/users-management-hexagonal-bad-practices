package com.jcaa.usersmanagement.domain.exception;

public final class UserNotFoundException extends DomainException {

  private static final String MSG_ID_NOT_FOUND =
          "The user with id '%s' was not found.";

  private UserNotFoundException(final String message) {
    super(message);
  }

  public static UserNotFoundException becauseIdWasNotFound(final String userId) {
    return new UserNotFoundException(String.format(MSG_ID_NOT_FOUND, userId));
  }
}