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

package li.l1t.common.intake;

import org.bukkit.command.CommandSender;

/**
 * Receives notifications about exceptions that occur while executing commands.
 *
 * @author <a href="https://l1t.li/">Literallie</a>
 * @since 2017-03-23
 */
public interface CommandExceptionListener {
    /**
     * Handles an exception that occurred while executing a command.
     *
     * @param argLine   the command line that triggered the command execution, without leading slash
     * @param sender    the command sender that issued the command
     * @param exception the exception that occurred
     * @return whether the built-in exception handling via {@link li.l1t.common.intake.i18n.ErrorTranslator} should
     * continue, possibly printing messages and usage information based on the exception type
     */
    boolean handle(String argLine, CommandSender sender, Exception exception);
}
