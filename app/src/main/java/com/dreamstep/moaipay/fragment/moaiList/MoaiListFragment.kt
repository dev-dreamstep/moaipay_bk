package com.dreamstep.moaipay.fragment.moaiList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dreamstep.moaipay.R
import com.dreamstep.moaipay.data.model.MoaiGroup
import com.dreamstep.moaipay.data.model.Users
import com.dreamstep.moaipay.data.presenter.MoaiListPresenter
import com.dreamstep.moaipay.interfaces.callback.MainTabCallback
import com.dreamstep.moaipay.interfaces.callback.MoaiListCallback
import com.dreamstep.moaipay.ui.moaiList.MoaiListAdapter
import com.dreamstep.moaipay.utils.BaseFragment
import com.dreamstep.moaipay.utils.MoaiPayGlobal
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.moai_list_fragment.*

class MoaiListFragment : BaseFragment(), MoaiListCallback {

    companion object {
        fun newInstance() = MoaiListFragment()
    }

    private lateinit var presenter: MoaiListPresenter
    private var adapter = MoaiListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.moai_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerMoaiList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerMoaiList.itemAnimator = DefaultItemAnimator()
        recyclerMoaiList.adapter = adapter

        if (MoaiPayGlobal.User == null) {
            val firebaseDB = FirebaseFirestore.getInstance()
            val query =
                firebaseDB.collection("users").whereEqualTo("userId", MoaiPayGlobal.AuthUserId)
            query.addSnapshotListener { snapshot, error ->
                if (snapshot != null) {
                    val docChange = snapshot.documentChanges.first()
                    val user = docChange.document.toObject(Users::class.java)
                    user.key = docChange.document.reference

                    MoaiPayGlobal.User = user

                    presenter = MoaiListPresenter(context!!, this)
                    presenter.fetchMoaiGroup()
                }
            }
        } else {
            presenter = MoaiListPresenter(context!!, this)
            presenter.fetchMoaiGroup()
        }

        btnNewMoai.setOnClickListener {}

        nestedScrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val dy = scrollY
            val dx = scrollX
            if (dy > oldScrollY) {
                println("a")
            } else {
                println("b")
            }
        }
    }

    override fun onDestroyView() {
        recyclerMoaiList?.adapter = null
        super.onDestroyView()
    }

    override fun renderMoaiList(moaiList: ArrayList<MoaiGroup>) {
        adapter.setItems(moaiList)
        if (recyclerMoaiList != null) {
            recyclerMoaiList.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    recyclerMoaiList.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    if (recyclerMoaiList.childCount > 0) {
                        val y =
                            recyclerMoaiList.y + recyclerMoaiList.getChildAt(recyclerMoaiList.childCount - 1).y
                        nestedScrollView.smoothScrollTo(0, y.toInt())
                    }
                }
            })
        }
    }
}
