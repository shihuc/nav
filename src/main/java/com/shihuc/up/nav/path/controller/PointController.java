package com.shihuc.up.nav.path.controller;

import com.shihuc.up.nav.path.model.DijPoint;
import com.shihuc.up.nav.path.service.IDijEdgeService;
import com.shihuc.up.nav.path.service.IDijPointService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: chengsh05
 * @Date: 2019/12/11 10:07
 */
@Controller
@RequestMapping("/path")
public class PointController {

    private Logger logger = Logger.getLogger(PointController.class);

    @Autowired
    @Qualifier("dijPointService")
    private IDijPointService pointService;

    @Autowired
    @Qualifier("dijEdgeService")
    private IDijEdgeService edgeService;

    @RequestMapping("/point/save")
    @ResponseBody
    public Map<String, Integer> save(HttpServletRequest req) {
        int px = Integer.valueOf(req.getParameter("pointx"));
        int py = Integer.valueOf(req.getParameter("pointy"));
        logger.info("x: " + px + ", y:" + py);
        DijPoint point = new DijPoint();
        point.setPointx(px);
        point.setPointy(py);
        pointService.insert(point);
        Map<String, Integer> data = new HashMap<>();
        data.put("info", point.getId());
        logger.info("id: " + point.getId() + ", x: " + point.getPointx() + ", y:" + point.getPointy());
        return data;
    }

    @RequestMapping("/point/getAll")
    @ResponseBody
    public List<DijPoint> getAll() {
        List<DijPoint> points = pointService.selectAll();
        logger.info("point count: " + points.size());
        return points;
    }

    @RequestMapping("/point/del")
    @ResponseBody
    public List<Integer> del(HttpServletRequest req) {
        int id = Integer.valueOf(req.getParameter("id"));
        List<Integer> edges = pointService.deletePointAndEdges(id);
        logger.info("del point: " + id + ", edge count: " + edges.size());
        return edges;
    }
}
