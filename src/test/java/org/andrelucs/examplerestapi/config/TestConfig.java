package org.andrelucs.examplerestapi.config;

public class TestConfig {
    public static final int SERVER_PORT = 8888;

    public static final String HEADER_PARAM_ORIGIN = "Origin";
    public static final String HEADER_PARAM_AUTHORIZATION = "Authorization";

    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_YML = "application/x-yaml";

    public static final String VALID_TEST_ORIGIN = "http://localhost:9000";
    public static final String INVALID_TEST_ORIGIN = "https://google.com";
}
