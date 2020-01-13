package com.deemo.gesture

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.deemo.library.gesture.bean.SwipeBean
import com.deemo.library.gesture.utils.PermissiontUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gesture.setActionListener { dots, paths ->


        }



        main_button.setOnClickListener {
            if (PermissiontUtils.isAccessibilityOn(this)) {
                PermissiontUtils.startServer(this)


                gesture.swipeList.firstOrNull()?.let {
                    Log.d("Main", it.toString())

                    val list = arrayListOf<Pair<Float, Float>>()
                    it.paths.forEach {
                        list.add(Pair(it.x, it.y))
                    }

                    EventBus.getDefault().post(SwipeBean(list, it.duration))
                }


                gesture.reset()

            } else {
                PermissiontUtils.openSetting(this)
            }
        }
    }
}
