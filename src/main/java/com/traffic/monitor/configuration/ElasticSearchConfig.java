package com.traffic.monitor.configuration;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.traffic.monitor.repository")
public class ElasticSearchConfig {

    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port}")
    private int esPort;

    @Value("${elasticsearch.clustername}")
    private String esClusterName;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(String.format("%s:%d", esHost, esPort))  // set the address of the Elasticsearch cluster
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchRestTemplate elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(restHighLevelClient());
    }

}
