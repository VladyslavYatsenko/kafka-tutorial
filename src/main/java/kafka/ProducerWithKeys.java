package kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerWithKeys {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final Logger logger = LoggerFactory.getLogger(ProducerWithKeys.class);
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i < 15; i++) {

            String topic = "my_first_topic";
            String message = "Hello from JDK->" + i;
            String key = "id_" + Integer.toString(i);
            final ProducerRecord<String, String> record =
                    new ProducerRecord<String, String>(topic,key, message);
            logger.info("Key: "+key);
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null) {
                        logger.info("Received new metadata. \n"
                                + "Topic: " + recordMetadata.topic()   + "\n"
                                + "Timestamp: " + recordMetadata.timestamp() + "\n"
                                + "Partition: "+recordMetadata.partition() + "\n"
                                + "Offset: "+recordMetadata.offset());
                    } else {
                        logger.error("error while producing", e);
                    }
                }
            }).get(); //block the .send() to make it synchronous - dont do this in prod

            producer.flush();
        }
        producer.close();
    }
}
