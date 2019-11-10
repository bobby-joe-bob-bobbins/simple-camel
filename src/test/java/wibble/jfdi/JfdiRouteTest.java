package wibble.jfdi;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;

import org.apache.camel.ProducerTemplate;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(CamelSpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
@MockEndpoints
public class JfdiRouteTest {

    @Autowired
    private ProducerTemplate producer;

    @EndpointInject("mock:direct:agg")
    private MockEndpoint agg;

    @EndpointInject("mock:direct:blah")
    private MockEndpoint blah;

    @Test
    public void execute() {

        for (int i = 0; i < 20; i++) {
            producer.sendBody("direct:jfdi", i);
        }

        agg.expectedMessageCount(20);
        blah.expectedMessageCount(4);
        blah.expectedBodiesReceived("0,1,2,3,4", "5,6,7,8,9", "10,11,12,13,14", "15,16,17,18,19");
    }

}