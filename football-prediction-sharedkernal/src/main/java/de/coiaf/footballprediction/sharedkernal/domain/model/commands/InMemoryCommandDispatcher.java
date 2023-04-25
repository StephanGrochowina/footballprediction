package de.coiaf.footballprediction.sharedkernal.domain.model.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class InMemoryCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends Command>, CommandHandler<? extends Command, ?>> commandHandlers = new HashMap<>();

    InMemoryCommandDispatcher() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends Command<R>, R> R dispatch(C command) {
        Objects.requireNonNull(command, "Command must not be null");

        Class<? extends Command> commandType = command.getClass();

        if (!commandHandlers.containsKey(commandType)) {
            throw new IllegalArgumentException("No handler registered for command.");
        }

        CommandHandler<? extends Command, ?> handler = this.commandHandlers.get(commandType);
        return ((CommandHandler<C, R>) handler).handle(command);
    }

    @Override
    public <C extends Command<R>, R> CommandDispatcher registerHandler(Class<? extends Command> commandType, CommandHandler<C, R> handler) {
        Objects.requireNonNull(commandType, "Command type must not be null");
        Objects.requireNonNull(handler, "Parameter handler must not be null");

        return null;
    }
}
