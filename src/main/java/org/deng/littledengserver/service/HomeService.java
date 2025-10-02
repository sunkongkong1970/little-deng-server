package org.deng.littledengserver.service;

import org.deng.littledengserver.model.vo.CreateHomeVo;

public interface HomeService {
    String generateHomeCode(String token);

    Long createHome(CreateHomeVo createHomeVo);
}
