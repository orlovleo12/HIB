package com.project.dao.abstraction;

import com.project.model.FeedbackRequest;

import java.util.List;

public interface FeedbackRequestDao extends GenericDao<Long, FeedbackRequest> {

    List<FeedbackRequest> findAllByOrderByRepliedAsc();

    List<FeedbackRequest> getByReplied(Boolean replied);
}
