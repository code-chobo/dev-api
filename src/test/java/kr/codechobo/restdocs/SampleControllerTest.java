package kr.codechobo.restdocs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/26
 */

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureRestDocs
public class SampleControllerTest {

    @RegisterExtension
    RestDocumentationExtension restDocumentation = new RestDocumentationExtension();

    WebTestClient client;

    @BeforeEach
    void setUp(ApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        client = WebTestClient.bindToApplicationContext(context)
                .configureClient()
                .filter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void sample() {
        client.get()
                .uri("/api/sample")
                .exchange()
                .expectBody()
                .consumeWith(document("sample",
                        responseFields(
                                fieldWithPath("message").description("sample 메시지입니다.")
                        )
                ));
    }
}
