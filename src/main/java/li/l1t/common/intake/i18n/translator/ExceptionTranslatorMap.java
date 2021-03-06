/*
 * Intake-Spigot, a Spigot bridge for the Intake command framework.
 * Copyright (C) Philipp Nowak (Literallie)
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package li.l1t.common.intake.i18n.translator;

import li.l1t.common.IntakePlugin;
import li.l1t.common.intake.i18n.ErrorTranslator;
import li.l1t.common.i18n.Message;
import li.l1t.common.intake.i18n.translator.generic.FallbackTranslator;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Maps classes to their exception translators.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 * @since 2016-07-24
 */
public class ExceptionTranslatorMap
        extends HashMap<Class<? extends Exception>, ExceptionTranslator<? extends Exception>> {
    //Bukkit registers plugin loggers with the plugin class name, not package (:
    private final ErrorTranslator root;
    private final Logger LOGGER = Logger.getLogger(IntakePlugin.class.getName());
    private FallbackTranslator fallbackTranslator;

    public ExceptionTranslatorMap(ErrorTranslator root) {
        this.root = root;
        putFallbackTranslator();
    }

    private void putFallbackTranslator() {
        fallbackTranslator = new FallbackTranslator();
        putTypesafe(Exception.class, fallbackTranslator);
    }

    @SuppressWarnings("unchecked")
    public Message translateAndLogIfNecessary(Exception exception, String commandLine) {
        ExceptionTranslator translator = getTranslator(exception.getClass());
        if (translator.needsLogging(exception)) {
            LOGGER.log(
                    Level.WARNING,
                    "Error executing /" + commandLine + ":",
                    exception
            );
        }
        return translator.translate(exception, commandLine);
    }

    @SuppressWarnings("unchecked")
    public <T extends Exception> ExceptionTranslator<T> getTypesafe(Class<T> key) {
        return (ExceptionTranslator<T>) super.get(key);
    }

    public <T extends Exception> ExceptionTranslator<? super T> getTranslator(Class<T> exceptionType) {
        Class<?> currentType = exceptionType;
        while (true) {
            @SuppressWarnings("unchecked")
            ExceptionTranslator<? super T> translator = (ExceptionTranslator<? super T>) get(currentType);
            if (translator != null) {
                return translator;
            }
            currentType = currentType.getSuperclass();
            if (currentType == null) {
                return fallbackTranslator; //this should not happen at all, but
                // let's prevent infinite loops like a responsible adults
            }
        }
    }

    @Override
    public ExceptionTranslator<? extends Exception> put(
            Class<? extends Exception> key, ExceptionTranslator<? extends Exception> value
    ) {
        if (!key.isAssignableFrom(value.getExceptionType())) {
            throw new UnsupportedOperationException(String.format(
                    "key must be assignable from value type: %s, %s",
                    key.getName(), value.getExceptionType()
            ));
        }
        return super.put(key, value);
    }

    public <T extends Exception> ExceptionTranslator<? extends Exception> putTypesafe(
            Class<? extends T> key, ExceptionTranslator<T> value
    ) {
        return put(key, value);
    }

    public ErrorTranslator getRoot() {
        return root;
    }
}
