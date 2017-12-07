package com.example.danni.domiti;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabsFragment extends Fragment {

    private AppBarLayout appBar;
    private TabLayout tabs;
    private ViewPager viewPager;

    public TabsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);

        View contenedor = (View)container.getParent();
        appBar=(AppBarLayout)contenedor.findViewById(R.id.appbar);
        tabs=new TabLayout(getActivity());

        tabs.setTabTextColors(Color.parseColor("#FFFFFF"),Color.parseColor("#FF9800"));
        appBar.addView(tabs);

        viewPager=(ViewPager)view.findViewById(R.id.pager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        appBar.removeView(tabs);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter{
        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }
        String[] tituloTabs={"Tiendas","Promociones","Carrito"};

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new TiendasFragment();
                case 1: return new PromocionesFragment();
                case 2: return new CarritoFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tituloTabs[position];

        }
    }
}
