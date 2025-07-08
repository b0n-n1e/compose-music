package com.loki.center.mine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.utils.extension.TAG
import com.loki.utils.network.bean.login.QrCheckResponse
import com.loki.utils.network.bean.login.QrCreateResponse
import com.loki.utils.network.bean.login.QrKeyResponse
import com.loki.utils.network.service.LoginService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class QrLoginUiState {
    object Loading : QrLoginUiState()
    data class ShowQr(val qrImg: String?, val qrUrl: String?) : QrLoginUiState()
    data class Error(val msg: String) : QrLoginUiState()
    data class Success(val cookie: String) : QrLoginUiState()
    data class Status(val statusMsg: String) : QrLoginUiState()
}

@HiltViewModel
class QrLoginViewModel @Inject constructor(
    private val loginService: LoginService
) : ViewModel() {
    private val _uiState = MutableStateFlow<QrLoginUiState>(QrLoginUiState.Loading)
    val uiState: StateFlow<QrLoginUiState> = _uiState.asStateFlow()

    private var qrKey: String? = null
    private var pollingJob: Job? = null

    fun startQrLogin() {
        _uiState.value = QrLoginUiState.Loading
        viewModelScope.launch {
            try {
                val keyResp: QrKeyResponse = loginService.getQrKey()
                val key = keyResp.data?.unikey
                if (key.isNullOrEmpty()) {
                    _uiState.value = QrLoginUiState.Error("获取二维码key失败")
                    Log.e(TAG, "startQrLogin: 获取二维码key失败")
                    return@launch
                }
                qrKey = key
                val qrResp: QrCreateResponse = loginService.createQr(key)
                val qrImg = qrResp.data?.qrimg
                val qrUrl = qrResp.data?.qrurl
                if (qrImg.isNullOrEmpty() && qrUrl.isNullOrEmpty()) {
                    _uiState.value = QrLoginUiState.Error("获取二维码失败")
                    Log.e(TAG, "startQrLogin: 获取二维码失败")
                    return@launch
                }
                _uiState.value = QrLoginUiState.ShowQr(qrImg, qrUrl)
                startPolling()
            } catch (e: Exception) {
                _uiState.value = QrLoginUiState.Error("网络异常: ${e.message}")
                Log.e(TAG, "startQrLogin: 网络异常",e)
            }
        }
    }

    fun refreshQr() {
        stopPolling()
        startQrLogin()
    }

    private fun startPolling() {
        stopPolling()
        pollingJob = viewModelScope.launch {
            while (true) {
                delay(2000)
                val key = qrKey ?: break
                try {
                    val checkResp: QrCheckResponse = loginService.checkQrStatus(key)
                    when (checkResp.code) {
                        800 -> {
                            _uiState.value = QrLoginUiState.Error("二维码已过期，请刷新")
                            stopPolling()
                            break
                        }
                        801 -> {
                            _uiState.value = QrLoginUiState.Status("等待扫码...")
                        }
                        802 -> {
                            _uiState.value = QrLoginUiState.Status("已扫码，等待确认...")
                        }
                        803 -> {
                            _uiState.value = QrLoginUiState.Success(checkResp.cookie ?: "")
                            stopPolling()
                            break
                        }
                        else -> {
                            _uiState.value = QrLoginUiState.Status(checkResp.message ?: "未知状态")
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = QrLoginUiState.Error("轮询失败: ${e.message}")
                    stopPolling()
                    break
                }
            }
        }
    }

    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
} 