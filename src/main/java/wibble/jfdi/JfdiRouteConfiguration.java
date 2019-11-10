package wibble.jfdi;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootConfiguration
public class JfdiRouteConfiguration {

    @Bean
    public RouteBuilder JfdiRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:jfdi")
                        .aggregate(constant(true), new GroupedBodyAggregationStrategy())
                        .completionSize(5)
                        .log("aggregated ${header.CamelAggregatedSize} messages as a result of a ${header.CamelAggregatedCompletedBy} trigger")
                        .to("direct:blah")
                        .end();
            }
        };
    }

    @Bean
    public RouteBuilder BlahRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:blah")
                        .convertBodyTo(String.class)
                        .to("file://target/out");
            }
        };
    }
}
