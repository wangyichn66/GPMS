package hk.com.rubyicl.gpms.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author wangyi
 *     create time: 2020/9/4 上午 10:54
 *     description: 对应物料的实体类
 *  <pre>
 */
public class MaterialEntity extends LitePalSupport {
    private long id;
    private String name;
    private String brand;
    private String type;
    private String compliance;  //合规性
    private String time;
    private List<SubstanceEntity> substanceEntityList = new ArrayList<>();

    public long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompliance() {
        return compliance;
    }

    public void setCompliance(String compliance) {
        this.compliance = compliance;
    }

    public List<SubstanceEntity> getSubstanceEntityList() {
        return substanceEntityList;
    }

    public void setSubstanceEntityList(List<SubstanceEntity> substanceEntityList) {
        this.substanceEntityList = substanceEntityList;
    }

    @Override
    public String toString() {
        return "MaterialEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", brand='" + brand + '\'' +
            ", type='" + type + '\'' +
            ", compliance='" + compliance + '\'' +
            ", time='" + time + '\'' +
            ", substanceEntityList=" + substanceEntityList +
            '}';
    }
}
