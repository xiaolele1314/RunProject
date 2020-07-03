package com.example.run.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.run.R;
import com.example.run.model.Take;
import com.example.run.viewmodel.OrderViewModel;

import java.util.List;

public class TakeFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TakeRecyclerAdapter adapter;
    private OrderViewModel viewModel;

    public TakeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_take, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.swipe_take_fragment);
        recyclerView = view.findViewById(R.id.rec_take_fragment);
        viewModel = new ViewModelProvider(getActivity()).get(OrderViewModel.class);
        initData();

        viewModel.getTakeOrderLive().observe(getActivity(), new Observer<List<Take>>() {
            @Override
            public void onChanged(List<Take> takes) {
                adapter.setList(takes);
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        //下拉刷新显示设置
        swipeRefreshLayout.setColorSchemeResources(R.color.main_blue,R.color.main_yellow,R.color.main_red);
        swipeRefreshLayout.setDistanceToTriggerSync(300);
        swipeRefreshLayout.setProgressViewOffset(true,100,150);
        swipeRefreshLayout.setProgressViewEndTarget(true,200);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.findTakeOrder();
            }
        });

    }

    private void initData() {
        adapter = new TakeRecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }
}
