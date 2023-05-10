package de.coiaf.footballprediction.sharedkernal.domain.model.commands;

/**
 * Factory for command dispatchers
 */
public class CommandDispatcherFactory {

    /**
     * Creates a new in memory command dispatcher
     * @return the created command dispatcher
     */
    public CommandDispatcher createInMemoryCommandDispatcher() {
        return new InMemoryCommandDispatcher();
    }
}
