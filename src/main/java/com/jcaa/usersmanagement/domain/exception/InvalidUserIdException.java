package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserIdException extends DomainException {

  private static final String MSG_EMPTY =
          "The user id must not be empty.";

  private InvalidUserIdException(final String message) {
    super(message);
  }

  public static InvalidUserIdException becauseValueIsEmpty() {
    return new InvalidUserIdException(MSG_EMPTY);
  }
}