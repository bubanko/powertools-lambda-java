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

import org.slf4j.Logger;
import org.slf4j.simple.SimpleLogger;
import org.slf4j.spi.LocationAwareLogger;

/**
 * Extends the {@link SimpleLogger} to provide a configurable log level for testing
 * purposes.
 *
 * @see ConfigurableTestLoggerScope
 */
public class ConfigurableTestLogger extends SimpleLogger implements Logger {

    public static final int LOG_LEVEL_OFF = LocationAwareLogger.ERROR_INT + 10;

    protected ConfigurableTestLogger(String name) {
        super(name);
    }

    int getLogLevel() {
        return this.currentLogLevel;
    }

    void setLogLevel(int logLevel) {
        this.currentLogLevel = logLevel;
    }
}
