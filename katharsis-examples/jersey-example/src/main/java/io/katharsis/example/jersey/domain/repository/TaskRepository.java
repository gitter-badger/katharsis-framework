package io.katharsis.example.jersey.domain.repository;

import io.katharsis.example.jersey.domain.model.Task;
import io.katharsis.queryParams.QueryParams;
import io.katharsis.repository.ResourceRepository;

import java.util.Arrays;

public class TaskRepository implements ResourceRepository<Task, Long> {
    @Override
    public <S extends Task> S save(S entity) {
        return null;
    }

    @Override
    public Task findOne(Long aLong, QueryParams requestParams) {
        Task task = new Task(aLong, "Some task");
        return task;
    }

    @Override
    public Iterable<Task> findAll(QueryParams requestParams) {
        return findAll(null, requestParams);
    }

    @Override
    public Iterable<Task> findAll(Iterable<Long> taskIds, QueryParams requestParams) {
        return Arrays.asList(new Task(1L, "First task"));
    }

    @Override
    public void delete(Long aLong) {

    }
}
