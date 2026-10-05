package jforgame.admin.asynctask.dao;

import jforgame.admin.domain.AsyncTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AsyncTaskDao extends JpaRepository<AsyncTask, Long>, JpaSpecificationExecutor<AsyncTask> {

    Optional<AsyncTask> findByTaskId(String taskId);
}
