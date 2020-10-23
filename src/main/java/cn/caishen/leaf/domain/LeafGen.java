package cn.caishen.leaf.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author LB
 */
@Data
@TableName(value = "leaf_gen")
public class LeafGen {

    /**
     * 主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 最大编号
     */
    @TableField(value = "max_id")
    private Long maxId;

    /**
     * 递增数量
     */
    @TableField(value = "step")
    private Integer step;

    /**
     * 生成类型：0-ID,1-NO单据号
     */
    @TableField(value = "gen_type")
    private Integer genType;

    /**
     * 业务类型
     */
    @TableField(value = "business_type")
    private Integer businessType;

    /**
     * 业务前缀
     */
    @TableField(value = "prefix")
    private String prefix;

    /**
     * 单位
     */
    @TableField(value = "unit_id")
    private Integer unitId;
}
