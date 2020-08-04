package kr.codechobo.infra;

import kr.codechobo.infra.TestProfileConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Eunmo Hong
 * @since : 2020/07/27
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Import(TestProfileConfiguration.class)
public @interface RestDocsTest {
}
