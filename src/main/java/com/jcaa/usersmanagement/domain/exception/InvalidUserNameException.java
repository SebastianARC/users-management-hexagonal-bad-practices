package com.jcaa.usersmanagement.domain.exception;

public final class InvalidUserNameException extends DomainException {

  private static final String MSG_EMPTY =
          "The user name must not be empty.";
  private static final String MSG_TOO_SHORT =
          "The user name must have at least %d characters.";

  private InvalidUserNameException(final String message) {
    super(message);
  }

  public static InvalidUserNameException becauseValueIsEmpty() {
    return new InvalidUserNameException(MSG_EMPTY);
  }

  public static InvalidUserNameException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidUserNameException(String.format(MSG_TOO_SHORT, minimumLength));
  }
}