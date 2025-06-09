package com.shitianqing.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class RecvForLambda {

    private final static String QUEUE_NAME = "conversion";
    private final static String EXCHANGE_NAME = "upload";
    private final static String RABBITMQ_HOST = "hello-world.default.svc.cluster.local"; // RabbitMQ服务器地址
    private final static String OPENFAAS_GATEWAY = "http://gateway.openfaas.svc.cluster.local:8080/function/conversion"; // OpenFaaS函数地址

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_HOST);
        factory.setUsername("default_user_8htcCAmSdbORg9cNKIt");
        factory.setPassword("6I8REnY-hQ2ScPCPKiZ6mbZMnxw93OLC");

        CountDownLatch latch = new CountDownLatch(1);

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {

            // 声明交换机（确保交换机存在，类型为 direct，持久化）
            // direct 是 RabbitMQ 中的一种交换机（Exchange）类型，它的作用是按照 完全匹配的 routing key 将消息路由到队列。
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);

            // 声明一个名为 "hello" 的队列，如果不存在则创建；存在则确保属性一致：
            // 参数说明：
            // 1. QUEUE_NAME  -> 队列名称（多个消费者使用同名队列即可接收消息）
            // 2. true        -> durable（是否持久化，true 表示 RabbitMQ 重启后队列仍存在）
            // 3. false       -> exclusive（是否排他，仅限当前连接访问，false 表示多个连接可共享）
            // 4. false       -> autoDelete（是否自动删除，true 表示最后一个消费者断开后删除）
            // 5. null        -> arguments（额外参数，通常用于设置 TTL、死信队列等，这里设为 null）
            // 参数必须与rabbitmq中完全一致，否则 RabbitMQ 会报错
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);


            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, QUEUE_NAME);

            System.out.println(" [*] Waiting for messages in '" + QUEUE_NAME + "'.");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] Received '" + message + "'");

                try {
                    URL url = new URL(OPENFAAS_GATEWAY);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(message.getBytes(StandardCharsets.UTF_8));
                    }

                    int responseCode = conn.getResponseCode();
                    System.out.println(" [x] POST Response Code :: " + responseCode);

                    conn.disconnect();
                } catch (
                        Exception e) {
                    System.err.println(" [!] Failed to send HTTP request: " + e.getMessage());
                }
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
            latch.await();
        }
    }
}
