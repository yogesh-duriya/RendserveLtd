package rendserve.rendserveltd.Pojo;

/**
 * Created by abhi on 04-02-2018.
 */

public class StatePojo {

    private int id;
    private String stateName;
    private boolean isCheck=false;

    public StatePojo(String stateName, int id) {

        this.id = id;
        this.stateName = stateName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
