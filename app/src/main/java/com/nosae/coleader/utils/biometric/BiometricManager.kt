package com.nosae.coleader.utils.biometric

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleObserver
import com.nosae.coleader.utils.aboveApi

/**
 * Create by NOSAE on 2020/6/13
 */
class BiometricManager private constructor(private val mActivity: FragmentActivity) {

    private val mImpl = BiometricPromptHelper(mActivity)

    companion object {
        fun from(activity: FragmentActivity) = BiometricManager(activity)
    }

    interface OnBiometriIndentifyCallback {
        fun onPassword() {}
        fun onSuccess(result: BiometricPrompt.AuthenticationResult) {}
        fun onFailed() {}
        fun onError(errCode: Int, reason: String) {}
        fun onCancel() {}
    }

    fun authenticate(cb: OnBiometriIndentifyCallback) {
        mImpl.authenticate(null, cb)
    }

    fun hasEnrolledFingerprints() = if (aboveApi(23)) {
        val manager = mActivity.getSystemService(FingerprintManager::class.java)
        manager != null && manager.hasEnrolledFingerprints()
    } else {
        false
    }

    fun isHardwareDetected() = if (aboveApi(23)) {
        val manager = mActivity.getSystemService(FingerprintManager::class.java)
        manager != null && manager.isHardwareDetected
    } else {
        false
    }

    fun isKeyguardSecure() = (mActivity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager?)
        ?.isKeyguardSecure ?: false

    fun isBiometricPromptEnable() = isHardwareDetected()
            && hasEnrolledFingerprints()
            && isKeyguardSecure()
}