package ua.org.smit.authorizationtlx;

import java.util.List;
import java.util.Optional;
import org.hibernate.SessionFactory;
import ua.org.smit.authorizationtlx.dao.Dao;
import ua.org.smit.authorizationtlx.dao.HibernateUtil;

public class AuthorizationImpl implements AuthorizationTLX {

    private final Dao dao = new Dao(AuthUser.class);

    private final ActiveUsersService activeUsers = new ActiveUsersService();

    public AuthorizationImpl(SessionFactory sessionFactory) {
        HibernateUtil.setSessionFactory(sessionFactory);
    }

    @Override
    public Optional<AuthUser> find(String sessionId) {
        Optional<AuthUser> activeUser = activeUsers.find(sessionId);
        if (activeUser.isPresent()) {
            return activeUser;
        }
        return dao.findBySessionId(sessionId);
    }

    @Override
    public boolean signInUser(String login, String password, String sessionId) {
        Optional<AuthUser> optional = dao.findByLogin(login);
        if (!optional.isPresent()) {
            return false;
        }

        AuthUser user = optional.get();
        if (user.isPasswordEquals(password)) {
            user.signIn(sessionId);
            this.activeUsers.add(user);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void signOut(String sessionId) {
        Optional<AuthUser> optional = this.find(sessionId);
        if (optional.isPresent()) {
            AuthUser user = optional.get();
            user.removeSession(sessionId);
            dao.update(user);
            this.activeUsers.remove(user);
        }
    }

    @Override
    public Optional<AuthUser> findByLogin(String login) {
        Optional<AuthUser> activeUser = activeUsers.findByLogin(login);
        if (activeUser.isPresent()) {
            return activeUser;
        }
        return dao.findByLogin(login);
    }

    @Override
    public AuthUser getById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AuthUser getByNickName(String nickName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<AuthUser> getAllUsers() {
        return dao.findAll();
    }

    @Override
    public void importUsers(AuthUser user) {
        dao.create(user);
    }

    @Override
    public boolean isUserActive(String sessionId) {
        return activeUsers.find(sessionId).isPresent();
    }
    
    @Override
    public void addActiveUser(AuthUser user) {
        this.activeUsers.add(user);
    }

}
