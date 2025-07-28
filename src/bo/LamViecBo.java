package bo;

import java.util.ArrayList;

import bean.LamViecBean;
import dao.LamViecDao;

public class LamViecBo {
    LamViecDao dao = new LamViecDao();

    public ArrayList<LamViecBean> getList() throws Exception {
        return dao.getList();
    }
}
