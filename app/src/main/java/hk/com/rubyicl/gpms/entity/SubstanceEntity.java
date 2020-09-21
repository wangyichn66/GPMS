package hk.com.rubyicl.gpms.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/7 18:24
 *     description: 对应具体物料所包含的物质
 *  <pre>
 */
public class SubstanceEntity extends LitePalSupport {
//    @Column(unique = true)              //是否唯一
//    @Column(defaultValue = "unknown")   //指定字段默认值
//    @Column(nullable = false)           //是否可以为空
//    @Column(ignore = true)              //是否可以忽略
    private long id;
    private String name;
    private String cas_no;
    private String content;
    private String usage;
    private MaterialEntity materialEntity;

    public MaterialEntity getMaterialEntity() {
        return materialEntity;
    }

    public void setMaterialEntity(MaterialEntity materialEntity) {
        this.materialEntity = materialEntity;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCas_no() {
        return cas_no;
    }

    public void setCas_no(String cas_no) {
        this.cas_no = cas_no;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "SubstanceEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", cas_no='" + cas_no + '\'' +
            ", content='" + content + '\'' +
            ", usage='" + usage + '\'' +
            ", materialEntity=" + materialEntity +
            '}';
    }
}
