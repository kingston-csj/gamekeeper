import axios from "../axios";

export const queryItemLog = params => {
  return axios({
    url: "/gamelog/query",
    method: "post",
    data: params
  });
};

export const queryExceptionLog = params => {
  return axios({
    url: "/gamelog/queryException",
    method: "post",
    data: params
  });
};

export const queryLogTypes = params => {
  return axios({
    url: "/gamelog/logTypes",
    method: "get",
    params
  });
};
