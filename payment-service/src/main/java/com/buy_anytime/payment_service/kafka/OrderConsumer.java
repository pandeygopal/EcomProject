package com.buy_anytime.payment_service.kafka;


import com.buy_anytime.common_lib.dto.order.OrderEvent;
import com.buy_anytime.payment_service.entity.PaymentStatus;
import com.buy_anytime.payment_service.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);
    private final PaymentService paymentService;

    public OrderConsumer(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @KafkaListener(topics = "${spring.kafka.order-topic.name}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consume(OrderEvent orderEvent){
        try {
            LOGGER.info(String.format("OrderDTO event received in payment service -> %s", orderEvent.toString()));

            if(orderEvent.getMessage().equals("REFUND")){
                paymentService.refundPayment(orderEvent.getOrderDTO().getOrderId());
                return;
            }

            if(orderEvent.getMessage().equals("PAID")){
                paymentService.updateStatusPayment(orderEvent.getOrderDTO().getOrderId(), PaymentStatus.SUCCESS);
                return;
            }

            paymentService.createPayment(orderEvent);

        }catch(Exception e){
           LOGGER.warn(String.format("Error message -> %s", e.getMessage()));
        }
    }
}
