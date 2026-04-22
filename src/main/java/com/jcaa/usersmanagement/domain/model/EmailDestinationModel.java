package com.jcaa.usersmanagement.domain.model;

import lombok.Value;

import java.util.Objects;

@Value
public class EmailDestinationModel {

  private static final String MSG_EMAIL_REQUIRED   = "El email del destinatario es requerido.";
  private static final String MSG_NAME_REQUIRED    = "El nombre del destinatario es requerido.";
  private static final String MSG_SUBJECT_REQUIRED = "El asunto es requerido.";
  private static final String MSG_BODY_REQUIRED    = "El cuerpo del mensaje es requerido.";

  String destinationEmail;
  String destinationName;
  String subject;
  String body;

  public EmailDestinationModel(
          final String destinationEmail,
          final String destinationName,
          final String subject,
          final String body) {
    this.destinationEmail = validateNotBlank(destinationEmail, MSG_EMAIL_REQUIRED);
    this.destinationName  = validateNotBlank(destinationName,  MSG_NAME_REQUIRED);
    this.subject          = validateNotBlank(subject,          MSG_SUBJECT_REQUIRED);
    this.body             = validateNotBlank(body,             MSG_BODY_REQUIRED);
  }

  private static String validateNotBlank(final String value, final String errorMessage) {
    if (Objects.isNull(value)) {
      throw new NullPointerException(errorMessage);
    }
    if (value.trim().isEmpty()) {
      throw new IllegalArgumentException(errorMessage);
    }
    return value;
  }
}