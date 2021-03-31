import axios from '../axios'


/* 
 * 服务器节点分页查询
 */
export const findPage = () => {
    return axios({
        url: '/server/findPage',
        method: 'get'
    })
}


// 服务器id列表
export const loadServerIds = (data) => {
    return axios({
        url: '/server/serverIds',
        method: 'get',
        data
    })
}


// 后台命令查询
export const findCommands = (data) => {
    return axios({
        url: '/gameCmd/commands',
        method: 'get',
        data
    })
}

// 后台命令执行
export const execCommand = (data) => {
    return axios({
        url: '/gameCmd/exec',
        method: 'post',
        data
    })
}