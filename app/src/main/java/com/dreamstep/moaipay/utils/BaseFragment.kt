package com.dreamstep.moaipay.utils

import androidx.fragment.app.Fragment
import com.dreamstep.moaipay.interfaces.callback.MainTabCallback

abstract class BaseFragment: Fragment() {

    lateinit var mainTabCallback: MainTabCallback

}