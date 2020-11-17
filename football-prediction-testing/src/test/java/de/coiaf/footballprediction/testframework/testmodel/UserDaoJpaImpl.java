package de.coiaf.footballprediction.testframework.testmodel;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class UserDaoJpaImpl implements UserDao {

    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void addUser(User user) {
        if ( user == null ) {
            throw new IllegalArgumentException("user cannot be null");
        }
        entityManager.persist(user);
    }

    public User getUserById(long id) {
        if ( false ) {
            // option below does not fetch eagerly telephone
            return entityManager.find(User.class, id);
        } else {
            String jql;
            jql = "select user from User user where id = ?1";
//            if ( false ) {
//                // option below creates more then one user when it has more than one telephone
//                jql = "select user from User user left join fetch user.telephones where id = ?1";
//            } else {
//                jql = "select distinct(user) from User user left join fetch user.telephones where id = ?1";
//            }
            Query query = entityManager.createQuery(jql);
            query.setParameter(1, id);
            if ( false ) {
                // option below makes negative tests to fail
                return (User) query.getSingleResult();
            } else {
                @SuppressWarnings("unchecked")
                List<User> users = query.getResultList();
                assert users.size() <= 1 : "returned " + users.size() + " users"; // sanity check
                return users.isEmpty() ? null : (User) users.get(0);
            }
        }
    }

    public List<User> getAllUsers() {
        String jql = "select user from User user";
        Query query = entityManager.createQuery(jql);
        List<User> users = query.getResultList();

        return users;
    }

    public void deleteUser(long id) {
        // option below would not cascade-delete telephones
        if ( false ) {
            String jql = "delete User where id = ?";
            Query query = entityManager.createQuery(jql);
            query.setParameter(1, id);
            query.executeUpdate();
        } else {
            User user = entityManager.find(User.class, id);
            entityManager.remove(user);
        }
    }
}
