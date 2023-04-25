package de.coiaf.footballprediction.sharedkernal.domain.model.commands;

import java.util.Objects;

/**
 * Despription of a command dispatcher
 */
public interface CommandDispatcher {

    /**
     * Determines the appropriate handler for a command, executes it and returns the result.
     * @param command the command to be executed
     * @param <C> the command type
     * @param <R> the return type of a command
     * @return the result of the command execution
     * @throws NullPointerException if command is null
     * @throws IllegalArgumentException if no handler is registered for command
     */
    <C extends Command<R>, R> R dispatch(C command);

    /**
     * Registers a command handler with a command.
     * @param command the command
     * @param handler the handler for the command
     * @param <C> the command type
     * @param <R> the return type of a command
     * @return this instance
     * @throws NullPointerException if command or handler is null
     */
    default <C extends Command<R>, R> CommandDispatcher registerHandler(C command, CommandHandler<C, R> handler) {
        Objects.requireNonNull(command, "Command must not be null");
        Objects.requireNonNull(handler, "Parameter handler must not be null");

        return this.registerHandler(command.getClass(), handler);
    }

    /**
     * Registers a command handler with a command class.
     * @param commandType the class of the command
     * @param handler the handler for the command
     * @param <C> the command type
     * @param <R> the return type of a command
     * @return this instance
     * @throws NullPointerException if commandType or handler is null
     */
    <C extends Command<R>, R> CommandDispatcher registerHandler(Class<? extends Command> commandType, CommandHandler<C, R> handler);
}
