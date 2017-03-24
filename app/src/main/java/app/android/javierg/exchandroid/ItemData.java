package app.android.javierg.exchandroid;

/**
 * Created by javierg on 22/03/2017.
 */

public class ItemData {
    String text;
    Integer imageId;
    String code;

    public ItemData(String text, Integer imageId, String code){
        this.text=text;
        this.imageId=imageId;
        this.code=code;
    }

    public String getText(){
        return text;
    }

    public Integer getImageId(){
        return imageId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
