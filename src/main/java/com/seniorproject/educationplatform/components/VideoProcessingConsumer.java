package com.seniorproject.educationplatform.components;

import com.seniorproject.educationplatform.dto.rabbitmq.VideoProcessMessage;
import com.seniorproject.educationplatform.services.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class VideoProcessingConsumer {
    private static Logger logger = LoggerFactory.getLogger(VideoProcessingProducer.class);

    private VideoService videoService;

    public VideoProcessingConsumer(VideoService videoService) {
        this.videoService = videoService;
    }

    @RabbitListener(queues="${rabbitmq.queue.name}")
    public void consumeMessage(VideoProcessMessage message) throws IOException {
        logger.info("Video Processing, DirectExchange Consumer, Thread name: " + Thread.currentThread().getName());
        logger.info("Message: " + message);
//        videoService.getMediaInformation();
        videoService.processVideo(message);
    }
}
