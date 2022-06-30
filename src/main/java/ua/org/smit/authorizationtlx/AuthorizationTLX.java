package ua.org.smit.authorizationtlx;

import java.util.List;
import java.util.Optional;

public interface AuthorizationTLX {

    Optional<AuthUser> find(String sessionId);

    boolean signInUser(String login, String password, String sessionId);

    void signOut(String sessionId);

    Optional<AuthUser> findByLogin(String login);

    AuthUser getById(int id);

    AuthUser getByNickName(String nickName);

    List<AuthUser> getAllUsers();
    
    void importUsers(AuthUser user);
    
    boolean isUserActive(String sessionId);
    
    void addActiveUser(AuthUser user);

}
