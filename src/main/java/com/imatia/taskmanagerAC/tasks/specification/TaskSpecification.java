package com.imatia.taskmanagerAC.tasks.specification;


import com.imatia.taskmanagerAC.tasks.model.TaskEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

/**
 * Utility class providing specifications for querying and filtering TaskEntity objects.
 * This class contains static methods for building JPA Specifications based on various filters
 * such as task name, completion status, date range, and sorting order.
 */
public class TaskSpecification {

    /**
     * Creates a specification to filter tasks by name.
     * The filter performs a case-insensitive search for tasks whose name contains the specified substring.
     *
     * @param name the substring to search for in task names; if null, no filtering is applied.
     * @return a Specification object for filtering by task name.
     */
    public static Specification<TaskEntity> filterByName(String name) {
        return (Root<TaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (name != null) {
                return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
            }
            return cb.conjunction();
        };
    }

    /**
     * Creates a specification to filter tasks by completion status.
     *
     * @param completed the completion status to filter by; if null, no filtering is applied.
     * @return a Specification object for filtering by task completion status.
     */
    public static Specification<TaskEntity> filterByCompleted(Boolean completed) {
        return (Root<TaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (completed != null) {
                return cb.equal(root.get("completed"), completed);
            }
            return cb.conjunction();
        };
    }

    /**
     * Creates a specification to filter tasks within a specified date range.
     * The filter checks the `creationDate` field against the provided start and end dates.
     *
     * @param startDate the starting date of the range; if null, no lower bound is applied.
     * @param endDate   the ending date of the range; if null, no upper bound is applied.
     * @return a Specification object for filtering tasks by date range.
     */
    public static Specification<TaskEntity> filterByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (Root<TaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (startDate != null && endDate != null) {
                return cb.between(root.get("creationDate"), startDate, endDate);
            } else if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get("creationDate"), startDate);
            } else if (endDate != null) {
                return cb.lessThanOrEqualTo(root.get("creationDate"), endDate);
            }
            return cb.conjunction();
        };
    }

    /**
     * Creates a specification to sort tasks by completion status and creation date.
     * Tasks are ordered by descending creation date.
     *
     * @return a Specification object for sorting tasks by completion status and creation date.
     */
    public static Specification<TaskEntity> orderByCompletedAndDate() {
        return (Root<TaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            query.orderBy(
                    cb.desc(root.get("creationDate"))
            );
            return cb.conjunction();
        };
    }
}
