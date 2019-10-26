package com.kingston.jforgame.admin.payorder.controller;

import com.kingston.jforgame.admin.gamenode.vo.ServerNodeInfoList;
import com.kingston.jforgame.admin.payorder.vo.PayOrderVo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Kingston
 * @Date: 2019/10/26 20:08
 */
@RestController
@RequestMapping("/channel")
public class PayOrderController {

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public @ResponseBody
    List<PayOrderVo> getArticleByState(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                       @RequestParam(value = "pageSize", defaultValue = "10") Integer count,
                                       @RequestParam(value = "selectFrom") Long selectFrom,
                                       @RequestParam(value = "selectTo") Long selectTo) {

        System.out.println(selectFrom);
        return new ArrayList<>();
    }
}
