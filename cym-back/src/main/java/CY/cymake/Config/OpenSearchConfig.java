package CY.cymake.Config;

import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.InfoResponse;
import org.opensearch.client.transport.aws.AwsSdk2Transport;
import org.opensearch.client.transport.aws.AwsSdk2TransportOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;

@Configuration
public class OpenSearchConfig {
    @Value("${cloud.aws.opensearch.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.opensearch.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.opensearch.endpoint}")
    private String endpoint;

    @Bean
    public OpenSearchClient openSearchClient() {

        // Create an Apache HTTP client
        SdkHttpClient httpClient = ApacheHttpClient.builder().build();

        // Create AWS credentials
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);


        // Create an OpenSearch client with AWS SDK 2 transport
        OpenSearchClient client = new OpenSearchClient(
                new AwsSdk2Transport(
                        httpClient,
                        endpoint, // OpenSearch endpoint, without https://
                        "es",
                        Region.AP_NORTHEAST_2, // signing service region
                        AwsSdk2TransportOptions.builder()
                                .setCredentials(StaticCredentialsProvider.create(awsCreds))
                                .build()
                )
        );

        // Fetch and print OpenSearch info (optional)
        InfoResponse info;
        try {
            info = client.info();
            System.out.println(info.version().distribution() + ": " + info.version().number());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Close the HTTP client (not necessary if managed by Spring)
        // httpClient.close();

        return client;
    }
}
