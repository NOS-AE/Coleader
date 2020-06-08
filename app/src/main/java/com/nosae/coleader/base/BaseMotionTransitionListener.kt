package com.nosae.coleader.base

import androidx.constraintlayout.motion.widget.MotionLayout

/**
 * Create by NOSAE on 2020/5/6
 */
abstract class BaseMotionTransitionListener: MotionLayout.TransitionListener {
    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}
    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}
    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {}
}