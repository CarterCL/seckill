package com.cl.order.service;

import com.cl.base.annotation.Retry;
import com.cl.base.exception.RetryException;
import com.cl.order.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author CarterCL
 * @create 2020/10/5 22:28
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductService {

    private final ProductMapper productMapper;

    @Retry(maxTimes = 5)
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED, propagation =
            Propagation.REQUIRES_NEW)
    public void reduceStock(Integer productId) {

        Integer version = productMapper.findVersionById(productId);

        int result = productMapper.updateStock(productId, version, -1);
        if (result != 1) {
            throw new RetryException();
        }
    }
}
