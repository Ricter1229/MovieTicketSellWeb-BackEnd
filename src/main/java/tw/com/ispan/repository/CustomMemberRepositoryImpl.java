package tw.com.ispan.repository;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import tw.com.ispan.domain.MemberBean;

public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MemberBean> find(JSONObject obj) {
        Integer id = obj.isNull("id") ? null : obj.getInt("id");
        String account = obj.isNull("account") ? null : obj.getString("account");

        Integer start = obj.isNull("start") ? null : obj.getInt("start");
        Integer max = obj.isNull("max") ? null : obj.getInt("max");
        String order = obj.isNull("order") ? "id" : obj.getString("order");
        boolean dir = obj.isNull("dir") ? false : obj.getBoolean("dir");

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MemberBean> criteriaQuery = criteriaBuilder.createQuery(MemberBean.class);
        Root<MemberBean> table = criteriaQuery.from(MemberBean.class);

        List<Predicate> predicates = new ArrayList<>();
        if (id != null) {
            predicates.add(criteriaBuilder.equal(table.get("id"), id));
        }
        if (account != null) {
            predicates.add(criteriaBuilder.like(table.get("account"), "%" + account + "%"));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        if (dir) {
            criteriaQuery.orderBy(criteriaBuilder.desc(table.get(order)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(table.get(order)));
        }

        return this.entityManager.createQuery(criteriaQuery)
                .setFirstResult(start != null ? start : 0)
                .setMaxResults(max != null ? max : Integer.MAX_VALUE)
                .getResultList();
    }

    @Override
    public long count(JSONObject obj) {
        Integer id = obj.isNull("id") ? null : obj.getInt("id");
        String account = obj.isNull("account") ? null : obj.getString("account");

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<MemberBean> table = criteriaQuery.from(MemberBean.class);

        List<Predicate> predicates = new ArrayList<>();
        if (id != null) {
            predicates.add(criteriaBuilder.equal(table.get("id"), id));
        }
        if (account != null) {
            predicates.add(criteriaBuilder.like(table.get("account"), "%" + account + "%"));
        }

        criteriaQuery.select(criteriaBuilder.count(table));
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return this.entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}