package de.coiaf.footballprediction.testframework.testmodel;

public interface UserDao {

    void addUser( User user );

    User getUserById( long id );

    void deleteUser( long id );
}
