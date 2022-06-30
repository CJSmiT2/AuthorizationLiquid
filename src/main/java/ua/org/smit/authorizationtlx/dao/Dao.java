package ua.org.smit.authorizationtlx.dao;

import java.util.List;
import java.util.Optional;
import org.hibernate.query.Query;
import ua.org.smit.authorizationtlx.AuthUser;

public class Dao extends AbstractHibernateDAO< AuthUser> {

    public Dao(Class<AuthUser> aClass) {
        setClazz(aClass);
    }

    public Optional<AuthUser> findBySessionId(String sessionId) {
        Optional<AuthUser> result = Optional.empty();
        this.openCurrentSession();
        String hql = "select E from " + clazz.getName() + " E LEFT JOIN E.sessions A WHERE A.session = :sessionId";
//        SELECT a, b FROM Author a LEFT JOIN a.books b WHERE a.lastName = 'Janssen'
        Query query = currentSession.createQuery(hql);
        query.setParameter("sessionId", sessionId);
        query.setMaxResults(1);
        List<AuthUser> items = query.getResultList();
        
        if (!query.getResultList().isEmpty()) {
            AuthUser entity = (AuthUser) query.getSingleResult();
            result = Optional.ofNullable(entity);
        }
        
        this.closeCurrentSession();
        return result;
    }
    
    public Optional<AuthUser> findByLogin(String login) {
        Optional<AuthUser> result = Optional.empty();

        this.openCurrentSession();
        String hql = "from " + clazz.getName() + " E where E.login = :login";
        Query query = currentSession.createQuery(hql);
        query.setParameter("login", login);
        query.setMaxResults(1);

        if (!query.getResultList().isEmpty()) {
            AuthUser entity = (AuthUser) query.getSingleResult();
            result = Optional.ofNullable(entity);
        }

        this.closeCurrentSession();

        return result;
    }
}
