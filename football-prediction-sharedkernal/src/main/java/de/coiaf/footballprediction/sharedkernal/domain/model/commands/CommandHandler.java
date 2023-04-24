package de.coiaf.footballprediction.sharedkernal.domain.model.commands;

/**
 * Description of an handler handling a command
 * @param <C> the command type
 * @param <R> the return type of the command
 */
@FunctionalInterface
public interface CommandHandler<C extends Command<R>, R> {

    /**
     * Executes command
     * @param command the command to be executed
     * @return the result of the command execution
     */
    R handle(C command);
}
