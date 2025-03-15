import axios from "../axios";

// 查询所有图片
export const queryAllPictures = params => {
  return axios({
    url: "/file/allPicture",
    method: "get",
    params
  });
};

// 上传图片
export const uploadPic = (data, headers) => {
  return axios({
    url: "/file/uploadPic",
    method: "post",
    data,
    headers: headers
  });
};

// 查询所有字体
export const queryAllFonts = params => {
  return axios({
    url: "/file/allFont",
    method: "get",
    params
  });
};

// 上传字体
export const uploadFont = (data, headers) => {
  return axios({
    url: "/file/uploadFont",
    method: "post",
    data,
    headers: headers
  });
};
