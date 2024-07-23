/*
package CY.cymake.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;


@Configuration
@Slf4j
public class ElasticsearchConfig extends ElasticsearchClientAutoConfiguration {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;


    @Value("${elasticsearch.api_key}")
    private String apiKey;

    @Value("${elasticsearch.user.username}")
    private String username;

    @Value("${elasticsearch.user.password}")
    private String password;



    @Bean
    public ElasticsearchClient elasticsearchClient() {
        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        try {
            // Create the low-level client
            RestClient restClient = RestClient
                    .builder(new HttpHost(host, port, "https"))
                    .setDefaultHeaders(new Header[]{
                            new BasicHeader("Authorization", "ApiKey " + apiKey)
                    })
                    .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                            .setDefaultCredentialsProvider(credsProv)
                    )
                    .build();

            // Create the transport with a Jackson mapper
            RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

            // And create the API client

            return new ElasticsearchClient(transport);

        } catch (Exception e) {
            log.error("Elasticsearch rest client error", e);
            throw new RuntimeException("Failed to create Elasticsearch client", e);
        }
    }
}

 */
