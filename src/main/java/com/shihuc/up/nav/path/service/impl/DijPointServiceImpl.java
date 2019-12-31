package com.shihuc.up.nav.path.service.impl;


import com.shihuc.up.nav.path.dao.DijEdgeMapper;
import com.shihuc.up.nav.path.dao.DijPointMapper;
import com.shihuc.up.nav.path.model.DijEdge;
import com.shihuc.up.nav.path.model.DijPoint;
import com.shihuc.up.nav.path.service.IDijPointService;
import com.shihuc.up.nav.path.util.DJMatrix;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: chengsh05
 * @Date: 2019/12/11 9:51
 */
@Service("dijPointService")
public class DijPointServiceImpl implements IDijPointService {

    private  Logger logger = Logger.getLogger(DijPointServiceImpl.class);

    @Autowired
    private DijPointMapper pointMapper;

    @Autowired
    private DijEdgeMapper edgeMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return pointMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public List<Integer> deletePointAndEdges(Integer id) {
        pointMapper.deleteByPrimaryKey(id);
        List<Integer> edges = edgeMapper.selectEdgeByPoint(id);
        edgeMapper.deleteEdgeByPoint(id);
        return edges;
    }

    @Override
    public int insert(DijPoint record) {
        return pointMapper.insert(record);
    }

    @Override
    public DijPoint selectByPrimaryKey(Integer id) {
        return pointMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DijPoint> selectAll() {
        return pointMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(DijPoint record) {
        return pointMapper.updateByPrimaryKey(record);
    }

    public List<DijPoint> calcNearestPath(int src, int dst) {

        List<DijPoint> points = pointMapper.selectAll();
        List<DijEdge> edges = edgeMapper.selectAll();
        logger.info("point count: " + points.size() + ", edge count: " + edges.size());
        //考虑到所有点的序号都是从1开始，所以，第0行和第0列都是空的
        int matrix[][] = new int[points.size()][points.size()];
        for (int i=0; i<points.size(); i++) {
            for (int j=0; j<points.size(); j++){
                matrix[i][j] = Integer.MAX_VALUE;
            }
            matrix[i][i] = 0;
        }

        /*
         * 做数据映射，将顶点数据放在一个连续的集合中，然后对应的边的端点和这个新的端点集合进行映射。
         * 这样做，主要目的是解决顶点编号和顶点数量不匹配的问题。例如：顶点集合只有5个顶点，但是顶点的
         * id编号是10-15. 此时不能用顶点编号作为邻接矩阵的下标，否则越界。
         */
        Map<Integer, Integer> pointToIdxMap = new HashMap<>();
        Map<Integer, Integer> idxToPointMap = new HashMap<>();
        for(int i=0; i<points.size(); i++){
            int pt = points.get(i).getId();
            pointToIdxMap.put(pt, i);
            idxToPointMap.put(i, pt);
        }

        int from = -1;
        int to = -1;
        for (int i = 0; i < edges.size(); i++) {
            DijEdge edge = edges.get(i);
            from = pointToIdxMap.get(edge.getPoint());
            to = pointToIdxMap.get(edge.getNeighbor());
            matrix[from][to] = edge.getWeight();
            matrix[to][from] = edge.getWeight();
        }
        int prev[] = new int[points.size()];
        int dist[] = new int[points.size()];
        int mappedSrc = pointToIdxMap.get(src);
        int mappedDst = pointToIdxMap.get(dst);
        DJMatrix.dijkstra(mappedSrc, matrix, prev, dist);
        Stack<Integer> out = new Stack<>();
        DJMatrix.calcPath(mappedSrc, mappedDst, prev, out);
        List<Integer> ids = new ArrayList<>();
        int resIdx = -1;
        while(!out.empty()) {
            resIdx = out.pop();
            ids.add(idxToPointMap.get(resIdx));
        }
        List<DijPoint> paths = getPoint(points, ids);
        return paths;
    }

    private List<DijPoint> getPoint(List<DijPoint> points, List<Integer> ids) {
        //下面这个方法，会导致顺序错误
        // List<DijPoint> dps = points.stream().filter(point -> ids.contains(point.getId())).collect(Collectors.toList());

        List<DijPoint> dps = new ArrayList<>();
        for (int i=0; i < ids.size(); i++){
            int idx = ids.get(i);
            for (DijPoint dp: points) {
                if (dp.getId() == idx) {
                    dps.add(dp);
                }
            }
        }

        return dps;
    }
}
