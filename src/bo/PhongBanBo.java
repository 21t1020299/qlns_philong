package bo;

import java.util.ArrayList;
import bean.PhongBanBean;
import dao.PhongBanDao;

public class PhongBanBo {
    PhongBanDao dao = new PhongBanDao();

    public ArrayList<PhongBanBean> getList() throws Exception {
        return dao.getList();
    }
}