package bo;

import java.util.ArrayList;
import bean.ChucVuBean;
import dao.ChucVuDao;

public class ChucVuBo {
    ChucVuDao dao = new ChucVuDao();

    public ArrayList<ChucVuBean> getList() throws Exception {
        return dao.getList();
    }
}