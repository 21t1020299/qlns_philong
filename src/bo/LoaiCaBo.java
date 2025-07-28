package bo;

import java.util.ArrayList;
import bean.LoaiCaBean;
import dao.LoaiCaDao;

public class LoaiCaBo {
    LoaiCaDao dao = new LoaiCaDao();

    public ArrayList<LoaiCaBean> getList() throws Exception {
        return dao.getList();
    }
}