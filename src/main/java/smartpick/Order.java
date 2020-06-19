package smartpick;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Order_table")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String productName;
    private Long qty;
    private Long customerId;
    private String orderDate;
    private String status;
    private Long storeId;
    private String orderType;

    @PostPersist
    public void onPostPersist(){
        if ("PICK".equals(this.orderType)){
            Ordered ordered = new Ordered();
            BeanUtils.copyProperties(this, ordered);
            ordered.publishAfterCommit();
        }
    }

    @PostUpdate
    public void onPostUpdate(){
        if ("CANCELED".equals(this.status)){
            smartpick.external.Pick pick = new smartpick.external.Pick();
            pick.setOrderId(this.id);
            // mappings goes here
            pick = Application.applicationContext.getBean(smartpick.external.PickService.class)
                    .inquiry(this.id);

            if(pick != null){
                if ("CONFIRMED".equals(pick.getStatus()) || "PICKED".equals(pick.getStatus())){
                    throw new RuntimeException("취소 불가");
                }
            }

            Canceled canceled = new Canceled();
            BeanUtils.copyProperties(this, canceled);
            canceled.publishAfterCommit();
        }
    }

    @PreUpdate
    public void onPreUpdate(){

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }




}
