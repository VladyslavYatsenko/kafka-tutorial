package kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerCallbacks {


    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(ProducerCallbacks.class);
        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers","127.0.0.1:9092");
//        properties.setProperty("key.serializer", StringSerializer.class.getName());
//        properties.setProperty("value.serializer",StringSerializer.class.getName());


        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("my_first_topic", "Hello From JDK");
        producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e == null) {
                    logger.info("Received new metadata. \n"
                            + "Topic: "+recordMetadata.topic()
                            +"\n" + "Timestamp"+recordMetadata.timestamp()) ;
                }else {
                    logger.error("error while producing",e);
                }
            }
        });

        producer.flush();
        producer.close();
    }
}