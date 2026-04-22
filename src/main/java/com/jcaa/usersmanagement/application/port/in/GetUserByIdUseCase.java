package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.model.UserModel;
import jakarta.validation.Valid;

public interface GetUserByIdUseCase {
  UserModel execute(@Valid GetUserByIdQuery query);
}