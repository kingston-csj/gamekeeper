import axios from "../axios";

/*
 * 机器节点分页查询
 */
export const findPage = () => {
  return axios({
    url: "/machine/findPage",
    method: "get"
  });
};

/*
 * 机器节点保存
 */
export const saveNode = data => {
  return axios({
    url: "/machine/saveNode",
    method: "post",
    data
  });
};

/*
 * 机器节点删除
 */
export const deleteNode = params => {
  return axios({
    url: "/machine/deleteNode",
    method: "delete",
    params
  });
};

/*
 * 机器节点列表
 */
export const loadServerIds = data => {
  return axios({
    url: "/machine/serverIds",
    method: "get",
    data
  });
};

export const monitorLatest = params => {
  return axios({
    url: "/monitor/machine/latest",
    method: "get",
    params
  });
};

export const monitorHistory = params => {
  return axios({
    url: "/monitor/machine/history",
    method: "get",
    params
  });
};

export const monitorOverview = () => {
  return axios({
    url: "/monitor/machine/overview",
    method: "get"
  });
};
