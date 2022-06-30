package ua.org.smit.authorizationtlx;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import ua.org.smit.authorizationtlx.dao.Dao;
import ua.org.smit.authorizationtlx.password.PasswordService;

@Entity
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private int alias;
    private String login;
    private String nickName;
    private String password;
    private String email;
    private boolean banned;
    private Timestamp registration;
    private Timestamp lastLogin;
    private Type type;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "userId")
    private List<UserSession> sessions = new ArrayList<>();

    @Transient
    private final Dao dao = new Dao(AuthUser.class);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlias() {
        return alias;
    }

    public void setAlias(int alias) {
        this.alias = alias;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public Timestamp getRegistration() {
        return registration;
    }

    public void setRegistration(Timestamp registration) {
        this.registration = registration;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<UserSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<UserSession> sessions) {
        this.sessions = sessions;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    void removeSession(String sessionId) {
        Iterator<UserSession> i = sessions.iterator();
        while (i.hasNext()) {
            UserSession session = i.next();
            if (session.getSession().equals(sessionId)) {
                i.remove();
            }

        }
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "AuthUser{" + "id=" + id + ", alias=" + alias + ", login=" + login + ", nickName=" + nickName + ", password=" + password + ", email=" + email + ", banned=" + banned + ", registration=" + registration + ", lastLogin=" + lastLogin + ", type=" + type + ", sessions=" + sessions + ", dao=" + dao + '}';
    }

    

    public boolean isPasswordEquals(String password) {
        return new PasswordService().isPasswordsEquals(password, this.password);
    }
    
    public void setHashedPassword(String password) {
        this.password = new PasswordService().getSaltedHash(password);
    }

    public void signIn(String sessionId) {
        Timestamp time = new Timestamp(System.currentTimeMillis());

        UserSession session = new UserSession();
        session.setSession(sessionId);
        session.setTime(time);
        this.sessions.add(session);

        lastLogin = time;
        dao.update(this);
    }

    boolean isHasSession(String sessionId) {
        for (UserSession userSession : sessions) {
            if (userSession.equalSession(sessionId)) {
                return true;
            }
        }
        return false;
    }

    boolean isThisUser(AuthUser user) {
        return this.id == user.getId();
    }
    
    

}
