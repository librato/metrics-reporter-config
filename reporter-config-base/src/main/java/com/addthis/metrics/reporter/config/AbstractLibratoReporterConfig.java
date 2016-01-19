/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.addthis.metrics.reporter.config;

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.List;

public class AbstractLibratoReporterConfig extends AbstractMetricReporterConfig
{
    private static final Logger log = LoggerFactory.getLogger(AbstractLibratoReporterConfig.class);

    @NotBlank
    protected String api = "https://metrics-api.librato.com/v1/metrics";

    @NotBlank
    protected String email;

    @NotBlank
    protected String token;

    @NotBlank
    protected String source;

    @Valid
    protected List<String> enabled_expansions;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<String> getEnabled_expansions() {
        return enabled_expansions;
    }

    public void setEnabled_expansions(List<String> enabled_expansions) {
        this.enabled_expansions = enabled_expansions;
    }

    protected boolean setup(String className)
    {

        if (!isClassAvailable(className))
        {
            log.error("Tried to enable LibratoReporter, but class {} was not found", className);
            return false;
        }

        return true;
    }
}
