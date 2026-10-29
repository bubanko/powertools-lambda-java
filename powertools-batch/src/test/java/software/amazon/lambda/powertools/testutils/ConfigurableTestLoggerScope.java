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

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A scoped wrapper for temporarily modifying the log level of a
 * {@link ConfigurableTestLogger} during test execution.
 * <p>
 * This class provides a mechanism to change the log level of a specific logger at runtime
 * and automatically restore the original log level when the scope is closed. It is
 * designed for use in test scenarios where log output needs to be controlled or
 * suppressed.
 * </p>
 * <p>
 * <b>Parallel Test Execution:</b> Since loggers are global instances, tests that modify
 * log levels for the same logger should not run in parallel, as concurrent modifications
 * would conflict and produce unpredictable results. As such, attempting to create
 * multiple scopes for the same logger will result in an {@link IllegalStateException}.
 * Use JUnit's {@link org.junit.jupiter.api.parallel.ResourceLock @ResourceLock}
 * annotation to ensure tests modifying the same logger execute sequentially.
 * </p>
 *
 * @see ConfigurableTestLoggerScope#forLogger(Class)
 */
public class ConfigurableTestLoggerScope implements AutoCloseable {

    private static final Set<String> ACTIVE_LOGGER_SCOPES = ConcurrentHashMap.newKeySet();

    private final ConfigurableTestLogger logger;
    private final int originalLogLevel;

    private ConfigurableTestLoggerScope(ConfigurableTestLogger logger) {
        Objects.requireNonNull(logger, "logger must not be null");
        this.logger = logger;
        this.originalLogLevel = logger.getLogLevel();
    }

    /**
     * Creates a new {@code ConfigurableTestLoggerScope} for the logger associated with the
     * specified class.
     * <p>
     * This method retrieves the logger for the given class and wraps it in a scope that
     * allows temporary log level modifications.
     * </p>
     *
     * @param clazz the class whose logger should be scoped
     * @return a new {@code ConfigurableTestLoggerScope} instance
     * @throws NullPointerException if {@code clazz} is null
     * @throws IllegalStateException if a scope for this logger is already active, or if the
     *     logger is not a {@link ConfigurableTestLogger}
     */
    public static ConfigurableTestLoggerScope forLogger(Class<?> clazz) {
        Objects.requireNonNull(clazz, "clazz must not be null");
        String loggerName = clazz.getName();
        if (ACTIVE_LOGGER_SCOPES.add(loggerName)) {
            Logger logger = LoggerFactory.getLogger(loggerName);
            if (logger instanceof ConfigurableTestLogger ctl) {
                return new ConfigurableTestLoggerScope(ctl);
            } else {
                throw new IllegalStateException("Logger " + loggerName + " is not a " +
                        ConfigurableTestLogger.class.getName());
            }
        } else {
            throw new IllegalStateException("Logger " + loggerName + " is already scoped");
        }
    }

    public void setLogLevel(int logLevel) {
        this.logger.setLogLevel(logLevel);
    }

    @Override
    public void close() {
        this.logger.setLogLevel(this.originalLogLevel);
        ACTIVE_LOGGER_SCOPES.remove(this.logger.getName());
    }
}
