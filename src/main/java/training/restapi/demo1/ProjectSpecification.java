package training.restapi.demo1;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import training.restapi.demo1.models.Project;
import training.restapi.demo1.models.ProjectFilter;

import java.util.ArrayList;
import java.util.List;

public class ProjectSpecification {

    public static Specification<Project> withFilter(ProjectFilter filter) {
        return ((root, query,criteriaBuilder)->{
            List<Predicate> predicates = new ArrayList<>();

            if(filter.getActive() != null){
                predicates.add(criteriaBuilder.equal(root.get("isActive"), filter.getActive()));
            }

            if(filter.getDeadlineAfter() != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("deadLine"), filter.getDeadlineAfter()));
            }

            if(filter.getDeadlineBefore() != null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("deadLine"), filter.getDeadlineBefore()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }
}
