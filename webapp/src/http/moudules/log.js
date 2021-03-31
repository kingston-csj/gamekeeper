import axios from '../axios'

/* 
 * 日志管理模块
 */

// 分页查询
export const findPage = (data) => {
    return axios({
        url: '/log/findPage',
        method: 'post',
        data
    })
}