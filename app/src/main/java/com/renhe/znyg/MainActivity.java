package com.renhe.znyg;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends Activity {
    private RecycleAdapter recycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            if (this.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void initUI() {
        final Context currentCtx = this;
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
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

                    recycleAdapter = new RecycleAdapter();
                    recyclerView.setAdapter(recycleAdapter);

                    container.addView(view);
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
                            ArrayList<String> options1Items = new ArrayList<>();
                            options1Items.add("123");
                            options1Items.add("456");
                            options1Items.add("789");
                            OptionsPickerView pvOptions = new OptionsPickerBuilder(currentCtx, new OnOptionsSelectListener() {
                                @Override
                                public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                                    editText1.setText("" + options1);
                                }
                            })
                                    .setSubCalSize(30)
                                    .setTitleSize(30)
                                    .setContentTextSize(30)
                                    .build();
                            pvOptions.setPicker(options1Items);
                            pvOptions.show();
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

        navigationTabBar.setBackgroundColor(Color.parseColor("#f7fafd"));
        navigationTabBar.setBgColor(Color.parseColor("#FFFFFF"));
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

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.txt.setText(String.format("Navigation Item #%d", position));
        }

        @Override
        public int getItemCount() {
            return 1;
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
