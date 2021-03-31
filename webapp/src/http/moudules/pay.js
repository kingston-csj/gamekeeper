import axios from '../axios'


/* 
 * 订单查询
 */
export const findPage = (params) => {
    return axios({
        url: '/pay/order',
        method: 'get',
        params
    })
}

/* 
 * 订单统计
 */
export const statistics = (params) => {
    return axios({
        url: '/pay/statistics',
        method: 'get',
        params
    })
}