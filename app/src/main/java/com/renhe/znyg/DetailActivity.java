package com.renhe.znyg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos", 0);
        final MedicineDataSet mds = MedicineDataSet.getInstance();
        final Medicine m = mds.getMedData().get(pos);
        final EditText editText1 = (EditText) findViewById(R.id.et1);
        editText1.setInputType(InputType.TYPE_NULL);
        editText1.setText(m.getCategory());
        final EditText editText2 = (EditText) findViewById(R.id.et2);
        editText2.setInputType(InputType.TYPE_NULL);
        editText2.setText(m.getName());
        final EditText editText3 = (EditText) findViewById(R.id.et3);
        editText3.setInputType(InputType.TYPE_NULL);
        editText3.setText(String.valueOf(m.getTotalCnt()));
        final EditText editText4 = (EditText) findViewById(R.id.et4);
        editText4.setInputType(InputType.TYPE_NULL);
        editText4.setText(m.getDescription());

        if(m.getExpCnt() > 0) {
            final Button btn = findViewById(R.id.btn1);
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog alertDialog = new AlertDialog.Builder(DetailActivity.this)
                            .setTitle("处理过期药品")
                            .setMessage("请处理" + m.getName() + "，数量：" + m.getExpCnt())
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    btn.setVisibility(View.INVISIBLE);
                                    mds.handleExp(m.getName());
                                }
                            })
                            .create();
                    alertDialog.show();
                }
            });
        }
    }
}
