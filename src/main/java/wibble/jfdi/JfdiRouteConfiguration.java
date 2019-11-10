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
                        .to("direct:agg")
                        .to("direct:blah");
            }
        };
    }

    @Bean
    public RouteBuilder AggRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:agg")
                        .log("get in: ${body}")
                        .aggregate(constant(true), new GroupedBodyAggregationStrategy())
                        .completionSize(5)
                        .log("aggregated : ${body}");
            }
        };
    }

    @Bean
    public RouteBuilder BlahRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:blah")
                        .log("yeah man: ${body}")
                        .convertBodyTo(String.class)
                        .to("file://target/out");
            }
        };
    }
}
