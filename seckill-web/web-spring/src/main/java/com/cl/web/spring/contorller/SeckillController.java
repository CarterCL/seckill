package com.cl.web.spring.contorller;

import com.cl.base.dto.SeckillDTO;
import com.cl.base.enums.CodeEnum;
import com.cl.base.vo.Result;
import com.cl.web.spring.service.SeckillService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CarterCL
 * @create 2020/9/29 21:40
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SeckillController {

    private static RateLimiter rateLimiter = RateLimiter.create(20);
    private final SeckillService seckillService;


    @GetMapping("/seckill")
    public Result<String> seckill(@Validated SeckillDTO seckillDTO) {

        if (rateLimiter.tryAcquire()) {
            return Result.makeResult(seckillService.seckill(seckillDTO));
        }

        return Result.makeResult(CodeEnum.SYSTEM_BUSY);
    }
}
