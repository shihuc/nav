package com.shihuc.up.nav.path.service.impl;

import com.shihuc.up.nav.path.dao.DijEdgeMapper;
import com.shihuc.up.nav.path.model.DijEdge;
import com.shihuc.up.nav.path.service.IDijEdgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: chengsh05
 * @Date: 2019/12/11 9:51
 */
@Service("dijEdgeService")
public class DijEdgeServiceImpl implements IDijEdgeService {
    @Autowired
    private DijEdgeMapper edgeMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return edgeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(DijEdge record) {
        return edgeMapper.insert(record);
    }

    @Override
    public DijEdge selectByPrimaryKey(Integer id) {
        return edgeMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<DijEdge> selectAll() {
        return edgeMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(DijEdge record) {
        return edgeMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Integer> selectEdgeByPoint(Integer id) {
        return edgeMapper.selectEdgeByPoint(id);
    }

    @Override
    public int deleteEdgeByPoint(Integer id){
        return edgeMapper.deleteEdgeByPoint(id);
    }
}
