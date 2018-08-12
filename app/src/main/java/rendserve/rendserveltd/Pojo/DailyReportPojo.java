package rendserve.rendserveltd.Pojo;

import java.util.ArrayList;

/**
 * Created by abhi on 19-03-2018.
 */

public class DailyReportPojo extends BasePojo {

    public ArrayList<ReportDataPojo> getData() {
        return data;
    }

    public void setData(ArrayList<ReportDataPojo> data) {
        this.data = data;
    }

    private ArrayList<ReportDataPojo> data;


}
