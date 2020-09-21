package hk.com.rubyicl.gpms.entity;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/18 上午 10:50
 *     description: 法规的表的实体类
 *  <pre>
 */
public class RegulationEntity extends LitePalSupport {
    private long id;            //数据库表的ID 自增 无法修改
    private String name;        //法规的名字
    private String remarks;     //法规的备注
    private List<RegulationItemEntity> regulationItemEntityList = new ArrayList<>();


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<RegulationItemEntity> getRegulationItemEntityList() {
        return regulationItemEntityList;
    }

    public void setRegulationItemEntityList(List<RegulationItemEntity> regulationItemEntityList) {
        this.regulationItemEntityList = regulationItemEntityList;
    }
}
