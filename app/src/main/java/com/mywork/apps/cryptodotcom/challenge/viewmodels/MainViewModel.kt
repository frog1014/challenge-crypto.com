package com.mywork.apps.cryptodotcom.challenge.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mywork.apps.cryptodotcom.challenge.data.CurrencyRepository
import com.mywork.apps.cryptodotcom.challenge.data.entity.CurrencyInfo
import com.mywork.apps.cryptodotcom.challenge.di.DefaultDispatcher
import com.mywork.apps.cryptodotcom.challenge.di.IoDispatcher
import com.mywork.apps.cryptodotcom.challenge.di.ObserverCoroutineScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CurrencyRepository,
    @ObserverCoroutineScope private val externalIoScope: CoroutineScope,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _listView by lazy { MutableLiveData<ListResult>() }
    private val _selectedCurrency by lazy { MutableLiveData<CurrencyInfo>() }
    private val externalIoScopeWithExceptionHandler by lazy {
        CoroutineScope(externalIoScope.coroutineContext + coroutineExceptionHandler)
    }

    private val externalDefaultScopeWithExceptionHandler by lazy {
        CoroutineScope(externalIoScope.coroutineContext + defaultDispatcher + coroutineExceptionHandler)
    }

    private val coroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            _listView.postValue(ListResult.Failed(throwable.message))
        }
    }

    private val workerFlow = MutableSharedFlow<Flow<List<CurrencyInfo>>>()
    val selectedCurrency: LiveData<CurrencyInfo> get() = _selectedCurrency
    val listView: LiveData<ListResult> get() = _listView

    init {
        externalDefaultScopeWithExceptionHandler.launch {
            workerFlow
                .flatMapLatest {
                    it
                }
                .catch {
                    _listView.postValue(ListResult.Failed(it.message))
                }
                .collectLatest {
                    // for long running test
                    // delay(3000L)
                    _listView.postValue(ListResult.Success(it))
                }
        }
    }

    private fun getSortDataFlow() = flow {
        val data = (listView.value as? ListResult.Success)?.data
        data
            ?.takeIf { it.isNotEmpty() }
            ?.let { emit(sorted(it)) }
    }

    fun loadData() {
        val worker = repository.getAllCurrenciesFlow().flowOn(ioDispatcher)
        emitWorker(worker)
    }

    fun sortData() {
        val worker = getSortDataFlow().flowOn(defaultDispatcher)
        emitWorker(worker)
    }

    private fun emitWorker(worker: Flow<List<CurrencyInfo>>) {
        externalDefaultScopeWithExceptionHandler.launch {
            workerFlow.emit(worker)
        }
    }

    private fun sorted(list: List<CurrencyInfo>): List<CurrencyInfo> = list.sortedBy {
        // for easy to observe the list changed
        // when (System.currentTimeMillis() % 3) {
        //     1L -> it.name
        //     2L -> it.symbol
        //     else -> it.id
        // }
        it.id
    }

    override fun onCleared() {
        externalIoScope.cancel()
        super.onCleared()
    }

    fun selectedCurrency(currencyInfo: CurrencyInfo) {
        _selectedCurrency.postValue(currencyInfo)
    }

    sealed class ListResult {
        data class Success(val data: List<CurrencyInfo>) : ListResult()
        data class Failed(val msg: String? = null) : ListResult()
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}