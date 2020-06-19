package smartpick;

import org.springframework.transaction.annotation.Transactional;
import smartpick.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @Autowired
    private OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    @Transactional
    public void wheneverConfirmed_ProductReady(@Payload Confirmed confirmed){

        if(confirmed.isMe()){
            System.out.println("##### listener ProductReady : " + confirmed.toJson());
            Optional<Order> opt = orderRepository.findById(confirmed.getOrderId());

            Order arrivedOrder = opt.get();
            arrivedOrder.setStatus("WAITING PICK");
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    @Transactional
    public void wheneverPicked_End(@Payload Picked picked){

        if(picked.isMe()){
            System.out.println("##### listener End : " + picked.toJson());
            Optional<Order> opt = orderRepository.findById(picked.getOrderId());

            Order pickedOrder = opt.get();
            pickedOrder.setStatus("PICKED");
        }
    }

}
