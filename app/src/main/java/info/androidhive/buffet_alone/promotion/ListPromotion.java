package info.androidhive.buffet_alone.promotion;

/**
 * Created by AndroidBash on 09/05/2016.
 */
public class ListPromotion {

    private int id;
    private String promotionName;
    private String imageLink;
    private String promotionGenre;
    private String pro_id;

    public ListPromotion(String promotionName, String imageLink, String promotionGenre , String pro_id) {

        this.promotionName = promotionName;
        this.imageLink = imageLink;
        this.promotionGenre = promotionGenre;
        this.pro_id = pro_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPromotionGenre() {
        return promotionGenre;
    }

    public void setPromotionGenre(String promotionGenre) {
        this.promotionGenre = promotionGenre;
    }

    public String getpro() {
        return pro_id;
    }

    public void setpro(String id) {
        this.pro_id = id;
    }

}
