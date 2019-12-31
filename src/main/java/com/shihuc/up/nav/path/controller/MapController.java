package com.shihuc.up.nav.path.controller;

import com.shihuc.up.nav.path.model.DijPoint;
import com.shihuc.up.nav.path.service.IDijPointService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: chengsh05
 * @Date: 2019/12/11 10:16
 */
@Controller
@RequestMapping("/path")
public class MapController {

    private Logger logger = Logger.getLogger(MapController.class);

    @RequestMapping("/map")
    public String map() {
        return "path/map";
    }

    @Autowired
    @Qualifier("dijPointService")
    private IDijPointService pointService;

    @RequestMapping("/go")
    @ResponseBody
    public List<DijPoint> go(HttpServletRequest req) {
        int src = Integer.valueOf(req.getParameter("srcId"));
        int dst = Integer.valueOf(req.getParameter("dstId"));
        logger.info("src: " + src + ", dst: " + dst);
        List<DijPoint> paths = pointService.calcNearestPath(src, dst);
        return  paths;
    }
}
