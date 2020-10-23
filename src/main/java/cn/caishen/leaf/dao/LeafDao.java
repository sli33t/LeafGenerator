package cn.caishen.leaf.dao;

import cn.caishen.leaf.domain.LeafGen;
import cn.caishen.leaf.utils.LbMap;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author LB
 */
@Mapper
public interface LeafDao extends BaseMapper<LeafGen> {

    /**
     * 根据业务查询最大ID
     * @param businessType 业务
     * @return 最大ID
     */
    @Select("select id, max_id, step, gen_type, business_type, prefix, unit_id " +
            "from leaf_gen " +
            "where business_type = #{businessType}")
    LbMap getNextId(@Param("businessType") int businessType);

    /**
     * 更新最大ID
     * @param businessType 业务
     * @return 下一号段
     */
    @Update("update leaf_gen set max_id = max_id + step where business_type = #{businessType}")
    int updateMaxId(@Param("businessType") int businessType);
}
