package bo;

import java.util.ArrayList;

import bean.ChuyenKhoang;
import dao.ChuyenKhoangDao;

public class ChuyenKhoangBo {
    ChuyenKhoangDao dao = new ChuyenKhoangDao();

    public ArrayList<ChuyenKhoang> getList() throws Exception {
        return dao.getList();
    }
}
