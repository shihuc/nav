package com.shihuc.up.nav.path.service;

import com.shihuc.up.nav.path.model.DijEdge;

import java.util.List;

/**
 * @Author: chengsh05
 * @Date: 2019/12/11 9:50
 */
public interface IDijEdgeService {

    int deleteByPrimaryKey(Integer id);

    int insert(DijEdge record);

    DijEdge selectByPrimaryKey(Integer id);

    List<DijEdge> selectAll();

    int updateByPrimaryKey(DijEdge record);

    List<Integer> selectEdgeByPoint(Integer id);

    int deleteEdgeByPoint(Integer id);
}
