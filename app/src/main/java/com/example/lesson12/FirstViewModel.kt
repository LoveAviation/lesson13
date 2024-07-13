package com.example.lesson12

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FirstViewModel : ViewModel() {

    var inputText = MutableStateFlow("")
    var displayText: MutableLiveData<String> = MutableLiveData()
    private val _state = MutableStateFlow<State>(State.succes)
    var state = _state.asStateFlow()

    private lateinit var loadingScope : Job
    private var handler = Handler(Looper.getMainLooper())

    init {
        inputText.debounce(300).onEach {
            if(_state.value == State.loading) loadingScope.cancel()
            _state.value = State.stop
            if(inputText.value.trim().length > 2){
                loading()
            }
        }.launchIn(viewModelScope)
    }


    private fun loading(){
        loadingScope = CoroutineScope(Dispatchers.Default).launch {
            _state.value = State.loading
            delay(5000)
            _state.value = State.succes
            handler.post {
                displayText.value = "По вашему запросу ${inputText.value.trim()} ничего не найдено"
            }
        }
    }
}