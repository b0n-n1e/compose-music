# MVI架构说明

本项目采用MVI（Model-View-Intent）架构模式，这是一种单向数据流的架构模式，特别适合Jetpack Compose应用。

## 架构组件

### 1. State（状态）
- 表示UI的当前状态
- 是不可变的数据类
- 包含所有UI需要显示的数据

```kotlin
data class SearchState(
    val searchResults: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val searchKeyword: String = "",
    val errorMessage: String? = null
) : MviState
```

### 2. Intent（意图）
- 表示用户的意图或系统事件
- 使用sealed class定义所有可能的用户操作
- 单向流入ViewModel

```kotlin
sealed class SearchIntent : MviIntent {
    data class SearchSongs(val keyword: String) : SearchIntent()
    data class UpdateSearchKeyword(val keyword: String) : SearchIntent()
    object ClearSearch : SearchIntent()
    object RetrySearch : SearchIntent()
}
```

### 3. Effect（效果）
- 表示一次性效果，如导航、显示Toast等
- 使用sealed class定义所有可能的副作用
- 单向流出ViewModel

```kotlin
sealed class SearchEffect : MviEffect {
    data class ShowToast(override val message: String) : SearchEffect(), ToastEffect
    data class ShowError(override val message: String) : SearchEffect(), ToastEffect
    object HideKeyboard : SearchEffect()
    object ClearFocus : SearchEffect()
}
```

## 数据流

```
User Action → Intent → ViewModel → State → UI
                ↓
            Effect → UI (一次性效果)
```

1. **用户操作** → 发送Intent
2. **ViewModel** → 处理Intent，更新State
3. **UI** → 观察State变化，更新界面
4. **ViewModel** → 发送Effect处理副作用

## 使用方法

### 1. 创建ViewModel

```kotlin
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchService: SearchService
) : MviViewModel<SearchState, SearchIntent, SearchEffect>() {

    private val _intent = MutableSharedFlow<SearchIntent>()
    val intent = _intent.asSharedFlow()

    override fun createInitialState(): SearchState = SearchState()

    override suspend fun handleIntents() {
        intent.collect { intent ->
            when (intent) {
                is SearchIntent.SearchSongs -> searchSongs(intent.keyword)
                is SearchIntent.UpdateSearchKeyword -> updateSearchKeyword(intent.keyword)
                // ... 处理其他Intent
            }
        }
    }

    // 发送Intent的方法
    fun sendIntent(searchIntent: SearchIntent) {
        viewModelScope.launch {
            _intent.emit(searchIntent)
        }
    }

    private fun searchSongs(keyword: String) {
        setState { copy(isLoading = true) }
        // 执行搜索逻辑
        setState { copy(searchResults = results, isLoading = false) }
        setEffect { SearchEffect.ShowToast("搜索完成") }
    }
}
```

### 2. 创建Composable

```kotlin
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.viewState.collectAsState()

    // 处理Effect
    HandleEffects(
        effects = viewModel.effect,
        onEffect = { effect ->
            when (effect) {
                SearchEffect.HideKeyboard -> keyboardController?.hide()
                SearchEffect.ClearFocus -> focusManager.clearFocus()
            }
        }
    )

    // UI组件
    OutlinedTextField(
        value = state.searchKeyword,
        onValueChange = { keyword ->
            viewModel.sendIntent(SearchIntent.UpdateSearchKeyword(keyword))
        }
    )
}
```

## 优势

1. **单向数据流**：数据流向清晰，易于调试
2. **状态可预测**：所有状态变化都通过Intent触发
3. **副作用隔离**：Effect专门处理副作用，不污染状态
4. **类型安全**：使用sealed class确保类型安全
5. **可测试性**：每个组件都可以独立测试

## 最佳实践

1. **State应该是不可变的**：使用data class的copy方法更新状态
2. **Intent应该表示用户意图**：不要直接操作数据，而是发送意图
3. **Effect应该是一次性的**：如Toast、导航等
4. **使用防抖**：对于搜索等频繁操作，使用debounce
5. **错误处理**：在State中包含错误信息，通过Effect显示错误提示

## 扩展功能

### ToastEffect接口
所有需要显示Toast的Effect都可以实现ToastEffect接口，统一处理：

```kotlin
interface ToastEffect : MviEffect {
    val message: String
}
```

### HandleEffects组件
通用的Effect处理器，自动处理Toast效果：

```kotlin
@Composable
fun <T : MviEffect> HandleEffects(
    effects: SharedFlow<T>,
    onEffect: (T) -> Unit
)
``` 