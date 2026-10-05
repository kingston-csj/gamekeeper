import axios from "../axios";

export const createBackupTask = data => {
  return axios({
    url: "/api/async-task/submit",
    method: "post",
    data
  });
};

export const queryCurrentBackupTask = params => {
  return axios({
    url: "/api/async-task/list",
    method: "get",
    params
  });
};

export const queryTaskDetail = taskId => {
  return axios({
    url: "/api/async-task/" + taskId,
    method: "get"
  });
};

export const queryBackupHistory = params => {
  return axios({
    url: "/api/async-task/list",
    method: "get",
    params
  });
};

export const downloadBackupFile = taskId => {
  return axios({
    url: "/yunwei/backup/download",
    method: "get",
    params: { taskId },
    responseType: "blob"
  });
};

export const deleteBackupResource = params => {
  return axios({
    url: "/yunwei/backup/delete",
    method: "delete",
    params
  });
};
