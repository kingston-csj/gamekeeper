import axios from "../axios";

// 查询玩家信息
export const queryPlayer = params => {
  return axios({
    url: "/gameCmd/simplyPlayer",
    method: "get",
    params
  });
};

// 查询公会信息
export const queryGuild = params => {
  return axios({
    url: "/gameCmd/simplyGuild",
    method: "get",
    params
  });
};

// 玩家封禁
export const banPlayer = data => {
  return axios({
    url: "/gameCmd/banPlayer",
    method: "post",
    data
  });
};

// 玩家封禁
export const cancelBanPlayer = data => {
  return axios({
    url: "/gameCmd/banPlayer",
    method: "post",
    data
  });
};
