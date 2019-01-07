//package cms.co.in.kat.fragment;
//
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//
//import java.util.List;
//
//import cms.co.in.kat.R;
//import cms.co.in.kat.activity.CauseList;
//import cms.co.in.kat.objectholders.Hearing;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class Fragment1 extends Fragment {
//
//
//    RecyclerView recyclerView;
////    private CauseListAdapter mAdapter;
//    private List<Hearing> hearingList;
//    private CauseList a;
//
//
//    public Fragment1(List<Hearing> HearingList) {
//        this.hearingList=HearingList;
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View v = inflater.inflate(R.layout.fragment_fragment1, container, false);
//        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
//
//        mAdapter = new CauseListAdapter(hearingList);
//        recyclerView.setAdapter(mAdapter);
//        return v;
//    }
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        if (context instanceof Activity){
//            a=(CauseList) context;
//        }
//    }
//}
