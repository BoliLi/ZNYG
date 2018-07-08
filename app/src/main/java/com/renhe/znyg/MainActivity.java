package com.renhe.znyg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends Activity {
    private MedicineListAdapter medicineListAdapter;
    private OutputListAdapter outputListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    @Override
    protected void onStart() {

        super.onStart();
        if(medicineListAdapter != null) {
            medicineListAdapter.notifyDataSetChanged();
        }

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            if (this.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private boolean checkInput() {
        final EditText editText1 = (EditText) findViewById(R.id.et1);
        if(editText1.getText().toString() == null || editText1.getText().toString().trim() == "") {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("输入错误")
                    .setMessage("请输入药品分类。")
                    .setPositiveButton("确定", null)
                    .create();
            alertDialog.show();
            return false;
        }

        final EditText editText2 = (EditText) findViewById(R.id.et2);
        if(editText2.getText().toString() == null || editText2.getText().toString().trim() == "") {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("输入错误")
                    .setMessage("请输入药品名称。")
                    .setPositiveButton("确定", null)
                    .create();
            alertDialog.show();
            return false;
        }

        final EditText editText3 = (EditText) findViewById(R.id.et3);
        if(editText3.getText().toString() == null || editText3.getText().toString().trim() == "") {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("输入错误")
                    .setMessage("请输入药品数量。")
                    .setPositiveButton("确定", null)
                    .create();
            alertDialog.show();
            return false;
        }

        final EditText editText5 = (EditText) findViewById(R.id.et5);
        if(editText5.getText().toString() == null || editText5.getText().toString().trim() == "") {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("输入错误")
                    .setMessage("请输入药品过期时间。")
                    .setPositiveButton("确定", null)
                    .create();
            alertDialog.show();
            return false;
        }

        return true;
    }

    private void clearInput() {
        final EditText editText1 = (EditText) findViewById(R.id.et1);
        editText1.setText("");
        final EditText editText2 = (EditText) findViewById(R.id.et2);
        editText2.setText("");
        final EditText editText3 = (EditText) findViewById(R.id.et3);
        editText3.setText("");
        final EditText editText4 = (EditText) findViewById(R.id.et4);
        editText4.setText("");
        final EditText editText5 = (EditText) findViewById(R.id.et5);
        editText5.setText("");
    }

    private void initUI() {
        final Context currentCtx = this;
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final ViewGroup container, final int position, final Object object) {
                Log.i("LBL", "1position:" + position);
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                Log.i("LBL", "2position:" + position);
                View view = null;
                if (position == 0) {
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.medicine_list_vp, null, false);

                    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(
                                    getBaseContext(), LinearLayoutManager.VERTICAL, false
                            )
                    );

                    medicineListAdapter = new MedicineListAdapter();
                    recyclerView.setAdapter(medicineListAdapter);

                    container.addView(view);
                } else if(position == 2) {
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.output_vp, null, false);

                    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(
                                    getBaseContext(), LinearLayoutManager.VERTICAL, false
                            )
                    );

                    outputListAdapter = new OutputListAdapter();
                    recyclerView.setAdapter(outputListAdapter);
                    container.addView(view);

                    final FloatingActionMenu menuRed = (FloatingActionMenu) view.findViewById(R.id.menu_red);
                    menuRed.setClosedOnTouchOutside(true);
                    final FloatingActionButton fab1 = view.findViewById(R.id.fab1);
                    fab1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            menuRed.close(true);
                            MedicineDataSet mds = MedicineDataSet.getInstance();
                            mds.buildOutputOption();
                            OptionsPickerView opv = new OptionsPickerBuilder(currentCtx, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                                    MedicineDataSet mds = MedicineDataSet.getInstance();
                                    List<List<String>> op2 = mds.getOutputOption2();
                                    String name = op2.get(options1).get(options2);
                                    mds.outputAdd(name, options3 + 1);
                                    outputListAdapter.notifyDataSetChanged();
                                }
                            }).setSubCalSize(30)
                                    .setTitleSize(30)
                                    .setContentTextSize(30)
                                    .build();

                            opv.setPicker(mds.getCategory(), mds.getOutputOption2(), mds.getOutputOption3());
                            opv.show();
                        }
                    });

                    FloatingActionButton fab2 = view.findViewById(R.id.fab2);
                    fab2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            menuRed.close(true);
                            MedicineDataSet mds = MedicineDataSet.getInstance();
                            mds.outputConfirm();
                            outputListAdapter.notifyDataSetChanged();
                            medicineListAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.input_vp, null, false);
                    container.addView(view);

                    final EditText editText1 = (EditText) findViewById(R.id.et1);
                    editText1.setInputType(InputType.TYPE_NULL);

                    editText1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i("LBL", "editText1 onClick");
                            hideKeyboard();
                            OptionsPickerView pvOptions = new OptionsPickerBuilder(currentCtx, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                                    MedicineDataSet mds = MedicineDataSet.getInstance();
                                    editText1.setText(mds.getCategory().get(options1));
                                }
                            })
                                    .setSubCalSize(30)
                                    .setTitleSize(30)
                                    .setContentTextSize(30)
                                    .build();
                            MedicineDataSet mds = MedicineDataSet.getInstance();
                            pvOptions.setPicker(mds.getCategory());
                            pvOptions.show();
                        }
                    });

                    final EditText editText5 = (EditText) findViewById(R.id.et5);
                    editText5.setInputType(InputType.TYPE_NULL);
                    editText5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.i("LBL", "editText5 onClick");
                            hideKeyboard();
                            TimePickerView tpv = new TimePickerBuilder(currentCtx, new OnTimeSelectListener() {
                                @Override
                                public void onTimeSelect(Date date, View v) {
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    editText5.setText(formatter.format(date));
                                }
                            }).setSubCalSize(30)
                                    .setTitleSize(30)
                                    .setContentTextSize(30)
                                    .setType(new boolean[]{true, true, true, false, false, false})
                                    .build();
                            tpv.show();
                        }
                    });

                    final Button button1 = (Button) findViewById(R.id.btn1);
                    button1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!checkInput()) {
                                return;
                            }

                            String category = ((EditText) findViewById(R.id.et1)).getText().toString();
                            String name = ((EditText) findViewById(R.id.et2)).getText().toString();
                            int count = 0;
                            try {
                                count = Integer.parseInt(((EditText) findViewById(R.id.et3)).getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            String description = ((EditText) findViewById(R.id.et4)).getText().toString();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date expDate = null;
                            try {
                                expDate = dateFormat.parse(((EditText) findViewById(R.id.et5)).getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MedicineDataSet mds = MedicineDataSet.getInstance();
                            mds.add(name, category, count, expDate, description);
                            medicineListAdapter.notifyDataSetChanged();
                            clearInput();
                        }
                    });

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            hideKeyboard();
                        }
                    });
                }

                return view;
            }
        });

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_warehouse1, null),
                        Color.parseColor("#73beff"))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_sixth))
                        .title("库存")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_inwarehouse, null),
                        Color.parseColor("#73beff"))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("入库")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_outwarehouse, null),
                        Color.parseColor("#73beff"))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("出库")
                        .badgeTitle("with")
                        .build()
        );

        navigationTabBar.setBackgroundColor(Color.parseColor("#f7fafd"));
        navigationTabBar.setBgColor(Color.parseColor("#F0F0F0"));
        navigationTabBar.setTitleSize(16);
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
        navigationTabBar.setIconSizeFraction(0.5f);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
    }

    public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            MedicineDataSet mds = MedicineDataSet.getInstance();
            Medicine m = mds.getMedData().get(position);
            holder.txt.setText(String.format("name:%s TotalCnt:%d expCnt:%d", m.getName(), m.getTotalCnt(), m.getExpCnt()));
            Log.i("LBL", String.format("name:%s TotalCnt:%d expCnt:%d", m.getName(), m.getTotalCnt(), m.getExpCnt()));
            if(m.getExpCnt() > 0) {
                holder.txt.setBackgroundResource(R.drawable.bg_round_rect_red);
            } else {
                holder.txt.setBackgroundResource(R.drawable.bg_round_rect);
            }

            holder.txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("pos", position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            MedicineDataSet mds = MedicineDataSet.getInstance();
            return mds.getMedData().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txt;

            public ViewHolder(final View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
            }
        }
    }

    public class OutputListAdapter extends RecyclerView.Adapter<OutputListAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            MedicineDataSet mds = MedicineDataSet.getInstance();
            Medicine m = mds.getOutputData().get(position);
            holder.txt.setText(String.format("name:%s TotalCnt:%d", m.getName(), m.getTotalCnt()));
        }

        @Override
        public int getItemCount() {
            MedicineDataSet mds = MedicineDataSet.getInstance();
            return mds.getOutputData().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txt;

            public ViewHolder(final View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
            }
        }
    }
}
