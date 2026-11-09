package training.restapi.demo1;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import training.restapi.demo1.models.TimeLog;
import training.restapi.demo1.models.TimeLogFilter;

import java.util.ArrayList;
import java.util.List;

public class TimeLogSpecification

{
    public static Specification<TimeLog> withFilter(TimeLogFilter filter){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(query.getResultType() != Long.class && query.getResultType() != long.class){
                root.fetch("user", JoinType.LEFT);
                root.fetch("project", JoinType.LEFT);
            }

            if(filter.getId() != null){
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), filter.getId()));
            }

            if(filter.getStartDate() !=null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("logDate"), filter.getStartDate()));
            }

            if(filter.getEndDate() !=null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("logDate"), filter.getEndDate()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }

        );

    }
}
