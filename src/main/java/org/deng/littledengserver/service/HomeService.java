package org.deng.littledengserver.service;

import org.deng.littledengserver.model.dto.CreateHomeDto;

public interface HomeService {
    String generateHomeCode(String token);

    Long createHome(CreateHomeDto createHomeDto);
}
