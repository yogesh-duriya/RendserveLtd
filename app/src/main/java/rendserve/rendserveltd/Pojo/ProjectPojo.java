package rendserve.rendserveltd.Pojo;

import java.util.ArrayList;

/**
 * Created by abhi on 19-03-2018.
 */

public class ProjectPojo extends BasePojo {

    public ArrayList<ProjectDataPojo> getData() {
        return data;
    }

    public void setData(ArrayList<ProjectDataPojo> data) {
        this.data = data;
    }

    private ArrayList<ProjectDataPojo> data;
}
