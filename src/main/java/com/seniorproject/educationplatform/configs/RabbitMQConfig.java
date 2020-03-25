package com.seniorproject.educationplatform.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({ "classpath:rabbitmq.properties" })
public class RabbitMQConfig {
    @Value("${rabbitmq.queue.name}")
    private String queueName;
    @Value("${rabbitmq.direct.exchange.name}")
    private String directExchangeName;
    @Value("${rabbitmq.routingkey}")
    private String routingKey;

    @Bean
    public Queue queue_1() {
        return new Queue(queueName, false);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directExchangeName);
    }

    @Bean
    public Binding binding_1(Queue queue_1, DirectExchange exchange) {
        return BindingBuilder.bind(queue_1).to(exchange).with(routingKey);
    }

}
