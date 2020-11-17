package de.coiaf.footballprediction.testframework.v1.boundary;

import de.coiaf.footballprediction.testframework.AbstractJpaDbUnitSingleTestFileTestCase;
import de.coiaf.footballprediction.testframework.testmodel.User;
import de.coiaf.footballprediction.testframework.testmodel.UserDaoJpaImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static de.coiaf.footballprediction.testframework.testmodel.EntitiesHelper.newUser;
import static de.coiaf.footballprediction.testframework.testmodel.EntitiesHelper.newUserWithTelephone;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserDaoJpaImpSingleFileTest extends AbstractJpaDbUnitSingleTestFileTestCase {

    private static final String FILE_PATH = "de/coiaf/footballprediction/testframework/testmodel";

    UserDaoJpaImpl dao;

    @Before
    public void prepareDao() {
        dao = new UserDaoJpaImpl();
        dao.setEntityManager(this.getEntityManager());
    }

    @Test
    public void testAddUserWithTelephone() throws Exception {
        beginTransaction();
        List<User> users = dao.getAllUsers();
        User user = newUserWithTelephone();
        dao.addUser(user);
//        users = dao.getAllUsers();
        commitTransaction();
        users = dao.getAllUsers();
        long id = user.getId();
        assertTrue(id>0);

        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    public void testAddUserWithoutTelephone() throws Exception {
        beginTransaction();
        List<User> users = dao.getAllUsers();
        User user = newUser();
        dao.addUser(user);
//        users = dao.getAllUsers();
        commitTransaction();
        users = dao.getAllUsers();
        long id = user.getId();
        assertTrue(id>0);

        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Override
    protected String getDataSetFilename() {
        return FILE_PATH + "/user_placeholderfree_ids.xml";
    }
}
