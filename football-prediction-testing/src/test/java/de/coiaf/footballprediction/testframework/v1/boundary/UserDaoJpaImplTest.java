package de.coiaf.footballprediction.testframework.v1.boundary;

import de.coiaf.footballprediction.testframework.AbstractJpaDbUnitELTemplateTestCase;
import de.coiaf.footballprediction.testframework.DataSets;
import de.coiaf.footballprediction.testframework.testmodel.User;
import de.coiaf.footballprediction.testframework.testmodel.UserDaoJpaImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static de.coiaf.footballprediction.testframework.testmodel.EntitiesHelper.*;
import static org.junit.Assert.*;

public class UserDaoJpaImplTest extends AbstractJpaDbUnitELTemplateTestCase {

    private static final String FILE_PATH = "de/coiaf/footballprediction/testframework/testmodel";

    UserDaoJpaImpl dao;

    @Before
    public void prepareDao() {
        dao = new UserDaoJpaImpl();
        dao.setEntityManager(this.getEntityManager());
    }

    @Test
    @DataSets(setUpDataSet= FILE_PATH + "/user.xml", referenceClass = UserDaoJpaImpl.class)
    public void testGetUserById() throws Exception {
        beginTransaction();
        long id = getClassId(User.class);
        User user = dao.getUserById(id);
        assertUser(user);
        commitTransaction();
    }

    @Test
    @DataSets(setUpDataSet= FILE_PATH + "/user.xml", referenceClass = UserDaoJpaImpl.class)
    public void testGetUserByIdUnknowId() throws Exception {
        beginTransaction();
        long id = getClassId(User.class)*2;
        User user = dao.getUserById(id);
        commitTransaction();
        assertNull(user);
    }

    @Test
    @DataSets(assertDataSet= FILE_PATH + "/user.xml", referenceClass = UserDaoJpaImpl.class)
    public void testAddUser() throws Exception {
        beginTransaction();
        User user = newUser();
        dao.addUser(user);
        commitTransaction();
        long id = user.getId();
        assertTrue(id>0);
    }

    @Test
    @DataSets(setUpDataSet= FILE_PATH + "/user.xml",assertDataSet= FILE_PATH + "/empty.xml", referenceClass = UserDaoJpaImpl.class)
    public void testDeleteUser() throws Exception {
        beginTransaction();
        long id = getClassId(User.class);
        dao.deleteUser(id);
        commitTransaction();
    }

    @Test
    @DataSets(setUpDataSet= FILE_PATH + "/user-with-telephone.xml", referenceClass = UserDaoJpaImpl.class)
    public void testGetUserByIdWithTelephone() throws Exception {
        beginTransaction();
        long id = getClassId(User.class);
        User user = dao.getUserById(id);
//    commitTransaction();
        assertUserWithTelephone(user);
        commitTransaction(true);
    }

    @Test
    @DataSets(assertDataSet= FILE_PATH + "/user-with-telephone.xml", referenceClass = UserDaoJpaImpl.class)
    public void testAddUserWithTelephone() throws Exception {
        beginTransaction();
        User user = newUserWithTelephone();
        dao.addUser(user);
        commitTransaction();
        long id = user.getId();
        assertTrue(id>0);
    }

    @Test
    @DataSets(setUpDataSet= FILE_PATH + "/user-with-telephone.xml",assertDataSet= FILE_PATH + "/empty.xml", referenceClass = UserDaoJpaImpl.class)
    public void testDeleteUserWithTelephone() throws Exception {
        beginTransaction();
        long id = getClassId(User.class);
        dao.deleteUser(id);
        commitTransaction();
    }

    @Test(expected=IllegalArgumentException.class)
    @DataSets(setUpDataSet= FILE_PATH + "/empty.xml", referenceClass = UserDaoJpaImpl.class)
    public void testAddNullUser() throws Exception {
        dao.addUser(null);
    }

    @Test
    @DataSets(setUpDataSet= FILE_PATH + "/empty.xml", referenceClass = UserDaoJpaImpl.class)
    public void testGetUserByIdOnNullDatabase() throws Exception {
        getUserReturnsNullTest(0);
    }

    @Test
    @DataSets(setUpDataSet= FILE_PATH + "/user.xml", referenceClass = UserDaoJpaImpl.class)
    public void testGetUserByIdUnknownId() throws Exception {
        getUserReturnsNullTest(666);
    }

    private void getUserReturnsNullTest(int deltaId) {
        beginTransaction();
        long id = getClassId(User.class)+deltaId;
        User user = dao.getUserById(id);
        commitTransaction(true);
        assertNull(user);
    }

    @Test
    @DataSets(setUpDataSet= FILE_PATH + "/user-with-telephones.xml", referenceClass = UserDaoJpaImpl.class)
    public void testGetUserByIdWithTelephones() throws Exception {
        beginTransaction();
        long id = getClassId(User.class);
        User user = dao.getUserById(id);
        assertUserWithTelephones(user);
        commitTransaction(true);
    }

    @Test
    @DataSets(setUpDataSet= FILE_PATH + "/users.xml", referenceClass = UserDaoJpaImpl.class)
    public void testGetAllUsers() throws Exception {
        beginTransaction();
        List<User> users = dao.getAllUsers();
        Set<Long> ids = new HashSet<>();

        if (users != null) {
            for (User user : users) {
                if (user != null) {
                    ids.add(user.getId());
                }
            }
        }

        assertNotNull(users);
        assertTrue(users.size() == 4);
        assertTrue(ids.size() == users.size());

        for (User user : users) {
            assertTrue(ids.contains(user.getId()));
        }
    }
}
