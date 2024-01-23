package inertia.spring.inertia;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class InertiaView extends AbstractView {

    public static final String INERTIA_HEADER_NAME = "X-Inertia";

    private static final String INERTIA_TEMPLATE_LOCATION = "classpath:/templates/index.html";
    private static final String INERTIA_TEMPLATE_PLACEHOLDER = "@inertia";
    private static final String INERTIA_ROOT_TAG = "<div id='app' data-page='%s'></div>";

    private final String template;
    private final ObjectMapper objectMapper;

    public InertiaView(ApplicationContext applicationContext) {
        this.setApplicationContext(applicationContext);

        this.template = createTemplate();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        final var page = InertiaPage.get((String) model.get("component"), (String) model.get("props"), request.getRequestURI());

        final var jsonPage = objectMapper.writeValueAsString(page);

        if (isInertiaRequest(request)) {
            writeJsonContentToResponse(response, jsonPage);
        } else {
            writeHtmlContentToResponse(response, jsonPage);
        }
    }

    private boolean isInertiaRequest(HttpServletRequest request) {
        return request.getHeader(INERTIA_HEADER_NAME) != null;
    }

    private void writeJsonContentToResponse(HttpServletResponse response, String json) throws IOException {
        response.addHeader(INERTIA_HEADER_NAME, "true");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentLength(json.length());
        response.getWriter().write(json);
    }

    private void writeHtmlContentToResponse(HttpServletResponse response, String json) throws IOException {
        final var inertiaTag = String.format(INERTIA_ROOT_TAG, json);
        final var html = template.replace(INERTIA_TEMPLATE_PLACEHOLDER, inertiaTag);
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.setContentLength(html.length());
        response.getWriter().write(html);
    }

    private String createTemplate() {
        final var templateFile = Objects.requireNonNull(getApplicationContext())
                .getResource(InertiaView.INERTIA_TEMPLATE_LOCATION);

        if (!templateFile.isReadable()) {
            throw new IllegalStateException("Missing template file: " + InertiaView.INERTIA_TEMPLATE_LOCATION);
        }

        try (final var reader = getReader(templateFile)) {
            return FileCopyUtils.copyToString(reader);
        } catch (Exception e) {
            throw new IllegalStateException("Could not read template file");
        }
    }

    private Reader getReader(Resource resource) throws IOException {
        return new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    @Override
    protected boolean isContextRequired() {
        return true;
    }
}
