package com.java.mapper;

import com.java.dto.UserDTO;
import com.java.entities.auth.UserEntity;
import com.java.request.RegistryRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, UserEntity> {
    List<UserEntity> fromRequest(List<RegistryRequest> dto);
}
