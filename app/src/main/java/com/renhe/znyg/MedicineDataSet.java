package com.renhe.znyg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MedicineDataSet {

    private List<Medicine> medData;
    private List<Medicine> outputData;
    private List<String> category;
    private List<List<Medicine>> medTemp;
    private List<List<String>> outputOption2;
    private List<List<List<String>>> outputOption3;
    private static MedicineDataSet instance = new MedicineDataSet();
    private MedicineDataSet() {
        category = new ArrayList<>();
        category.add("药品分类1");
        category.add("药品分类2");

        medTemp = new ArrayList<>();
        List<Medicine> list1 = new ArrayList<>();
        list1.add(new Medicine(category.get(0), "药品1", "描述1"));
        list1.add(new Medicine(category.get(0), "药品2", "描述2"));
        medTemp.add(list1);
        List<Medicine> list2 = new ArrayList<>();
        list2.add(new Medicine(category.get(1), "药品3", "描述3"));
        list2.add(new Medicine(category.get(1), "药品4", "描述4"));
        medTemp.add(list2);

        medData = new ArrayList<>();
        outputData = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Medicine m = null;
        Date date = null;
        try {
            date = dateFormat.parse("2019-06-01");
        } catch (Exception e) {
            e.printStackTrace();
        }

        m = new Medicine(category.get(0), "药品1", "描述1");
        m.add(3, date);
        medData.add(m);

        try {
            date = dateFormat.parse("2009-06-01");
        } catch (Exception e) {
            e.printStackTrace();
        }

        m = new Medicine(category.get(0), "药品2", "描述2");
        m.add(4, date);
        medData.add(m);

        try {
            date = dateFormat.parse("2019-08-01");
        } catch (Exception e) {
            e.printStackTrace();
        }

        m = new Medicine(category.get(1), "药品3", "描述3");
        m.add(5, date);
        medData.add(m);

        refreshData();
    }

    public static MedicineDataSet getInstance() {
        return instance;
    }

    public List<List<String>> getOutputOption2() {
        return outputOption2;
    }

    public void setOutputOption2(List<List<String>> outputOption2) {
        this.outputOption2 = outputOption2;
    }

    public List<List<List<String>>> getOutputOption3() {
        return outputOption3;
    }

    public void setOutputOption3(List<List<List<String>>> outputOption3) {
        this.outputOption3 = outputOption3;
    }

    public List<Medicine> getOutputData() {
        return outputData;
    }

    public void setOutputData(List<Medicine> outputData) {
        this.outputData = outputData;
    }

    public List<Medicine> getMedData() {
        return medData;
    }

    public void setMedData(List<Medicine> medData) {
        this.medData = medData;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public List<List<Medicine>> getMedTemp() {
        return medTemp;
    }

    public void setMedTemp(List<List<Medicine>> medTemp) {
        this.medTemp = medTemp;
    }

    public static void setInstance(MedicineDataSet instance) {
        MedicineDataSet.instance = instance;
    }

    private class ForCompare implements Comparator<Medicine> {

        @Override
        public int compare(Medicine medicine, Medicine t1) {
            if(medicine.checkExp() != 0 && t1.checkExp() == 0) {
                return -1;
            } else if(medicine.checkExp() == 0 && t1.checkExp() != 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public void refreshData() {
        Collections.sort(medData, new ForCompare());
    }

    public void add(String name, String category, int count, Date expireDate, String description) {
        Medicine medicine = null;
        for(Medicine m : medData) {
            if(m.getName().equals(name)) {
                medicine = m;
                break;
            }
        }

        if(medicine == null) {
            medicine = new Medicine();
            medData.add(medicine);
        }

        medicine.setName(name);
        medicine.setCategory(category);
        medicine.setDescription(description);
        medicine.add(count, expireDate);

        refreshData();
    }

    public void sub(String name, int count) {
        Iterator<Medicine> it = medData.iterator();
        while(it.hasNext()) {
            Medicine m = it.next();
            if(m.getName().equals(name)) {
                m.sub(count);
                if(m.getTotalCnt() <= 0) {
                    it.remove();
                }
                break;
            }
        }
    }

    public void checkExp() {
        for(Medicine m : medData) {
            m.checkExp();
        }
    }

    public void handleExp(String name) {
        Iterator<Medicine> it = medData.iterator();
        while(it.hasNext()) {
            Medicine m = it.next();
            if(m.getName().equals(name)) {
                m.handleExp();
                if(m.getTotalCnt() <= 0) {
                    it.remove();
                }
                break;
            }
        }
    }

    public void outputAdd(String name, int count) {
        Medicine m = new Medicine();
        m.setName(name);
        m.setTotalCnt(count);
        outputData.add(m);
    }

    public void outputConfirm() {
        for(Medicine m : outputData) {
            sub(m.getName(), m.getTotalCnt());
        }
        outputData.clear();
    }

    public void buildOutputOption() {
        outputOption2 = new ArrayList<>();
        outputOption3 = new ArrayList<>();
        for(int i = 0; i < category.size(); i++) {
            outputOption2.add(new ArrayList<String>());
            outputOption3.add(new ArrayList<List<String>>());
            String cat = category.get(i);
            for(int j = 0; j < medData.size(); j++) {
                Medicine m = medData.get(j);
                if(cat.equals(m.getCategory())) {
                    List<String> op2 = outputOption2.get(i);
                    op2.add(m.getName());
                    List<List<String>> op3 = outputOption3.get(i);
                    List<String> op3Op = new ArrayList<>();
                    for(int k = 1; k <= m.getTotalCnt(); k++) {
                        op3Op.add(String.valueOf(k));
                    }
                    op3.add(op3Op);
                }
            }
        }
    }
}
