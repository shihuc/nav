package com.shihuc.up.nav.path.dao;

import com.shihuc.up.nav.path.model.DijEdge;

import java.util.List;

public interface DijEdgeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dij_edge
     *
     * @mbg.generated Wed Dec 11 09:18:09 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dij_edge
     *
     * @mbg.generated Wed Dec 11 09:18:09 CST 2019
     */
    int insert(DijEdge record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dij_edge
     *
     * @mbg.generated Wed Dec 11 09:18:09 CST 2019
     */
    DijEdge selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dij_edge
     *
     * @mbg.generated Wed Dec 11 09:18:09 CST 2019
     */
    List<DijEdge> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dij_edge
     *
     * @mbg.generated Wed Dec 11 09:18:09 CST 2019
     */
    int updateByPrimaryKey(DijEdge record);

    List<Integer> selectEdgeByPoint(Integer id);

    int deleteEdgeByPoint(Integer id);
}