/*
 * Copyright 2023 Amazon.com, Inc. or its affiliates.
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package software.amazon.lambda.powertools.testutils;

import org.slf4j.ILoggerFactory;
import org.slf4j.simple.SimpleServiceProvider;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * Extends the {@link SimpleServiceProvider} to provide a
 * {@link ConfigurableTestLoggerFactory} for testing purposes.
 */
public class ConfigurableTestServiceProvider extends SimpleServiceProvider implements SLF4JServiceProvider {

    private ILoggerFactory loggerFactory;

    @Override
    public ILoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }

    @Override
    public void initialize() {
        super.initialize();
        this.loggerFactory = new ConfigurableTestLoggerFactory();
    }
}
