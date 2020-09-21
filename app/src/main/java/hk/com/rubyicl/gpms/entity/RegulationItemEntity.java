package hk.com.rubyicl.gpms.entity;

import org.litepal.crud.LitePalSupport;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/18 上午 10:43
 *     description: 法规每行有哪些内容
 *  <pre>
 */
public class RegulationItemEntity extends LitePalSupport {
    private long id;    //数据库表的ID 自增 无法修改
    private String No;     //对应法规的 No号
    private String substances_name_cn;  //法规条目 物资中文名称
    private String substances_name_eg;   //法规条目的 物资英文名称
    private String CAS_No;  //对应法规CAS NO.
    private String threshold;   //限制要求/参考阈值/Threshold
    private RegulationEntity regulationEntity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getSubstances_name_cn() {
        return substances_name_cn;
    }

    public void setSubstances_name_cn(String substances_name_cn) {
        this.substances_name_cn = substances_name_cn;
    }

    public String getSubstances_name_eg() {
        return substances_name_eg;
    }

    public void setSubstances_name_eg(String substances_name_eg) {
        this.substances_name_eg = substances_name_eg;
    }

    public String getCAS_No() {
        return CAS_No;
    }

    public void setCAS_No(String CAS_No) {
        this.CAS_No = CAS_No;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public RegulationEntity getRegulationEntity() {
        return regulationEntity;
    }

    public void setRegulationEntity(RegulationEntity regulationEntity) {
        this.regulationEntity = regulationEntity;
    }
}
