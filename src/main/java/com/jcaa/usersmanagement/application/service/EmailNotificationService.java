package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.out.EmailSenderPort;
import com.jcaa.usersmanagement.domain.exception.EmailSenderException;
import com.jcaa.usersmanagement.domain.model.EmailDestinationModel;
import com.jcaa.usersmanagement.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

@Log
@RequiredArgsConstructor
public final class EmailNotificationService {

  private static final String SUBJECT_CREATED = "Tu cuenta ha sido creada — Gestión de Usuarios";
  private static final String SUBJECT_UPDATED = "Tu cuenta ha sido actualizada — Gestión de Usuarios";

  private static final String TOKEN_NAME     = "name";
  private static final String TOKEN_EMAIL    = "email";
  private static final String TOKEN_PASSWORD = "password";
  private static final String TOKEN_ROLE     = "role";
  private static final String TOKEN_STATUS   = "status";

  private final EmailSenderPort emailSenderPort;

  public void notifyUserCreated(final UserModel user, final String plainPassword) {
    final Map<String, String> tokens = Map.of(
            TOKEN_NAME,     user.getName().value(),
            TOKEN_EMAIL,    user.getEmail().value(),
            TOKEN_PASSWORD, plainPassword,
            TOKEN_ROLE,     user.getRole().name());
    sendNotification(user, SUBJECT_CREATED, "user-created.html", tokens);
  }

  public void notifyUserUpdated(final UserModel user) {
    final Map<String, String> tokens = Map.of(
            TOKEN_NAME,   user.getName().value(),
            TOKEN_EMAIL,  user.getEmail().value(),
            TOKEN_ROLE,   user.getRole().name(),
            TOKEN_STATUS, user.getStatus().name());
    sendNotification(user, SUBJECT_UPDATED, "user-updated.html", tokens);
  }

  private void sendNotification(
          final UserModel user,
          final String subject,
          final String templateName,
          final Map<String, String> tokens) {
    final String body = renderTemplate(loadTemplate(templateName), tokens);
    final EmailDestinationModel destination = buildDestination(user, subject, body);
    sendOrFail(destination);
  }

  private static EmailDestinationModel buildDestination(
          final UserModel user, final String subject, final String body) {
    return new EmailDestinationModel(
            user.getEmail().value(), user.getName().value(), subject, body);
  }

  private String loadTemplate(final String templateName) {
    final String path = "/templates/" + templateName;
    try (InputStream inputStream = openResourceStream(path)) {
      if (Objects.isNull(inputStream)) {
        throw EmailSenderException.becauseSendFailed(
                new IllegalStateException("Template not found: " + path));
      }
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (final IOException ioException) {
      throw EmailSenderException.becauseSendFailed(ioException);
    }
  }

  InputStream openResourceStream(final String path) {
    return getClass().getResourceAsStream(path);
  }

  private static String renderTemplate(final String template, final Map<String, String> tokens) {
    String result = template;
    for (final Map.Entry<String, String> tokenEntry : tokens.entrySet()) {
      result = result.replace("{{" + tokenEntry.getKey() + "}}", tokenEntry.getValue());
    }
    return result;
  }

  private void sendOrFail(final EmailDestinationModel destination) {
    try {
      emailSenderPort.send(destination);
    } catch (final EmailSenderException senderException) {
      log.log(
              Level.WARNING,
              "[EmailNotificationService] No se pudo enviar correo a: {0}. Causa: {1}",
              new Object[]{destination.getDestinationEmail(), senderException.getMessage()});
      throw senderException;
    }
  }
}
