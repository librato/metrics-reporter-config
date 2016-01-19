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

package com.addthis.metrics3.reporter.config;

import com.addthis.metrics.reporter.config.AbstractLibratoReporterConfig;
import com.codahale.metrics.MetricRegistry;
import com.librato.metrics.HttpPoster;
import com.librato.metrics.LibratoReporter;
import com.librato.metrics.LibratoReporter.ExpandedMetric;
import com.librato.metrics.NingHttpPoster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

public class LibratoReporterConfig extends AbstractLibratoReporterConfig implements MetricsReporterConfigThree
{
    private static final Logger log = LoggerFactory.getLogger(LibratoReporterConfig.class);

    private LibratoReporter.Builder reporterBuilder;

    @Override
    // what is this used for?
    public void report() {
    }

    private HttpPoster newHttpPoster() {
        return NingHttpPoster.newPoster(getEmail(), getToken(), getApi());
    }

    @Override
    public boolean enable(MetricRegistry registry)
    {
        boolean success = setup("com.librato.metrics.LibratoReporter");
        if (!success)
        {
            return false;
        }

        Set<ExpandedMetric> expansions = null;

        if (getEnabled_expansions() != null) {
            expansions = new HashSet<ExpandedMetric>();

            for (String expansion : getEnabled_expansions()) {
                try {
                    ExpandedMetric e = ExpandedMetric.valueOf(expansion);
                    expansions.add(e);
                } catch (IllegalArgumentException e) {
                    log.error("invalid enabled_expansions value: " + expansion);
                    return false;
                }
            }
        }

        LibratoReporter.Builder reporterBuilder = LibratoReporter
                .builder(
                        registry,
                        getEmail(),
                        getToken(),
                        getSource())
                .setFilter(MetricFilterTransformer.generateFilter(getPredicate()))
                .setTimeout(10, TimeUnit.SECONDS)
                .setHttpPoster(newHttpPoster());

        if (expansions != null) {
            reporterBuilder.setExpansionConfig(
                    new LibratoReporter.MetricExpansionConfig(expansions));
        }

        LibratoReporter.enable(reporterBuilder, getPeriod(), TimeUnit.SECONDS);

        return true;
    }



}
