package com.shihuc.up.nav.path.controller;

import com.shihuc.up.nav.path.model.DijEdge;
import com.shihuc.up.nav.path.service.IDijEdgeService;
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
public class EdgeController {

    private Logger logger = Logger.getLogger(EdgeController.class);

    @Autowired
    @Qualifier("dijEdgeService")
    private IDijEdgeService edgeService;

    @RequestMapping("/edge/save")
    @ResponseBody
    public Map<String, Integer> save(HttpServletRequest req) {
        int point = Integer.valueOf(req.getParameter("from"));
        int neighbor = Integer.valueOf(req.getParameter("to"));
        int weight = Integer.valueOf(req.getParameter("len"));
        logger.info("x: " + point + ", y:" + neighbor);
        DijEdge edge = new DijEdge();
        edge.setPoint(point);
        edge.setNeighbor(neighbor);
        edge.setWeight(weight);
        edgeService.insert(edge);
        Map<String, Integer> data = new HashMap<>();
        data.put("info", edge.getId());
        logger.info("id: " + edge.getId() + ", point: " + edge.getPoint() + ", neighbor:" + edge.getNeighbor() + ", weight: " + edge.getWeight());
        return data;
    }

    @RequestMapping("/edge/getAll")
    @ResponseBody
    public List<DijEdge> getAll() {
        List<DijEdge> edges = edgeService.selectAll();
        logger.info("point count: " + edges.size());
        return edges;
    }

    @RequestMapping("/edge/del")
    @ResponseBody
    public boolean del(HttpServletRequest req) {
        int id = Integer.valueOf(req.getParameter("id"));
        int res = edgeService.deleteByPrimaryKey(id);
        logger.info("del edge: " + id + ", " + res);
        return true;
    }
}
