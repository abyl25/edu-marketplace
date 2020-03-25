package com.seniorproject.educationplatform.components;

import com.seniorproject.educationplatform.dto.rabbitmq.VideoProcessMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VideoProcessingProducer {
    private static Logger logger = LoggerFactory.getLogger(VideoProcessingProducer.class);

    private final RabbitTemplate template;
    @Value("${rabbitmq.direct.exchange.name}")
    private String directExchangeName;
    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    public VideoProcessingProducer(RabbitTemplate template) {
        this.template = template;
    }

    public void produceMessage(VideoProcessMessage message) {
        logger.info("Video Processing Producer, Thread name: " + Thread.currentThread().getName());
        logger.info("Video Processing Producer Message: " + message);
        template.convertAndSend(directExchangeName, routingKey, message);
    }

}
