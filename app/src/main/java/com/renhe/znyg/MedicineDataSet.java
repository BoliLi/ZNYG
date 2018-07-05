package com.renhe.znyg;

import java.util.List;
import java.util.Map;

public class MedicineDataSet {

    private List<Medicine> data;
    private List<List<String>> categorySet;
    private static MedicineDataSet instance = new MedicineDataSet();
    private MedicineDataSet() {

    }

    public static MedicineDataSet getInstance() {
        return instance;
    }
}
