package com.sccpa.winelist.data;

import com.sccpa.winelist.service.WineService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WineRepository implements WineService {
    private static Logger LOGGER = LoggerFactory.getLogger(WineRepository.class);

    private RestTemplate restTemplate;
    private String jwtToken;

    @Value("${aws.host:http://localhost:4000}")
    private String awsHost;

    @Autowired
    public WineRepository(final RestTemplate template) {
        restTemplate = template;
    }

    @Override
    public boolean login(final String username, final String password) {
        try {
            final String url = awsHost + "/auth/login";
            final Map<String, Object> params = new LinkedHashMap<>();
            params.put("username", username);
            params.put("password", password);
            final RequestEntity request = createRequest(params, HttpMethod.POST, url);
            ResponseEntity<Map> entity = restTemplate.exchange(request, Map.class);
            LOGGER.debug("status code: {}", entity.getStatusCode());
            LOGGER.debug("headers:\n{}", entity.getHeaders());
            if (entity.getBody() != null
                    && entity.getBody().containsKey("auth")
                    && (boolean) entity.getBody().get("auth")) {
                LOGGER.debug("Login successful!");
                final List<String> tokens = entity.getHeaders().get("set-cookie");
                if (tokens != null && tokens.size() == 1)
                    jwtToken = StringUtils.remove(StringUtils.substringBefore(
                            tokens.get(0), ";"), "token=");

            } else {
                LOGGER.warn("Login NOT successful!");
            }
        }
        catch (final Exception ex) {
            LOGGER.warn("Login failed!", ex);
            throw new RuntimeException(ex);
        }

        return StringUtils.isNotBlank(jwtToken);
    }

    @Override
    public List<WineEntry> fetchEntireList() {
        return queryList();
    }

    @Override
    public WineEntry fetchEntry(final long id) {
        return restTemplate.getForObject(toUrl(awsHost, "wines", id), WineEntry.class);
    }

    @Override
    public WineEntry update(final WineEntry entry) {
        final String url = toUrl(awsHost, "wines", entry.getId());
        try {
            final ResponseEntity<WineEntry> entity = restTemplate.exchange(
                    createRequest(entry, HttpMethod.PUT, url),
                    new ParameterizedTypeReference<WineEntry>() {});
            LOGGER.debug("status code: {}", entity.getStatusCode());
            LOGGER.debug("headers:\n{}", entity.getHeaders());
            LOGGER.debug("updated: {}", entity.getBody());
            /*
                {
                    "fieldCount": 0,
                    "affectedRows": 1,
                    "insertId": 0,
                    "serverStatus": 2,
                    "warningCount": 0,
                    "message": "(Rows matched: 1  Changed: 1  Warnings: 0",
                    "protocol41": true,
                    "changedRows": 1
                }
             */
            return entry;
        }
        catch (Exception ex) {
            LOGGER.error("Update failed!", ex);
            return null;
        }
    }

    @Override
    public WineEntry insert(final WineEntry entry) {
        final String url = toUrl(awsHost, "wines");
        try {
            final ResponseEntity<Map> entity = restTemplate.exchange(
                    createRequest(entry, HttpMethod.POST, url),
                    new ParameterizedTypeReference<Map>() {});
            LOGGER.debug("status code: {}", entity.getStatusCode());
            LOGGER.debug("headers:\n{}", entity.getHeaders());
            LOGGER.debug("inserted: {}", entity.getBody());
            /*
                {
                    "fieldCount": 0,
                    "affectedRows": 1,
                    "insertId": 3045,
                    "serverStatus": 2,
                    "warningCount": 0,
                    "message": "",
                    "protocol41": true,
                    "changedRows": 0
                }
             */
            entry.setId(NumberUtils.toLong(String.valueOf(entity.getBody().get("insertId"))));
            return entry;
        }
        catch (Exception ex) {
            LOGGER.error("Insert failed!", ex);
            return null;
        }
    }

    private List<WineEntry> queryList(final Object... parameters) {
        try {
            ResponseEntity<List<WineEntry>> entity = restTemplate.exchange(
                    createRequest(null, HttpMethod.GET, toUrl(awsHost, "wines")),
                    new ParameterizedTypeReference<List<WineEntry>>(){});
            LOGGER.debug("status code: {}", entity.getStatusCode());
            LOGGER.debug("headers:\n{}", entity.getHeaders());
            return entity.getBody();
        }
        catch (final Exception ex) {
            LOGGER.warn("Query list failed!", ex);
            // noinspection unchecked
            return Collections.EMPTY_LIST;
        }
    }

    private <T> RequestEntity<T> createRequest(final T body,
                                        final HttpMethod method,
                                        final String url) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        headers.set("X-User-Agent", "JMS Swinger App");
        return new RequestEntity<>(body, headers, method, new URI(url));
    }

    private String toUrl(final Object... parts) {
        return StringUtils.join(parts, "/");
    }
}
