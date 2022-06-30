package ua.org.smit.authorizationtlx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

class ActiveUsersService {

    private final List<AuthUser> users = new ArrayList<>();

    Optional<AuthUser> find(String sessionId) {
        for (AuthUser user : users) {
            if (user.isHasSession(sessionId)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    Optional<AuthUser> findByLogin(String login) {
        for (AuthUser user : users) {
            if (user.getLogin().equals(login)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    void add(AuthUser user) {
        this.users.add(user);
    }

    void remove(AuthUser user) {
        Iterator<AuthUser> iterator = users.iterator();
        while (iterator.hasNext()) {
            AuthUser userSelect = iterator.next();
            if (userSelect.isThisUser(user)) {
                iterator.remove();
            }
        }
    }
}
