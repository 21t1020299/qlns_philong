package bo;

import java.util.List;
import bean.CaBean;
import dao.CaDao;

public class CaBo {
    CaDao caDao = new CaDao();

    public List<CaBean> getAllCa() throws Exception {
        return caDao.getAllCa();
    }
}
