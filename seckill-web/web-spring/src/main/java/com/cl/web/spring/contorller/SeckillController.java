package com.cl.web.spring.contorller;

import com.cl.web.base.dto.SeckillDTO;
import com.cl.web.base.vo.Result;
import com.cl.web.spring.service.SeckillService;
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

    private final SeckillService seckillService;

    @GetMapping("/seckill")
    public Result<String> test(@Validated SeckillDTO seckillDTO) {

        return Result.makeResult(seckillService.seckill(seckillDTO));
    }
}
