package ua.org.smit.authorizationtlx.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class AbstractHibernateDAO<T> {

    Class<T> clazz;
    Transaction currentTransaction;
    Session currentSession;

    public final void setClazz(final Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    public T findOne(int id) {
        this.openCurrentSession();
        T entity = currentSession.get(clazz, id);
        this.closeCurrentSession();
        return entity;
    }

    public List<T> findAll() {
        this.openCurrentSession();
        List<T> items = currentSession.createQuery("from " + clazz.getName()).list();
        this.closeCurrentSession();
        return items;
    }

    public T create(final T entity) {
        this.openCurrentSessionwithTransaction();
        currentSession.saveOrUpdate(entity);
        this.closeCurrentSessionwithTransaction();
        return entity;
    }

    public T update(final T entity) {
        this.openCurrentSessionwithTransaction();
        T item = (T) currentSession.merge(entity);
        this.closeCurrentSessionwithTransaction();
        return item;
    }

    public void delete(final T entity) {
        this.openCurrentSessionwithTransaction();
        currentSession.delete(entity);
        this.closeCurrentSessionwithTransaction();
    }

    public void deleteById(int entityId) {
        this.openCurrentSessionwithTransaction();
        final T entity = findOne(entityId);
        delete(entity);
        this.closeCurrentSessionwithTransaction();
    }

    public List<Integer> getAllIds() {
        this.openCurrentSession();
        String hql = "select " + clazz.getName() + ".id from " + clazz.getName();
        List<Integer> items = currentSession.createQuery(hql).list();
        this.closeCurrentSession();
        return items;
    }

    void openCurrentSession() {
        currentSession = HibernateUtil.getSessionFactory().openSession();
    }

    void openCurrentSessionwithTransaction() {
        currentSession = HibernateUtil.getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
    }

    void closeCurrentSession() {
        currentSession.close();
    }

    void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

}
