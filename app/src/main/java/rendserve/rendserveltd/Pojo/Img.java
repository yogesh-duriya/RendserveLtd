package rendserve.rendserveltd.Pojo;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by abhi on 22-03-2018.
 */

public class Img implements Serializable {
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private transient Bitmap bitmap;
}
