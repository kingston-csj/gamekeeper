import axios from "../axios";

/*
 * 服务器节点分页查询
 */
export const findGamePage = () => {
  return axios({
    url: "/sdk/findGamePage",
    method: "get"
  });
};

/*
 * SDK账号分页查询
 */
export const findAccountPage = params => {
  return axios({
    url: "/sdk/findAccountPage",
    method: "get",
    params
  });
};

/*
 * SDK账号批量创建
 */
export const createAccountBatch = data => {
  return axios({
    url: "/sdk/createAccountBatch",
    method: "post",
    data
  });
};

/*
 * SDK账号更新
 */
export const updateAccountInfo = data => {
  return axios({
    url: "/sdk/updateAccountInfo",
    method: "post",
    data
  });
};

/*
 * SDK账号删除
 */
export const deleteAccount = params => {
  return axios({
    url: "/sdk/deleteAccount",
    method: "delete",
    params
  });
};

/*
 * SDK账号导出
 */
export const exportAccountPage = params => {
  return axios({
    url: "/sdk/exportAccountPage",
    method: "get",
    params,
    responseType: "blob"
  });
};

/*
 * 服务器保存节点
 */
export const saveNode = data => {
  return axios({
    url: "/sdk/saveNode",
    method: "post",
    data
  });
};

/*
 * 服务器新增节点
 */
export const deleteNode = params => {
  return axios({
    url: "/sdk/deleteNode",
    method: "delete",
    params: params
  });
};
