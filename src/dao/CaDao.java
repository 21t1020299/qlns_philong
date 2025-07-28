package dao;

import java.util.ArrayList;
import java.util.List;
import bean.CaBean;

public class CaDao {
    public List<CaBean> getAllCa() throws Exception {
        List<CaBean> list = new ArrayList<>();
        list.add(new CaBean(1, "Ca Sáng"));
        list.add(new CaBean(2, "Ca Chiều"));
        return list;
    }
}
