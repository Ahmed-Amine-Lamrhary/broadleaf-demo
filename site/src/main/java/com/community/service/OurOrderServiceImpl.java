package com.community.service;

import java.math.BigDecimal;

import org.broadleafcommerce.common.extension.ExtensionResultHolder;
import org.broadleafcommerce.common.money.Money;
import org.broadleafcommerce.core.order.domain.Order;
import org.broadleafcommerce.core.order.service.OrderServiceExtensionHandler;
import org.broadleafcommerce.core.order.service.OrderServiceImpl;
import org.broadleafcommerce.core.order.service.exception.IllegalCartOperationException;
import org.springframework.stereotype.Service;

@Service("ourOrderServiceImpl")
public class OurOrderServiceImpl extends OrderServiceImpl {
    
    @Override
    public void preValidateCartOperation(Order cart) {
        if (cart.getItemCount() > 3) {
            System.out.println("BEFORE : " + cart.getTotal().getAmount());

            Money total = cart.getTotal();
            Money newTotal = new Money((total.getAmount().subtract(total.getAmount().multiply(BigDecimal.valueOf(0.1)))));
            cart.setTotal(newTotal);

            System.out.println("AFTER : " + cart.getTotal().getAmount());
        }

        ExtensionResultHolder erh = new ExtensionResultHolder();
        ((OrderServiceExtensionHandler)this.extensionManager.getProxy()).preValidateCartOperation(cart, erh);
        
        if (erh.getThrowable() instanceof IllegalCartOperationException) {
            throw (IllegalCartOperationException)erh.getThrowable();
        } else if (erh.getThrowable() != null) {
            throw new RuntimeException(erh.getThrowable());
        }
    }
}
