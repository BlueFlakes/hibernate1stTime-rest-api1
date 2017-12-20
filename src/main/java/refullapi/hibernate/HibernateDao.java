package refullapi.hibernate;


import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.function.BiConsumer;

class HibernateDao<T> {

    public void remove(T obj) {
        BiConsumer<T, Session> removeObj = (o, session) -> session.remove(o);
        apply(obj, removeObj);
    }

    public void update(T obj) {
        BiConsumer<T, Session> updateObj = (o, session) -> session.update(o);
        apply(obj, updateObj);
    }

    public void saveToDatabase(T obj) {
        BiConsumer<T, Session> saveObj = (o, session) -> session.save(o);
        apply(obj, saveObj);
    }

    private void apply(T obj, BiConsumer<T, Session> consumer) {
        Session session = HibernateUtils.getSessionFactory().openSession();

        try {
            session.getTransaction().begin();

            consumer.accept(obj, session);

            session.getTransaction().commit();
            session.close();

        } catch (HibernateException e) {
            session.getTransaction().rollback();
        }
    }

    public T get(Class<T> givenClass, Integer userId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.getTransaction().begin();
        T obj = session.get(givenClass, userId);
        session.close();

        return obj;
    }

    public List<T> getAll(Class<T> givenClass) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.getTransaction().begin();

        CriteriaBuilder builder = session.getCriteriaBuilder();

        CriteriaQuery<T> criteria = builder.createQuery(givenClass);
        Root<T> root = criteria.from(givenClass);
        criteria.select( root );

        List<T> objContainer = session.createQuery( criteria ).getResultList();

        session.close();
        return objContainer;
    }
}
