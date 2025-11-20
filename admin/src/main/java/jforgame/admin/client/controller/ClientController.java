package jforgame.admin.client.controller;

import jforgame.admin.http.HttpResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    @RequestMapping(value = "/getVersion", method = RequestMethod.GET)
    public HttpResult getVersion() {
        return HttpResult.ok("1.0.0");
    }
}
